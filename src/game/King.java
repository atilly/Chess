package game;

import java.util.ArrayList;

public class King extends Piece{

	public King(int color, int y, int x) {
		super(color,y,x);
		
	}

	@Override
	public boolean isLegalMove(int toy, int tox) {

		int diffy = Math.abs(toy - y);
		int diffx = Math.abs(tox - x);
		if(diffy <= 1 && diffx <=1){
			return true;
		}
		
		return false;
	}
	@Override
	public ArrayList<Coordinate> getPath(int toy, int tox) {
		ArrayList<Coordinate> l = new ArrayList<Coordinate>();
		return l;
	}
	
}
