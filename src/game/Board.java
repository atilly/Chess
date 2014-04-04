package game;

public class Board {

	private Piece chessBoard[][];
	public static final int SIZE = 8;
	private Coordinate[] kingPositions;
	private Pawn enPassantPawn;

	public Board() {
		chessBoard = new Piece[SIZE][SIZE];
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
		kingPositions = new Coordinate[2];
		kingPositions[Piece.WHITE] = new Coordinate(7, 4);
		kingPositions[Piece.BLACK] = new Coordinate(0, 4);

	}

	public Piece[][] getChessBoard() {
		return chessBoard;
	}

	public void makeMove(int fromy, int fromx, int toy, int tox) {

		chessBoard[toy][tox] = chessBoard[fromy][fromx];
		chessBoard[toy][tox].move(toy, tox);
		chessBoard[fromy][fromx] = new Empty(fromy, fromx);
		if (chessBoard[toy][tox] instanceof King) {
			kingPositions[chessBoard[toy][tox].color] = new Coordinate(toy, tox);
		}

	}

	public boolean move(int fromy, int fromx, int toy, int tox, int color) {
		Piece piece = chessBoard[fromy][fromx];
		if (piece.color != color) {
			return false;
		}
		Piece other = chessBoard[toy][tox];

		if (piece.color == other.color) {
			return false;
		}

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
		int diffx = Math.abs(tox - fromx);

		if (piece instanceof Pawn) {
			
			if(piece.x != tox && chessBoard[piece.y][tox].equals(enPassantPawn)){
				chessBoard[piece.y][tox] = new Empty(piece.y, tox);
			}else if(piece.x != tox && other instanceof Empty){
				return false;				
			}
			if (!(other instanceof Empty) && piece.x == tox)
				return false;
			


			if(color == Piece.BLACK && toy == 7){
				chessBoard[fromy][fromx] = new Queen(color,toy,tox);
			}else if(color == Piece.WHITE && toy == 0){
				chessBoard[fromy][fromx] = new Queen(color,toy,tox);
			}
			int diffy = Math.abs(toy - fromy);
			if(diffy == 2){
				enPassantPawn = (Pawn) piece;
			}
			
		}

		if (piece instanceof King && piece.canCastle && diffx == 2){

				if(fromx-tox > 0){
					Piece other2 = chessBoard[toy][tox+1];
					if(chessBoard[toy][0].canCastle){
						makeMove(fromy, fromx, toy, tox+1);
						if (kingIsInCheck(color)) {
							makeMove(toy, tox+1, fromy, fromx);
							chessBoard[toy][tox+1] = other2;
							piece.canCastle = true;
							return false;
						}
						makeMove(fromy, fromx-1, toy, tox);
						if (kingIsInCheck(color)) {
							makeMove(toy, tox, fromy, fromx);
							chessBoard[toy][tox] = other;
							piece.canCastle = true;
							return false;
						}
						tox = 3;
						fromx = 0;
					}
				}else{
					Piece other2 = chessBoard[toy][tox-1];
					if(chessBoard[toy][7].canCastle){
						makeMove(fromy, fromx, toy, tox-1);
						if (kingIsInCheck(color)) {
							makeMove(toy, tox-1, fromy, fromx);
							chessBoard[toy][tox-1] = other2;
							piece.canCastle = true;
							return false;
						}
						makeMove(fromy, fromx+1, toy, tox);
						if (kingIsInCheck(color)) {
							makeMove(toy, tox, fromy, fromx);
							chessBoard[toy][tox] = other;
							piece.canCastle = true;
							return false;
						}
						tox = 5;
						fromx = 7;
					}
					
				}
		}
		makeMove(fromy, fromx, toy, tox);

		if (kingIsInCheck(color)) {
			makeMove(toy, tox, fromy, fromx);
			chessBoard[toy][tox] = other;
			return false;
		}

		return true;
	}

	private boolean kingIsInCheck(int color) {

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

}
