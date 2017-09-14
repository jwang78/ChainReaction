package model;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.Constants;

public class CRBoard {

	private Piece[][] _board;
	private boolean _changed = true;
	private boolean _started = false;

	public CRBoard(int dimX, int dimY) {
		_board = new Piece[dimX][dimY];
		this.initializeBPs();
	}

	private void initializeBPs() {
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				int numNeighbors = this.neighbors(i, j).size();
				Piece bp = new Piece(Player.getNullPlayer(), 0,
				        numNeighbors - 1);
				this.set(bp, i, j);
			}
		}
	}

	public CRBoard copy() {
		return new CRBoard(this);
	}

	public int getWidth() {
		return _board.length;
	}

	public int getHeight() {
		return _board[0].length;
	}

	public CRBoard(CRBoard b) {
		this(b.getWidth(), b.getHeight());
		for (int i = 0; i < _board.length; i++) {
			for (int j = 0; j < _board[0].length; j++) {
				this.set(b.get(i, j).copy(), i, j);
			}
		}
	}

	public Piece get(int x, int y) {
		return _board[x][y];
	}

	public void set(Piece bp, int x, int y) {
		_board[x][y] = bp;
	}

	public boolean applyMove(Move m) {
		if (m == Move.PASS_MOVE) {
			throw new IllegalArgumentException("Player attempted to pass");
		}
		if (!isValidMove(m)) {
			return false;
		}
		this.updateTile(m.getPlayer(), m.getX(), m.getY(), Constants.MAX_DEPTH_UPDATE);
		return true;
	}

	private void updateTile(Player causer, int x, int y, int depth) {
		
		Piece bp = this.get(x, y);
		bp.setPlayer(causer);
		if (depth <= 0) {
			return;
		}
		bp.increment();
		if (bp.getQuantity() > bp.getCarryingCapacity()) {
			bp.setQty(bp.getQuantity() - bp.getCarryingCapacity() - 1);
			if (bp.getQuantity() == 0) {
				bp.setPlayer(Player.getNullPlayer());
			}
			for (int[] coords : this.neighbors(x, y)) {
				/*
				 * if (coords[0] == oldX && coords[1] == oldY) { return; }
				 */
				this.updateTile(causer, coords[0], coords[1], depth - 1);
			}
		}
	}

	public List<int[]> neighbors(int x, int y) {
		List<int[]> neighbors = new ArrayList<>();
		if (x > 0) {
			neighbors.add(new int[] { x - 1, y });
		}
		if (y > 0) {
			neighbors.add(new int[] { x, y - 1 });
		}
		if (x < this.getWidth() - 1) {
			neighbors.add(new int[] { x + 1, y });
		}
		if (y < this.getHeight() - 1) {
			neighbors.add(new int[] { x, y + 1 });
		}
		return neighbors;
	}

	public boolean isValidMove(Move m) {
		Piece bp = this.get(m.getX(), m.getY());
		return bp.getPlayer() == null || m.getPlayer().equals(bp.getPlayer()) || bp.getPlayer().equals(Player.getNullPlayer());
	}

	public Map<Player, Integer> getScoreMap() {
		Map<Player, Integer> mp = new HashMap<>();
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				Piece bp = this.get(i, j);
				if (bp.getPlayer() != null) {
					Player p = bp.getPlayer();
					if (!mp.containsKey(p)) {
						mp.put(p, 1);
					} else {
						mp.put(p, mp.get(p) + 1);
					}
				}
			}
		}
		return mp;
	}

	// Careful about the null player
	public List<Move> getViableMoves(Player p) {
		List<Move> moves = new ArrayList<>();
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				Move m = new Move(p, i, j);
				if (this.isValidMove(m)) {
					moves.add(m);
				}
			}
		}
		return moves;
	}

	public void updated() {
		_changed = true;
	}

	public boolean hasBeenChanged() {
		return _changed;
	}

	// 2; 1 for null player and 1 for winner
	public boolean isTerminal() {
		return this.getScoreMap().keySet().size() == 2 && this.getScoreMap().values().stream().min((a, b)->(a-b)).get() > 1;
	}

	public static CRBoard getCRBoard(String string,
	        Map<Integer, Player> playerMap) {
		string = string.replaceAll("\\s", "");
		// Get (width, height, data) from string
		String[] whd = string.split("/");
		
		// Piece data
		String[] pieces = whd[2].split(">");
		
		int w, h;
		try {
			w = Integer.parseInt(whd[0]);
			h = Integer.parseInt(whd[1]);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Board string was in incorrect format.");
		}
		CRBoard board = new CRBoard(w, h);
		for (String pieceStr : pieces) {
			pieceStr = pieceStr.replaceAll("\\(|\\)", "");
			String[] components = pieceStr.split(":");
			String[] coordinates = components[0].split(",");
			String[] idqty = components[1].split(";");
			int x,y,id,qty, cc;
			try {
				x = Integer.parseInt(coordinates[0]);
				y = Integer.parseInt(coordinates[1]);
				id = Integer.parseInt(idqty[0]);
				qty = Integer.parseInt(idqty[1]);
				cc = Integer.parseInt(idqty[2]);
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException("Board string was in incorrect format:"+pieceStr);
			}
			board.set(new Piece(playerMap.get(id), qty, cc), x, y);
		}
		return board;
	}

	@Override
	/**
	 * Generates the string representation of this board in the format (x,
	 * y):id;quantity;carryingcapacity>
	 * 
	 */
	public String toString() {
		String s = this.getWidth() + "/" + this.getHeight() + "/";
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				Piece p = this.get(i, j);
				s += "(" + i + ", " + j + "):";
				s += p.toString();
				s += ">";
			}
			
		}
		return s.substring(0, s.length()-1);
	}

}
