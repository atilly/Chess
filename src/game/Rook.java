package game;

import java.util.ArrayList;

public class Rook extends Piece{

	public Rook(int color, int y, int x) {
		super(color,y,x);
		
	}

	public void move(int toy, int tox){
		canCastle = false;
		this.y = toy;
		this.x = tox;
		
	}
	
	@Override
	public boolean isLegalMove(int toy, int tox) {
		if(x == tox || y == toy){
			return true;
		}
		return false;
	}
	public ArrayList<Coordinate> getPath(int toy, int tox) {
		ArrayList<Coordinate> l = new ArrayList<Coordinate>();
		if(toy > y){
			for(int i = y+1; i < toy; ++i)
				l.add(new Coordinate(i,x));
		}else if(toy < y){
			for(int i = toy+1; i < y; ++i)
				l.add(new Coordinate(i,x));
		}else if(tox > x){
			for(int i = x+1; i < tox; ++i)
				l.add(new Coordinate(y,i));
		}else if(tox < x){
			for(int i = tox+1; i < x; ++i)
				l.add(new Coordinate(y,i));
		}
		return l;
	}

	@Override
	public boolean isLegalCapturingMove(int toy, int tox) {
		return isLegalMove(toy,tox);
	}
	
}
