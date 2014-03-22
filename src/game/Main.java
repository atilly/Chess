package game;

import gui.ChessGUI;

public class Main {

	public static void main(String[] args) {
		Board board = new Board();
		board.newgame();
		ChessGUI gui = new ChessGUI(board);
/*
		while(true){
			Coordinate from = gui.getMove();
			Coordinate to = gui.getMove();
			board.move(from.y, from.x, to.y, to.x);
			
			
		}
	*/	
		
	}

}
