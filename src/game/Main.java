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
		
		if(args.length > 0 && args[0].equals("learn")){
			Random rand = new Random();
			Pawn.value += rand.nextInt(Pawn.value/2)-(Pawn.value/4);
			Bishop.value += rand.nextInt(Bishop.value/2)-(Bishop.value/4);
			Knight.value += rand.nextInt(Knight.value/2)-(Knight.value/4);
			Rook.value += rand.nextInt(Rook.value/2)-(Rook.value/4);
			Queen.value += rand.nextInt(Queen.value/2)-(Queen.value/4);
			King.value += rand.nextInt(King.value/2)-(King.value/4);
		}else{
			try {
				BufferedReader reader = new BufferedReader(new FileReader("results.txt"));
				int[] values = new int[6];
				int count = 0;
				while(reader.ready()){
					String line = reader.readLine();

					if(line.equals("win") || line.equals("draw")){
						count++;
						for(int i=0; i<6; i++){
							line = reader.readLine();
							String[] split = line.split("=");
							int value = Integer.parseInt(split[1]);
							values[i] += value;
						}
					}
				}
				for(int i=0; i<6; i++){
					values[i] = values[i]/count;
				}
				Pawn.value = values[0];
				Bishop.value = values[1];
				Knight.value = values[2];
				Rook.value = values[3];
				Queen.value = values[4];
				King.value = values[5];
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		Board board = new Board();
		board.newgame();

		if(args.length > 1 && args[1].equals("gnuchess")){
			InputStream is;
			OutputStream os;
			
			try {
				ProcessBuilder pb = new ProcessBuilder("/h/d3/s/dt09at1/edan50/gnuchess-6.1.1/src/gnuchess");
				Process p = pb.start();
				is = p.getInputStream();
				os = p.getOutputStream();
				PrintWriter writer = new PrintWriter(os);
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				Agent agent = new Agent(Piece.WHITE);
				ChessGUI gui = new ChessGUI(board, false);
				Move nextMove = null;
				int currentMove = 1;

				while(true){
					Move move = agent.move(board, nextMove);
					board = board.makeMove(move.fromy, move.fromx, move.toy, move.tox);
					gui.update(board);
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
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(args.length > 1 && args[1].equals("playself")){
			ChessGUI gui = new ChessGUI(board, false);
			Agent white = new Agent(Piece.WHITE);
			Agent black = new Agent(Piece.BLACK);
			Move lastMove = null;
			Board[] lastStates = new Board[8];
			int i = 0;
			boolean draw = false;
			while(!draw){
				Move move = white.move(board, lastMove);
				board = board.makeMove(move.fromy, move.fromx, move.toy, move.tox);
				lastStates[i] = board;
				i++;
				gui.update(board);
				lastMove = move;
				move = black.move(board, lastMove);
				board = board.makeMove(move.fromy, move.fromx, move.toy, move.tox);
				lastStates[i] = board;
				i++;
				gui.update(board);
				lastMove = move;
				if(i >= 8){
					i = 0;
					
					for(int j = 4; j<8; j++){
						if(lastStates[j].equals(lastStates[j-4])){
							draw = true;
						}
					}
					
				}

				if(draw){
					System.out.println("draw");
					ResultWriter.writeResult("draw");
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}else{
			ChessGUI gui = new ChessGUI(board, true);
		}
		
	}

}