package agent;

import game.Board;
import game.Move;
import game.Piece;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Agent {

	private int color;
	private static final String db = "IB1217.pgn";
	private static final int OPENINGMOVES = 3;;
	
	public Agent(int color){
		this.color = color;
	}
	
	public void parseDatabase(){
		
		BufferedReader reader = null;
		
		try {
			
			reader = new BufferedReader(new FileReader(db));
			
			int j = 0;
			while(reader.ready() && j<100){
				j++;
				String line = reader.readLine();
				if(line.startsWith("1")){
					String[] split = line.split(" ");
					
					for(int i = 0; i< OPENINGMOVES; i++){
						String s = split[i];
						if(s.charAt(1) == '.'){
							s = s.substring(2);
						}
						if(s.length() == 2){

							char c = s.charAt(0);
							int tox = c - 'a';

							s = s.substring(1);
							int toy = Integer.parseInt(s)-1;
							
							int fromy = 0; //todo
							int fromx = 0; //todo
							
							Move move = new Move(fromy, fromx, toy, tox);

						}
						//todo
					}
				}		
			}

			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
