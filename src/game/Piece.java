package game;

import java.util.ArrayList;

public abstract class Piece {
	
	public int color, y, x;
	public final static int BLACK = 0;
	public final static int WHITE = 1;
	
	
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

	public abstract ArrayList<Coordinate> getPath(int toy, int tox);
	
}
