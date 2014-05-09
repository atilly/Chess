package game;

import java.util.ArrayList;

public abstract class Piece {
	
	public int color, y, x;
	public final static int BLACK = 0;
	public final static int WHITE = 1;
	public boolean canCastle = true;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (canCastle ? 1231 : 1237);
		result = prime * result + color;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (canCastle != other.canCastle)
			return false;
		if (color != other.color)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public Piece(int color, int y, int x) {
		this.color = color;
		this.y = y;
		this.x = x;
	}

	public Piece(int y, int x) {
		this.color = -1;
		this.y = y;
		this.x = x;
	}
	
	public void move(int toy, int tox){
		this.y = toy;
		this.x = tox;
		
	}
	
	public abstract boolean isLegalMove(int toy, int tox);
	public abstract boolean isLegalCapturingMove(int toy, int tox);

	public String toString(){
		if(this instanceof Empty){
			return "empty";
		}
		
		String color, type = null;
		if(this.color == BLACK){
			color = "black";
		}else{
			color = "white";
		}
			
		if(this instanceof  Pawn){
			type = "pawn";
		}else if(this instanceof Rook){
			type = "rook";
		}else if(this instanceof Knight){
			type = "knight";
		}else if(this instanceof Bishop){
			type = "bishop";
		}else if(this instanceof Queen){
			type = "queen";
		}else if(this instanceof King){
			type = "king";
		}

		return color+type;
	}

	public abstract int getValue();
	
	public abstract ArrayList<Coordinate> getPath(int toy, int tox);
	public abstract Piece clone();

	
}
