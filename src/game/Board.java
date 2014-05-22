package game;

import java.util.ArrayList;

public class Board {

	private Piece chessBoard[][];
	public static final int SIZE = 8;
	private Coordinate[] kingPositions;
	private Pawn enPassantPawn;

	public Board() {
		kingPositions = new Coordinate[2];
		kingPositions[Piece.WHITE] = new Coordinate(7, 4);
		kingPositions[Piece.BLACK] = new Coordinate(0, 4);
		chessBoard = new Piece[SIZE][SIZE];
	}

	public boolean equals(Board other){

		for(int i = 0; i<8; i++){
			for(int j = 0;j<8;j++){
				if(chessBoard[i][j].equals(other.chessBoard[i][j])){
					
				}else{
					return false;
				}
			}
		}
		return true;
	}
	
	public void newgame() {

		chessBoard[0][0] = new Rook(Piece.BLACK, 0, 0);
		chessBoard[0][7] = new Rook(Piece.BLACK, 0, 7);
		chessBoard[0][1] = new Knight(Piece.BLACK, 0, 1);
		chessBoard[0][6] = new Knight(Piece.BLACK, 0, 6);
		chessBoard[0][2] = new Bishop(Piece.BLACK, 0, 2);
		chessBoard[0][5] = new Bishop(Piece.BLACK, 0, 5);
		chessBoard[0][3] = new Queen(Piece.BLACK, 0, 3);
		chessBoard[0][4] = new King(Piece.BLACK, 0, 4);
		for (int i = 0; i < 8; ++i) {
			chessBoard[1][i] = new Pawn(Piece.BLACK, 1, i);
			chessBoard[6][i] = new Pawn(Piece.WHITE, 6, i);
			for (int j = 2; j < 6; ++j) {
				chessBoard[j][i] = new Empty(j, i);
			}
		}
		chessBoard[7][0] = new Rook(Piece.WHITE, 7, 0);
		chessBoard[7][7] = new Rook(Piece.WHITE, 7, 7);
		chessBoard[7][1] = new Knight(Piece.WHITE, 7, 1);
		chessBoard[7][6] = new Knight(Piece.WHITE, 7, 6);
		chessBoard[7][2] = new Bishop(Piece.WHITE, 7, 2);
		chessBoard[7][5] = new Bishop(Piece.WHITE, 7, 5);
		chessBoard[7][3] = new Queen(Piece.WHITE, 7, 3);
		chessBoard[7][4] = new King(Piece.WHITE, 7, 4);


	}

	public Piece[][] getChessBoard() {
		return chessBoard;
	}

	public Board makeMove(int fromy, int fromx, int toy, int tox) {

		Board newBoard = clone();
		Piece[][] newChessBoard = newBoard.chessBoard;
		
		newChessBoard[toy][tox] = newChessBoard[fromy][fromx];
		newChessBoard[toy][tox].move(toy, tox);
		newChessBoard[fromy][fromx] = new Empty(fromy, fromx);
		if (newChessBoard[toy][tox] instanceof King) {
			newBoard.kingPositions[newChessBoard[toy][tox].color] = new Coordinate(toy, tox);
			if(tox - fromx == 2){
				newChessBoard[toy][tox-1] = newChessBoard[toy][tox+1];
				newChessBoard[toy][tox+1] = new Empty(toy,tox+1);
				newChessBoard[toy][tox-1].move(toy, tox-1);
			}else if(fromx - tox == 2){
				newChessBoard[toy][tox+1] = newChessBoard[toy][tox-2];
				newChessBoard[toy][tox-2] = new Empty(toy,tox-2);
				newChessBoard[toy][tox+1].move(toy, tox+1);
			}
		}
		
		if (newChessBoard[toy][tox] instanceof Pawn) {
			if(newChessBoard[toy][tox].color == Piece.BLACK && toy == 7){
				newBoard.chessBoard[toy][tox] = new Queen(newChessBoard[toy][tox].color,toy,tox);
			}else if(newChessBoard[toy][tox].color == Piece.WHITE && toy == 0){
				newBoard.chessBoard[toy][tox] = new Queen(newChessBoard[toy][tox].color,toy,tox);
			}
			
			if(fromx != tox && newBoard.chessBoard[fromy][tox].equals(newBoard.enPassantPawn)){
				newBoard.chessBoard[fromy][tox] = new Empty(fromy, tox);				
			}
			
			int diffy = Math.abs(toy - fromy);
			if(diffy == 2){
				newBoard.enPassantPawn = (Pawn) newChessBoard[toy][tox];
			}
		}

		return newBoard;

	}

