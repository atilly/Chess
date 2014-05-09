package game;

import java.util.ArrayList;

public class Knight extends Piece{

	public static int value = 30;
	
	public Knight(int color, int y, int x) {
		super(color,y,x);
		
	}

	public int getValue(){
		return value;
	}
	
	@Override
	public boolean isLegalMove(int toy, int tox) {
		int diffy = Math.abs(toy - y);
		int diffx = Math.abs(tox - x);
		
		if((diffy == 1 && diffx == 2)  || (diffy == 2 && diffx == 1)){
			return true;
		}
		return false;
	}
	@Override
	public ArrayList<Coordinate> getPath(int toy, int tox) {
		ArrayList<Coordinate> l = new ArrayList<Coordinate>();
		return l;
	}

	@Override
	public boolean isLegalCapturingMove(int toy, int tox) {
		return isLegalMove(toy,tox);
	}
	
	public Piece clone(){
		return new Knight(color,y,x);
	}
	
}
