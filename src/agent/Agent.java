package agent;

import game.Board;
import game.Coordinate;
import game.Move;
import game.Pawn;
import game.Piece;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pgnparse.Color;
import pgnparse.MalformedMoveException;
import pgnparse.PGNGame;
import pgnparse.PGNMove;
import pgnparse.PGNParseException;
import pgnparse.PGNParser;
import util.DatabaseParser;

public class Agent {

	private int color;
	private int currentMove = 0;
	private final static int MAX_DEPTH = 3;
	private Move bestMove;
	private ArrayList<ArrayList<Move>> allOpeningMoves;
	
	public Agent(int color){
		bestMove = new Move(0,1,2,0);
		this.color = color;
		if(color == Piece.BLACK){
			currentMove = 1;
		}
		DatabaseParser parser = new DatabaseParser();
		allOpeningMoves = parser.parseOpenings();
	}
	
	
	public Move move(Board board, Move lastMove){
		getOpening(lastMove);
		if(allOpeningMoves.size() != 0){
			System.out.println("Using opening database, current number of games with this opening: " + allOpeningMoves.size());
			Move nextMove = getNextOpeningMove(allOpeningMoves);
			board.isLegalMove(nextMove.fromy,nextMove.fromx,nextMove.toy,nextMove.tox, color);
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
		return moves.get(0).get(currentMove);
	}

	public void getOpening(Move lastMove) {
		for(int i = 0; i<allOpeningMoves.size(); ++i){
			ArrayList<Move> game = allOpeningMoves.get(i);
			Move move = game.get(currentMove-1);
			if(!move.equals(lastMove)){
				allOpeningMoves.remove(i);
				--i;
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
		if (currentPlayer == color) {
			for (Move move : legalMoves) {
				Board newBoard = board.makeMove(move.fromy,move.fromx,move.toy,move.tox);
				alpha = Math.max(
						alpha,
						alfaBeta(newBoard, depth + 1, maxDepth, alpha, beta,
								changePlayer(currentPlayer)));

				if (beta <= alpha) {
					bestMove = move;
					break;
				}
				bestMove = move;

			}
			return alpha;
		}

		for (Move move : legalMoves) {
			Board newBoard = board.makeMove(move.fromy,move.fromx,move.toy,move.tox);
			beta = Math.min(
					beta,
					alfaBeta(newBoard, depth + 1, maxDepth, alpha, beta,
							changePlayer(currentPlayer)));
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
				
				value = chessBoard[i][j].getValue();
				
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
