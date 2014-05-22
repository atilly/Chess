package util;

import game.Move;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pgnparse.Color;
import pgnparse.PGNGame;
import pgnparse.PGNMove;
import pgnparse.PGNParser;


public class DatabaseParser {

	private static final String db = "IB1217.pgn";
	private static final int OPENINGMOVES = 10;
	
	public ArrayList<ArrayList<Move>> parseOpenings(){
		
		ArrayList<ArrayList<Move>> allOpeningMoves = new ArrayList<ArrayList<Move>>();
		List<PGNGame> games = null;
		
		BufferedReader reader = null;
		String pgn = "";
		
		try {
			
			reader = new BufferedReader(new FileReader(db));
			int j = 0;
			StringBuilder sb = new StringBuilder();
			//50007
			//120
			while(reader.ready() && j<50007){
				j++;
				String line = reader.readLine();
				sb.append(line+"\n");

			}
			pgn = sb.toString();
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
				Move move = parseMove(pgnmove);
				moves.add(move);
			}
			allOpeningMoves.add(moves);
		}
		return allOpeningMoves;
	}
	
	public static Move parseMove(PGNMove pgnmove){
		
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
		return new Move(fromy, fromx, toy, tox);
	}
}
