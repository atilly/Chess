package game;

import gui.ChessGUI;

public class Main {

	public static void main(String[] args) {
		
		Board board = new Board();
		board.newgame();
		ChessGUI gui = new ChessGUI(board);

	}

}
