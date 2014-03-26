package game;

import java.util.ArrayList;

public class Pawn extends Piece{

	public Pawn(int color, int y, int x) {
		super(color,y,x);
		
	}


	@Override
	public boolean isLegalMove(int toy, int tox) {
		if(color == Piece.WHITE){
			if((toy == y-2 && tox == x && y == 6)){
				return true;
			}
			if(toy == y -1 && (tox == x-1 || tox == x+1 || tox == x)){
				return true;
			}
		}
		
		if(color == Piece.BLACK){

			if(toy == y+2 && tox == x && y == 1){
				return true;
			}
			if(toy == y +1 && (tox == x-1 || tox == x+1 || tox == x)){
				return true;
			}
		}
		
		return false;
	}


	@Override
	public ArrayList<Coordinate> getPath(int toy, int tox) {
		ArrayList<Coordinate> l = new ArrayList<Coordinate>();
		if(Math.abs(y-toy) == 2)
			l.add(new Coordinate(y+(toy-y)/2,x));
		return l;
	}

}
