package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import pgnparse.MalformedMoveException;
import pgnparse.PGNGame;
import pgnparse.PGNMove;
import pgnparse.PGNParser;
import util.DatabaseParser;
import util.ResultWriter;
import agent.Agent;
import gui.ChessGUI;

public class Main {

	public static void main(String[] args) {
		
		boolean learn = false;
		if(args.length > 0 && args[0].equals("learn")){
			learn = true;
		}
		
		Board board = new Board();
		board.newgame();

		if(args.length > 1 && args[1].equals("gnuchess")){
			InputStream is;
			OutputStream os;
			
			try {
				ProcessBuilder pb = new ProcessBuilder(args[2]);
				Process p = pb.start();
				is = p.getInputStream();
				os = p.getOutputStream();
				PrintWriter writer = new PrintWriter(os);
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				Agent agent = new Agent(Piece.WHITE, learn);
				ChessGUI gui = new ChessGUI(board, false, learn);
				Move nextMove = null;
				int currentMove = 1;
				String result = null;
				while(result == null){
					Move move = agent.move(board, nextMove);
					board = board.makeMove(move.fromy, move.fromx, move.toy, move.tox);
					gui.update(board);
					
					if(board.getLegalMoves(Piece.BLACK).size() == 0){
						result = "win";
					}
					
					move.fromy = 8-move.fromy;
					move.toy = 8-move.toy;
					char c = (char) (move.fromx+'a');
					StringBuilder sb = new StringBuilder();
					sb.append(c);
					sb.append(move.fromy);
					c = (char)(move.tox+'a');
					sb.append(c);
					sb.append(move.toy);
					String s = sb.toString();
					writer.write(s+"\n");
					writer.flush();
			
					String line = br.readLine();
					while(!line.startsWith("My move")){
						line = br.readLine();
						
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					writer.write("pgnsave game\n");
					writer.flush();
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					PGNMove pgnmove = null;
					BufferedReader filereader = new BufferedReader(new FileReader("game"));

					StringBuilder gamestring = new StringBuilder();
					while(filereader.ready()){
						gamestring.append(filereader.readLine());
						gamestring.append("\n");
					}
					gamestring.append("1-0\n");
					try {
						List<PGNGame> list = PGNParser.parse(gamestring.toString());
						pgnmove = list.get(0).getMove(currentMove);
						currentMove += 2;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					nextMove = DatabaseParser.parseMove(pgnmove);
					board = board.makeMove(nextMove.fromy, nextMove.fromx, nextMove.toy, nextMove.tox);
					gui.update(board);
					File file = new File("game");
					file.delete();
					
					if(board.getLegalMoves(Piece.WHITE).size() == 0){
						result = "loss";
					}
					
				}
				System.out.println(result);
				ResultWriter.writeResult(result, agent.values);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(args.length > 1 && args[1].equals("playself")){
			ChessGUI gui = new ChessGUI(board, false, learn);
			Agent white = new Agent(Piece.WHITE, learn);
			Agent black = new Agent(Piece.BLACK, learn);
			Move lastMove = null;
			Board[] lastStates = new Board[8];
			int i = 0;
			String result = null;
			while(result == null){
				Move move = white.move(board, lastMove);
				board = board.makeMove(move.fromy, move.fromx, move.toy, move.tox);
				if(board.getLegalMoves(Piece.BLACK).size() == 0){
					result = "win";
				}
				lastStates[i] = board;
				i++;
				gui.update(board);
				lastMove = move;
				move = black.move(board, lastMove);
				board = board.makeMove(move.fromy, move.fromx, move.toy, move.tox);
				if(board.getLegalMoves(Piece.WHITE).size() == 0){
					result = "lose";
				}
				lastStates[i] = board;
				i++;
				gui.update(board);
				lastMove = move;
				if(i >= 8){
					i = 0;
					
					for(int j = 4; j<8; j++){
						if(lastStates[j].equals(lastStates[j-4])){
							result = "draw";
						}
					}
					
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			System.out.println(result);
			if(result.equals("win")){
				ResultWriter.writeResult("win", white.values);
				ResultWriter.writeResult("loss", black.values);
			}else if(result.equals("loss")){
				ResultWriter.writeResult("win", black.values);
				ResultWriter.writeResult("loss", white.values);
			}else{
				ResultWriter.writeResult("draw", white.values);
				ResultWriter.writeResult("draw", black.values);
			}
		}else{
			ChessGUI gui = new ChessGUI(board, true, learn);
		}
		
	}

}