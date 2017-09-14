package ChainReaction.model;

import java.util.ArrayList;
import java.util.List;

public class Piece {
	private int _qty;
	private Player _player;
	private int _carryingCapacity;

	Piece(Player p, int qty, int carryingCapacity) {
		_qty = qty;
		_player = p;
		_carryingCapacity = carryingCapacity;
	}

	public Piece copy() {
		return new Piece(_player, _qty, _carryingCapacity);
	}

	public int getQuantity() {
		return _qty;
	}

	public Player getPlayer() {
		return _player;
	}

	public void setQty(int v) {
		_qty = v;
	}

	public void setPlayer(Player p) {
		_player = p;
	}

	public void increment() {
		_qty++;
	}

	public int getCarryingCapacity() {
		return _carryingCapacity;
	}
	public String toString() {
		return this.getPlayer().getID() + ";" + this.getQuantity() + ";"+this.getCarryingCapacity();
	}

}