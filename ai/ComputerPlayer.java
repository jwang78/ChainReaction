package ai;

import java.util.ArrayList;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import game.Constants;
import model.Move;
import model.CRBoard;
import model.Piece;
import model.Player;
import model.PlayerType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * The default implementation of the computer player; uses an alpha-beta pruning
 * algorithm to achieve greater depth than standard minimax. Computer players
 * are given a set of board weights, a difficulty, and a color when created.
 * When a computer player is notified of its turn with the current board
 * position, it calculates its response and returns it asynchronously. This
 * class can be subclassed for a different implementation; the findBestMoves,
 * evaluatePosition, and evaluateBoardPosition have been made protected to allow
 * subclasses and AI-related classes to access them. Multithreaded support up to
 * the number of moves available. Note that attempting a search with depth > 7
 * may take a long time to process.
 * 
 */
public class ComputerPlayer extends Player {
    // The depth at which this player searches; depth = 0 means only a heuristic
    // evaluation is performed.
    private int _depth;
    // The weights of each tile on the board; dimensions must be consistent with
    // boards provided on turn notification.
    // Whether the ComputerPlayer is currently working; if so, will not respond
    // to more turn notifications.
    private boolean _computing;
    // Computer wins evaluation
    private static final double WIN = Double.POSITIVE_INFINITY;
    // Computer loses evaluation
    private static final double LOSE = Double.NEGATIVE_INFINITY;
    // The currently active futures; when the AI is killed, these are cancelled.
    private List<Future> _activeFutures = new ArrayList();
    // The executor that will run the evaluation algorithm for each possible
    // move.
    private ExecutorService _executor;
    // Whether this AI is dead.
    private boolean _dead;
    
    /**
     * Creates a computer player with the specified piece fill, difficulty,
     * board weights, name, and ID.
     * 
     * @param paint
     *            The color/image fill that this player's pieces will have.
     * @param difficulty
     *            The depth at which the computer will search for the best move;
     *            0 for direct evaluation
     * @param weights
     *            The board weights with which this computer player will
     *            evaluate the move.
     * @param name
     *            This computer player's assigned name.
     * @param ID
     *            The unique identification number assigned to this player.
     */
    public ComputerPlayer(Paint paint, int difficulty, String name, int ID) {
        super(paint, PlayerType.COMPUTER, name, ID);
        _depth = difficulty;
        _executor = Executors.newFixedThreadPool(Constants.NUM_THREADS);
    }
    
    /**
     * Notifies the computer player of its turn; initializes the computing
     * process for the best move if not already computing.
     * 
     * @param board
     *            The board on which this computer is supposed to evaluate and
     *            place a move.
     * @param otherPlayer
     *            The primary antagonist for this player to play against. This
     *            AI does not support more than two players.
     */
    @Override
    public void notifyOfTurn(final CRBoard board, Player otherPlayer) {
        // Checks if the AI is currently computing a move
        if (_computing) {
            return;
        }
        // Indicates currently computing
        _computing = true;
        new Thread(() -> {
            List<Move> bestMoves = this.findBestMoves(board, otherPlayer);
            this.setMove(bestMoves.get((int) (bestMoves.size() * Math.random())));
            // Computation has finished
            _computing = false;
        }).start();
    }
    
