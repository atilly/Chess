package util;

import game.*;

import java.io.*;

public class ResultWriter {

	public static void writeResult(String result){
		
		File file = new File("results.txt");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(result);
			bw.newLine();
			bw.write("pawn="+Pawn.value);
			bw.newLine();
			bw.write("bishop="+Bishop.value);
			bw.newLine();
			bw.write("knight="+Knight.value);
			bw.newLine();
			bw.write("rook="+Rook.value);
			bw.newLine();
			bw.write("queen="+Queen.value);
			bw.newLine();
			bw.write("king="+King.value);
			bw.newLine();
			bw.flush();
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
