package game;

public class Piece {
	
	public int type, color;
	public final static int BLACK = 0;
	public final static int WHITE = 1;
	public final static int EMPTY = 0;
	public final static int PAWN = 1;
	public final static int ROOK = 2;
	public final static int KNIGHT = 3;
	public final static int BISHOP = 4;
	public final static int QUEEN = 5;
	public final static int KING = 6;
	
	
	public Piece(int type, int color) {
		this.type = type;
		this.color = color;
	}

	public Piece(int type) {
		this.type = type;
		this.color = -1;
	}

	public String toString(){
		if(type == EMPTY){
			return "empty";
		}
		
		String color, type = null;
		if(this.color == BLACK){
			color = "black";
		}else{
			color = "white";
		}
			
		if(this.type == PAWN){
			type = "pawn";
		}else if(this.type == ROOK){
			type = "rook";
		}else if(this.type == KNIGHT){
			type = "knight";
		}else if(this.type == BISHOP){
			type = "bishop";
		}else if(this.type == QUEEN){
			type = "queen";
		}else if(this.type == KING){
			type = "king";
		}

		return color+type;
	}
	
}
