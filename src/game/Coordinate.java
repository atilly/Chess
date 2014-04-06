package game;

public class Coordinate {

	public int y,x;
	
	public Coordinate(int y, int x){
		this.y = y;
		this.x = x;
	}
	
	public String toString(){
		return y + " " + x;
	}
	
}
