package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import gui.ChessGUI;

public class Main {

	public static void main(String[] args) {
		
		if(args.length > 0){
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
		ChessGUI gui = new ChessGUI(board);

	}

}