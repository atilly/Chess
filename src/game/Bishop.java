package game;

import java.util.ArrayList;

public class Bishop extends Piece{

	public static int value = 30;
	
	public Bishop(int color, int y, int x) {
		super(color,y,x);
		
	}
	
	public int getValue(){
		return value;
	}

	@Override
	public boolean isLegalMove(int toy, int tox) {
		if(x == tox && y == toy)
			return false;
		if(Math.abs(x-tox) == Math.abs((y-toy))){
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Coordinate> getPath(int toy, int tox) {
		ArrayList<Coordinate> l = new ArrayList<Coordinate>();
		if(toy > y && tox > x){
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
	
	public Piece clone(){
		return new Bishop(color,y,x);
	}
	
}