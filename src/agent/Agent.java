package agent;

import game.Bishop;
import game.Board;
import game.Coordinate;
import game.King;
import game.Knight;
import game.Move;
import game.Pawn;
import game.Piece;
import game.Queen;
import game.Rook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pgnparse.Color;
import pgnparse.MalformedMoveException;
import pgnparse.PGNGame;
import pgnparse.PGNMove;
import pgnparse.PGNParseException;
import pgnparse.PGNParser;
import util.DatabaseParser;

public class Agent {

	private final Move nullMove = new Move(-1,-1,-1,-1);
	private int color;
	private int currentMove = 0;
	private final static int MAX_DEPTH = 4;
	private Move bestMove;
	private ArrayList<ArrayList<Move>> allOpeningMoves;
	public HashMap<String, Integer> values;
	
	public Agent(int color, boolean learn){
		bestMove = nullMove;
		this.color = color;
		if(color == Piece.BLACK){
			currentMove = 1;
		}
		DatabaseParser parser = new DatabaseParser();
		allOpeningMoves = parser.parseOpenings();
		values = new HashMap<String, Integer>();

		if(learn){
			values.put("pawn",10);
			values.put("bishop",30);
			values.put("knight",30);
			values.put("rook",50);
			values.put("queen",90);
			values.put("king",1000);

			Random rand = new Random();
			for(String s: values.keySet()){
				int value = values.get(s);
				value += rand.nextInt(value/2 - value/4);
				values.put(s, value);
			}
			values.put("empty",0);
		}else{
			try {
				BufferedReader reader = new BufferedReader(new FileReader("results.txt"));
				int[] vals = new int[6];
				int count = 0;
				while(reader.ready()){
					String line = reader.readLine();
					if(line.equals("win") || line.equals("draw")){
						count++;
						for(int i=0; i<6; i++){
							line = reader.readLine();
							String[] split = line.split("=");
							int value = Integer.parseInt(split[1]);
							vals[i] += value;
						}
					}
				}
				for(int i=0; i<6; i++){
					vals[i] = vals[i]/count;
				}

				values.put("pawn",vals[0]);
				values.put("bishop",vals[1]);
				values.put("knight",vals[2]);
				values.put("rook",vals[3]);
				values.put("queen",vals[4]);
				values.put("king",vals[5]);
				values.put("empty",0);
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Move move(Board board, Move lastMove){
		getOpening(lastMove);
		if(allOpeningMoves.size() != 0 && currentMove < 9){
			System.out.println("Using opening database, current number of games with this opening: " + allOpeningMoves.size());
			Move nextMove = getNextOpeningMove(allOpeningMoves);
//			board.isLegalMove(nextMove.fromy,nextMove.fromx,nextMove.toy,nextMove.tox, color);
			currentMove++;
			getOpening(nextMove);
			currentMove++;
			return nextMove;
		}else{
			System.out.println("No longer using opening database.");
		}

		return calculateBestMove(board);

	}

	private Move getNextOpeningMove(ArrayList<ArrayList<Move>> moves) {
		Random rand = new Random();
		int index = rand.nextInt(moves.size());
		return moves.get(index).get(currentMove);
	}

	public void getOpening(Move lastMove) {
		if(lastMove != null){
			for(int i = 0; i<allOpeningMoves.size(); ++i){
				ArrayList<Move> game = allOpeningMoves.get(i);
				Move move = game.get(currentMove-1);
				if(!move.equals(lastMove)){
					allOpeningMoves.remove(i);
					--i;
				}
			}
		}

	}
	
	private Move calculateBestMove(Board board) {
		alfaBeta(board, 0, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE,
				color);
		return bestMove;
	}
	
	private int alfaBeta(Board board, int depth, int maxDepth, int alpha, int beta, int currentPlayer) {
		if (depth == maxDepth) {
			return evaluate(currentPlayer, board);
		}

		ArrayList<Move> legalMoves = board.getLegalMoves(currentPlayer);
		if (legalMoves.size() == 0) {
			return evaluate(currentPlayer, board);
		}

		if (bestMove.equals(nullMove)) {
			bestMove = legalMoves.get(0);
		}

		//If maximizing player:
		if (currentPlayer == color) {
			for (Move move : legalMoves) {
				Board newBoard = board.makeMove(move.fromy,move.fromx,move.toy,move.tox);
				
				int score = alfaBeta(newBoard, depth + 1, maxDepth, alpha,
						beta, changePlayer(currentPlayer));
				
				if (score > alpha) {
					alpha = score;
					if (depth == 0) {
						bestMove = move;
					}
				}
				if (beta <= alpha) {
					break;
				}
			}
			return alpha;
		}

		//If minimizing player:
		for (Move move : legalMoves) {
			Board newBoard = board.makeMove(move.fromy, move.fromx, move.toy, move.tox);

			int score = alfaBeta(newBoard, depth + 1, maxDepth, alpha, beta,
					changePlayer(currentPlayer));
			if (score < beta) {
				beta = score;
			}
			if (beta <= alpha) {
				break;
			}
		}
		return beta;
	}
	
	public int evaluate(int color, Board board) {
		int sum = 0;
		Piece[][] chessBoard = board.getChessBoard();

		int value = 0;
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				
				String type = chessBoard[i][j].getType();
				value = values.get(type);
				
				if (chessBoard[i][j].color == color) {
					sum += value;
				} else {
					sum -= value;
				}
			}
		}
		return sum;
	}
	
	private int changePlayer(int player) {
		if (player == Piece.WHITE) {
			return Piece.BLACK;
		}
		return Piece.WHITE;
	}
	
}
