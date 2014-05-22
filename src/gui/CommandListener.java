package gui;

import game.Board;
import game.Coordinate;
import game.Move;
import game.Piece;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import util.ResultWriter;

import agent.Agent;

public class CommandListener implements ActionListener {

	private Coordinate from, to;
	private Board board;
	private ChessGUI gui;
	private Agent agent;
	private int playerColor, agentColor;
	
	public CommandListener(Board board, ChessGUI gui, Agent agent, int playerColor, int agentColor){
		this.board = board;
		this.gui = gui;
		this.agent = agent;
		this.playerColor = playerColor;
		this.agentColor = agentColor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ChessButton button = (ChessButton) e.getSource();
		if(from == null){
			from = new Coordinate(button.y, button.x);
		}else{
			to = new Coordinate(button.y, button.x);

			if(board.isLegalMove(from.y, from.x, to.y, to.x, playerColor)){
				board = board.makeMove(from.y, from.x, to.y, to.x);
				gui.update(board);
				if(board.getLegalMoves(agentColor).size() == 0){
					System.out.println("you won");
					ResultWriter.writeResult("loss", agent.values);
				}
				/*
				if(playerColor == Piece.WHITE){
					playerColor = Piece.BLACK;
				}else{
					playerColor = Piece.WHITE;
				}
				*/
			//	System.out.println(from.y+ " " + from.x+ " " + to.y+ " " + to.x);
				Move move = agent.move(board, new Move(from.y, from.x, to.y, to.x));
				board = board.makeMove(move.fromy, move.fromx, move.toy, move.tox);
				//board.makemove(gnuchess)
				if(board.getLegalMoves(playerColor).size() == 0){
					System.out.println("you lost");
					ResultWriter.writeResult("win", agent.values);
				}
				gui.update(board);
			}
			from = null;
		}
	}
	
}
