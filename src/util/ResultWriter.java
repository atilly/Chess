package util;

import game.*;

import java.io.*;
import java.util.HashMap;

public class ResultWriter {

	public static void writeResult(String result, HashMap<String, Integer> values){
		
		File file = new File("results.txt");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(result);
			bw.newLine();
			bw.write("pawn="+values.get("pawn"));
			bw.newLine();
			bw.write("bishop="+values.get("bishop"));
			bw.newLine();
			bw.write("knight="+values.get("knight"));
			bw.newLine();
			bw.write("rook="+values.get("rook"));
			bw.newLine();
			bw.write("queen="+values.get("queen"));
			bw.newLine();
			bw.write("king="+values.get("king"));
			bw.newLine();
			bw.flush();
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
