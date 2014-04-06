package game;

import java.util.ArrayList;

public class Empty extends Piece{

	public Empty(int y, int x) {
		super(y, x);

	}

	@Override
	public boolean isLegalMove(int toy, int tox) {
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
		return new Empty(y,x);
	}
	
	public int getValue(){
		return 0;
	}

}
