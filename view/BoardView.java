package ChainReaction.view;

import java.util.List;

import ChainReaction.model.Move;
import ChainReaction.model.CRBoard;

/**
 * Represents the board viewing part of the user interface. These methods are
 * intended to allow display of the board and legal moves.
 */
public interface BoardView {
    
    /**
     * Updates the BoardView with the board.
     * @param board The current board
     * @param legalMoves A list of legal moves
     * @param highlights Any moves to highlight
     * */
    public void update(CRBoard board, Move last, List<Move> legalMoves, Move... highlights);
    
    /**
     * Returns the node of this BoardView
     * @return The node of this BoardView
     * */
    public javafx.scene.layout.Pane getNode();
    
}
