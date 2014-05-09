package game;

import java.util.ArrayList;

public class King extends Piece{

	public static int value = 1000;
	
	public King(int color, int y, int x) {
		super(color,y,x);
		
	}
	
	public int getValue(){
		return value;
	}
	
	public void move(int toy, int tox){
		canCastle = false;
		this.y = toy;
		this.x = tox;
		
	}
	
	@Override
	public boolean isLegalMove(int toy, int tox) {

		int diffy = Math.abs(toy - y);
		int diffx = Math.abs(tox - x);
		if(diffy <= 1 && diffx <=1){
			return true;
		}
		
		if(canCastle && diffx == 2 && y == toy){
			return true;
		}
		
		return false;
	}
	@Override
	public ArrayList<Coordinate> getPath(int toy, int tox) {
		ArrayList<Coordinate> l = new ArrayList<Coordinate>();
		for(int i = x+1; i<tox; i++){
			l.add(new Coordinate(y,i));
		}
		for(int i = x-1; i>tox; i--){
			l.add(new Coordinate(y,i));
		}
		return l;
		
	}

	@Override
	public boolean isLegalCapturingMove(int toy, int tox) {
		return isLegalMove(toy,tox);
	}
	
	public Piece clone(){
		King king = new King(color,y,x);
		king.canCastle = canCastle;
		return king;
	}
	
}