	public boolean isLegalMove(int fromy, int fromx, int toy, int tox, int color) {
		if(chessBoard[fromy][fromx].color != color){
			return false;
		}
		if(chessBoard[fromy][fromx].color == chessBoard[toy][tox].color){
			return false;
		}
		
		Piece piece = chessBoard[fromy][fromx];
		Piece other = chessBoard[toy][tox];
		
		if (piece.isLegalMove(toy, tox)) {
			if (!(piece instanceof Knight)) {
				for (Coordinate c : piece.getPath(toy, tox)) {
					if (!(chessBoard[c.y][c.x] instanceof Empty)) {
						return false;
					}
				}
			}
		} else {
			return false;
		}
		
		if (piece instanceof Pawn) {
			
			if(piece.x != tox && chessBoard[piece.y][tox].equals(enPassantPawn)){

			}else if(piece.x != tox && other instanceof Empty){
				return false;				
			}
			if (!(other instanceof Empty) && piece.x == tox)
				return false;

		}
		
		Board newBoard = clone();
		piece = newBoard.chessBoard[fromy][fromx];
		other = newBoard.chessBoard[toy][tox];
		int diffx = Math.abs(tox - fromx);
		
		if (piece instanceof King && piece.canCastle && diffx == 2){
				if(fromx-tox > 0){
					if(newBoard.chessBoard[toy][0].canCastle){
						newBoard = makeMove(fromy, fromx, toy, tox+1);
						if (newBoard.kingIsInCheck(color)) {
							return false;
						}
						newBoard = makeMove(fromy, fromx-1, toy, tox);
						if (newBoard.kingIsInCheck(color)) {
							return false;
						}
						tox = 3;
						fromx = 0;
					}
				}else{
					if(newBoard.chessBoard[toy][7].canCastle){
						newBoard = makeMove(fromy, fromx, toy, tox-1);
						if (newBoard.kingIsInCheck(color)) {
							return false;
						}
						newBoard = makeMove(fromy, fromx+1, toy, tox);
						if (newBoard.kingIsInCheck(color)) {
							return false;
						}
						tox = 5;
						fromx = 7;
					}
					
				}
		}
		newBoard = makeMove(fromy, fromx, toy, tox);

		if (newBoard.kingIsInCheck(color)) {
			return false;
		}

		return true;
	}

	public boolean kingIsInCheck(int color) {

		boolean check = false;
		int y = kingPositions[color].y;
		int x = kingPositions[color].x;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (chessBoard[i][j].color != color && !(chessBoard[i][j] instanceof Empty)) {
					if (chessBoard[i][j].isLegalCapturingMove(y, x)) {
						check = true;
						for (Coordinate c : chessBoard[i][j].getPath(y, x)) {
							if (!(chessBoard[c.y][c.x] instanceof Empty)) {
								check = false;
							}
						}
						if (check) {
							return check;
						}
					}
				}
			}
		}
		return check;

	}
	
	public ArrayList<Move> getLegalMoves(int color){
		ArrayList<Move> moves = new ArrayList<Move>();
		for(int i = 0; i<SIZE; i++){
			for(int j = 0; j<SIZE; j++){
				for(int k = 0; k<SIZE; k++){
					for(int l = 0; l<SIZE; l++){
						if(isLegalMove(i,j,k,l,color)){
							moves.add(new Move(i,j,k,l));
						}
					}
				}
			}
		}
		return moves;
	}
	
	public Board clone() {
		Board b = new Board();
		b.setState(getState());
		if(enPassantPawn !=null){
			b.enPassantPawn = (Pawn) enPassantPawn.clone();			
		}
		b.kingPositions[Piece.BLACK] = new Coordinate(kingPositions[Piece.BLACK].y, kingPositions[Piece.BLACK].x);
		b.kingPositions[Piece.WHITE] = new Coordinate(kingPositions[Piece.WHITE].y, kingPositions[Piece.WHITE].x);
		return b;
	}
	
	public Piece[][] getState() {
		Piece[][] state = new Piece[SIZE][SIZE];
	
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				state[i][j] = chessBoard[i][j].clone();
			}
		}
		return state;
	}

	private void setState(Piece[][] state) {
		chessBoard = state;
	}

}
