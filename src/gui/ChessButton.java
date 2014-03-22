package gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ChessButton extends JButton{

	public int y,x;
	
	public ChessButton(ImageIcon icon, int y, int x){
		super(icon);
		this.y = y;
		this.x = x;
	}
	
}
