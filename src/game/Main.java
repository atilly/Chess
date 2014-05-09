package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Random;
import java.util.Scanner;

import pgnparse.MalformedMoveException;
import pgnparse.PGNMove;
import util.DatabaseParser;
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

					if(line.equals("win")){
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
			Runtime runtime = Runtime.getRuntime();
			InputStream is;
			OutputStream os;
			
			try {
				ProcessBuilder pb = new ProcessBuilder("/h/d3/s/dt09at1/edan50/gnuchess-6.1.1/src/gnuchess");
				Process p = pb.start();
				is = p.getInputStream();
				Scanner scan = new Scanner(is);
				os = p.getOutputStream();
				PrintWriter writer = new PrintWriter(os);
//				InputStreamReader reader = new InputStreamReader(is);
//				BufferedReader br = new BufferedReader(reader);
				Agent agent = new Agent(Piece.WHITE);
				ChessGUI gui = new ChessGUI(board, false);
				Move nextMove = null;

				while(true){
					Move move = agent.move(board, nextMove);
					board = board.makeMove(move.fromy, move.fromx, move.toy, move.tox);
					gui.update(board);
					System.out.println(move);
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
					System.out.println("about to write " + s);
					writer.write(s);
					writer.flush();
					System.out.println("about to read");
			
					while(scan.hasNext()){
						String line = scan.next();
						System.out.println(line);
						scan = new Scanner(System.in);
						
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					
					/*
					String line = br.readLine();
					while(!line.startsWith("My move")){
						System.out.println(line);
						line = br.readLine();
						is = p.getInputStream();
						reader = new InputStreamReader(is);
						br = new BufferedReader(reader);
						p.
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					*/
/*
					c = gnuchars[res-1];
					int i = 1;
					while(c != 's'){
						c = gnuchars[res-i];
								i++;
					}

					s = "";
					i+=4;
					while(gnuchars[res-i]!='W'){
						s+=gnuchars[res-i];
						i--;
					}
					*/
					PGNMove pgnmove = null;
					System.out.println(s);
					try {
						pgnmove = new PGNMove(s);
					} catch (MalformedMoveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(pgnmove);
					nextMove = DatabaseParser.parseMove(pgnmove);
					System.out.println(nextMove);
					board = board.makeMove(nextMove.fromy, nextMove.fromx, nextMove.toy, nextMove.tox);
					gui.update(board);
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{

			ChessGUI gui = new ChessGUI(board, true);
		}
		
	}

}