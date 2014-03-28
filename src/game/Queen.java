package game;

import java.util.ArrayList;


public class Queen extends Piece{

	public Queen(int color, int y, int x) {
		super(color,y,x);
		
	}

	@Override
	public boolean isLegalMove(int toy, int tox) {

		if(x == tox || y == toy || Math.abs((x-tox)/(y-toy)) == 1){
			return true;
		}
		
		return false;
	}
	@Override
	public ArrayList<Coordinate> getPath(int toy, int tox) {
		ArrayList<Coordinate> l = new ArrayList<Coordinate>();
		if(toy > y && x == tox){
			for(int i = y+1; i < toy; ++i)
				l.add(new Coordinate(i,x));
		}else if(toy < y && x == tox){
			for(int i = toy+1; i < y; ++i)
				l.add(new Coordinate(i,x));
		}else if(tox > x && y== toy){
			for(int i = x+1; i < tox; ++i)
				l.add(new Coordinate(y,i));
		}else if(tox < x && y== toy){
			for(int i = tox+1; i < x; ++i)
				l.add(new Coordinate(y,i));
		}else if(toy > y && tox > x){
			for(int i = 1; i < toy-y; ++i)
				l.add(new Coordinate(y+i,x+i));
		}else if(toy > y && tox < x){
			for(int i = 1; i < toy-y; ++i)
				l.add(new Coordinate(y+i,x-i));
		}else if(toy < y && tox > x){
			for(int i = 1; i < y-toy; ++i)
				l.add(new Coordinate(y-i,x+i));
		}else if(toy < y && tox < x){
			for(int i = 1; i < y-toy; ++i)
				l.add(new Coordinate(y-i,x-i));
		}
		return l;
	}

	@Override
	public boolean isLegalCapturingMove(int toy, int tox) {
		return isLegalMove(toy,tox);
	}
	
}
