package agent;

import game.Board;
import game.Piece;

public class Agent {

	private int color;
	
	public Agent(int color){
		this.color = color;
	}
	
	
	public void move(Board board){
		Piece[][] chessBoard = board.getChessBoard();
		for(int i = 0; i<Board.SIZE; i++){
			for(int j = 0; j<Board.SIZE; j++){
				for(int k = 0; k < Board.SIZE; k++){
					for(int l = 0; l< Board.SIZE; l++){
						if(board.move(i,j,k,l, color)){
							return;
						}
					}
				}
			}
		}
	}
}
