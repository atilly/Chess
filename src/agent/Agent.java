package agent;

import game.Board;
import game.Move;
import game.Pawn;
import game.Piece;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pgnparse.Color;
import pgnparse.MalformedMoveException;
import pgnparse.PGNGame;
import pgnparse.PGNMove;
import pgnparse.PGNParseException;
import pgnparse.PGNParser;

public class Agent {

	private int color;
	private int currentMove = 0;
	private static final String db = "IB1217.pgn";
	private static final int OPENINGMOVES = 10;;
	private ArrayList<ArrayList<Move>> allMoves;
	
	public Agent(int color){
		this.color = color;
		if(color == Piece.BLACK){
			currentMove = 1;
		}
	}
	
	public void parseDatabase(){
		
		allMoves = new ArrayList<ArrayList<Move>>();
		List<PGNGame> games = null;
		
		BufferedReader reader = null;
		String pgn = "";
		
		try {
			
			reader = new BufferedReader(new FileReader(db));
			int j = 0;
			while(reader.ready() && j<1035){
				j++;
				String line = reader.readLine();
				pgn +=line+"\n";

			}

			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			games = PGNParser.parse(pgn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(PGNGame game: games){
			Iterator<PGNMove> iterator = game.getMovesIterator();
			ArrayList<Move> moves = new ArrayList<Move>();
			int i = 0;
			while(iterator.hasNext() && i<OPENINGMOVES){
				i++;
				PGNMove pgnmove = iterator.next();

				int fromy = 0;
				int fromx = 0;
				int toy = 0;
				int tox = 0;
				if(pgnmove.isKingSideCastle()){
					fromx = 4;
					tox = 6;
					Color color = pgnmove.getColor();
					if(color == Color.white){	
						fromy = 7;
						toy = 7;
					}else{	
						fromy = 0;
						toy = 0;
					}
				}else if(pgnmove.isQueenSideCastle()){
					fromx = 4;
					tox = 2;
					Color color = pgnmove.getColor();
					if(color == Color.white){	
						fromy = 7;
						toy = 7;
					}else{
						fromy = 0;
						toy = 0;
					}

				}else{
					String s = pgnmove.toString();

					String[] split = s.split(" ");

					char c = split[0].charAt(0);
					fromx = c - 'a';
					
					split[0] = split[0].substring(1);
					fromy = Integer.parseInt(split[0])-1;
					
					c = split[1].charAt(0);
					tox = c - 'a';
					
					split[1] = split[1].substring(1);
					toy = Integer.parseInt(split[1])-1;
					fromy = 7-fromy;
					toy = 7-toy;
					
				}

				Move move = new Move(fromy, fromx, toy, tox);
				moves.add(move);
			}
			allMoves.add(moves);
		}

	}
	
	
	public void move(Board board, Move move){
		getOpening(move);
		if(allMoves.size() != 0){
			Move nextMove = getNextOpeningMove(allMoves);
			board.move(nextMove.fromy,nextMove.fromx,nextMove.toy,nextMove.tox, color);
			currentMove++;
			getOpening(nextMove);
			currentMove++;
			return;
		}
		
		for(int i = 0; i<Board.SIZE; i++){
			for(int j = 0; j<Board.SIZE; j++){
				for(int k = 0; k < Board.SIZE; k++){
					for(int l = 0; l< Board.SIZE; l++){
						if(board.move(i,j,k,l, color)){
							move = new Move(i,j,k,l);
							return;
						}
					}
				}
			}
		}

	}

	private Move getNextOpeningMove(ArrayList<ArrayList<Move>> moves) {

		return moves.get(0).get(currentMove);
	}

	public void getOpening(Move move) {
		for(int i = 0; i<allMoves.size(); ++i){
			ArrayList<Move> game = allMoves.get(i);
			Move m = game.get(currentMove-1);
			if(!m.equals(move)){
				allMoves.remove(i);
				--i;
			}
			
		}

	}
}
