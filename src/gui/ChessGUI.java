package gui;

import game.Board;
import game.Coordinate;
import game.Piece;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import agent.Agent;

public class ChessGUI {

	private Board board;
	private ChessButton[][] buttons;
	private CommandListener cmd;
	private HashMap<String, BufferedImage> images;
	
	public ChessGUI(Board board){

		this.board = board;
		buttons = new ChessButton[8][8];
		Agent agent = new Agent(Piece.BLACK);
		cmd = new CommandListener(board, this, agent, Piece.WHITE);
		images = new HashMap<String, BufferedImage>();
		
		JFrame frame = new JFrame("Chess");
		GridLayout grid = new GridLayout(8,8);
		frame.setLayout(grid);
		initImages();
		
		boolean dark = true;
		for(int i = 0; i<8; i++){
			for(int j = 0; j<8; j++){
				
				Piece[][] chessBoard = board.getChessBoard();
				String piece = chessBoard[i][j].toString();
				
				BufferedImage img = getPieceImage(piece, dark);
				dark = !dark;

				ImageIcon icon = new ImageIcon(img);
				ChessButton button = new ChessButton(icon,i,j);
				buttons[i][j] = button;
				button.addActionListener(cmd);
				frame.add(button);
			}
			dark = !dark;
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setSize(300, 300);
		frame.setVisible(true);

	}
	
	public BufferedImage getPieceImage(String piece, boolean dark){
		if(dark){
			piece = piece+"dark.png";			
		}else{
			piece = piece+"light.png";	
		}
		return images.get(piece);
	}
	
	public void initImages(){
		File imageDirectory = new File("images");
		File[] files = imageDirectory.listFiles();
		for(File f: files){
			BufferedImage img = null;
			try {
				img = ImageIO.read(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String imageString = f.toString().substring(7);
			images.put(imageString, img);
		}
	}

	public void update() {
		boolean dark = true;
		for(int i = 0; i<8; i++){
			for(int j = 0; j<8; j++){
				
				Piece[][] chessBoard = board.getChessBoard();
				String piece = chessBoard[i][j].toString();
				
				BufferedImage img = getPieceImage(piece, dark);
				dark = !dark;

				ImageIcon icon = new ImageIcon(img);
				ChessButton button = buttons[i][j];
				button.setIcon(icon);
			}
			dark = !dark;
		}
	}
}