    public List<Move> findBestMoves(CRBoard board, Player otherPlayer) {
        // Gets the legal moves
		board = board.copy();
        List<Move> moves = board.getViableMoves(this);
        
        // No moves
        if (moves.isEmpty()) {
            return Arrays.asList(Move.PASS_MOVE);
        }
        
        // Creates and populates the list of possible board permutations
        List<CRBoard> boards = new ArrayList();
        for (Move move : moves) {
            CRBoard b = board.copy();
            b.applyMove(move);
            boards.add(b);
        }
        
        // Maps moves to their evaluations, based on evaluateBoardPosition()
        Map<Move, Double> evaluations = new ConcurrentHashMap<Move, Double>();
        List<Future> futures = new ArrayList();
        for (int i = 0; i < boards.size(); i++) {
            CRBoard b = boards.get(i);
            final int x = i;
            Runnable task = () -> {
                evaluations.put(moves.get(x), -this.evaluateBoardPosition(b, otherPlayer, this, _depth - 1,
                        Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
            };
            
            Future f = _executor.submit(task);
            futures.add(f);
            _activeFutures.add(f);
        }
        
        // Waits for all computations to finish before progressing
        for (Future f : futures) {
            try {
                if (!f.isCancelled()) {
                    f.get();
                }
                _activeFutures.remove(f);
            } catch (CancellationException ex) {
                // Throw away, since this is normal
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Finding the moves that are within the tolerance
        double bestEval = Collections.max(evaluations.values());
        List<Move> goodMoves = new ArrayList<Move>();
        for (Move moove : evaluations.keySet()) {
            if (evaluations.get(moove) + Constants.MOVE_TOLERANCE >= bestEval) {
                goodMoves.add(moove);
            }
        }
        // Sorting the moves based on their evaluations
        Collections.sort(goodMoves, new Comparator<Move>() {
            @Override
            public int compare(Move arg0, Move arg1) {
                return (int) (evaluations.get(arg1) - evaluations.get(arg0));
            }
        });
        
        // Returns the top NUM_MOVES moves in the list.
        return goodMoves.subList(0, Math.min(Constants.NUM_MOVES, goodMoves.size()));
    }
	protected double evaluateBoardPosition(CRBoard board, Player firstPlayer, Player otherPlayer, int depth,
            double a, double b) {
        // Whether the computer has been ordered to cease computation.
        if (Thread.interrupted() || this.isDead()) {
            _dead = true;
            return 0;
        }
        
        // If no depth, then returns the heuristic evaluation.
        if (depth == 0) {
            return this.evaluatePosition(board, firstPlayer);
        }
        
        // The possible moves
        List<Move> possibleMoves = board.getViableMoves(firstPlayer);
        
        // No possible moves in this position
        if (possibleMoves.isEmpty()) {
            // Adds the pass move to the list of possible moves
            // If the current player has a higher count than the other players,
            // returns a win, else a loss.
                Map<Player, Integer> scoreMap = board.getScoreMap();
                if (scoreMap.get(firstPlayer) == Collections.max(scoreMap.values())) {
                    return WIN;
                } else {
                    return LOSE;
                }
            
        }
        List<CRBoard> possibleBoards = new ArrayList();
        // Constructs the possible CRBoards resulting from each move.
        possibleMoves.iterator().forEachRemaining((Move m) -> {
            CRBoard booard = new CRBoard(board);
            booard.applyMove(m);
            possibleBoards.add(booard);
        });
        // The worst evaluation possible
        double eval = LOSE;
        for (CRBoard booard : possibleBoards) {
            if (Thread.interrupted()) {
                return 0;
            }
            // Evaluation of the modified board position, from the other
            // player's perspective
            double val = -this.evaluateBoardPosition(booard, otherPlayer, firstPlayer, depth - 1, -b, -a);
            // If this evaluation is better than the previous, then we should
            // take this path and set the evaluation to it.
            eval = Math.max(val, eval);
            // Increasing the maximum score assured.
            a = Math.max(val, a);
            /*
             * Cutoff if the other player's minimum assured score is better than
             * the evaluation; the other player will not make this move tree
             * possible so there is no point in exploring further.
             */
            if (b <= a) {
                break;
            }
        }
        return eval;
    }
	private static double evaluatePosition(CRBoard board, Player Player) {
		double val = 0;
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				val += squareValue(board, Player, i, j);
			}
		}
		return val;
	}
	
	private static double squareValue(CRBoard board, Player player, int x, int y) {
		Piece boardpos = board.get(x, y);
		if (!player.equals(boardpos.getPlayer())) {
			return 0;
		}
		
		double value = boardpos.getQuantity() + 1;
		List<int[]> coords = board.neighbors(x, y);
		boolean badguys = false;
		/*for (int[] coord : coords) {
			BoardPosition bp = board.get(coord[0], coord[1]);
			if (bp.getQuantity() == bp.getCarryingCapacity()) {
				if (bp.getPlayer() == player) {
					value += 0.1;
				}
				else {
					value -= bp.getCarryingCapacity() - 5;
					badguys = true;
				}
			}
		}*/
		if (!badguys) {
			value += (boardpos.getQuantity() == boardpos.getCarryingCapacity())? 2 : 0;
			if (x == 0 || x == board.getWidth() - 1) {
				if (y == board.getHeight() - 1 || y == 0) {
					value += 3;
				}
			}
			else if (x == 0 || y == 0 || x == board.getWidth() - 1 || y == board.getHeight() - 1) {
				value += 2;
			}
		}
		
		return value;
	}
	private boolean isDead() {
		return _dead;
	}
    
    /**
     * Orders the computer player to cease calculation, as it is no longer needed.
     * */
    @Override
    public void die() {
        _dead = true;
        for (Future f : _activeFutures) {
            f.cancel(true);
        }
        _executor.shutdownNow();
    }
    
}
