package game;

public class Board {

	private Piece chessBoard[][];
	public static final int SIZE = 8;
	
	public Board(){
		chessBoard = new Piece[SIZE][SIZE];
	}

	public void newgame(){

		chessBoard[0][0] = new Piece(Piece.ROOK,Piece.BLACK);
		chessBoard[0][7] = new Piece(Piece.ROOK, Piece.BLACK);
		chessBoard[0][1] = new Piece(Piece.KNIGHT, Piece.BLACK);
		chessBoard[0][6] = new Piece(Piece.KNIGHT, Piece.BLACK);
		chessBoard[0][2] = new Piece(Piece.BISHOP, Piece.BLACK);
		chessBoard[0][5] = new Piece(Piece.BISHOP, Piece.BLACK);
		chessBoard[0][3] = new Piece(Piece.QUEEN, Piece.BLACK);
		chessBoard[0][4] = new Piece(Piece.KING, Piece.BLACK);
		for(int i = 0; i < 8; ++i){
			chessBoard[1][i] = new Piece(Piece.PAWN, Piece.BLACK);
			chessBoard[6][i] = new Piece(Piece.PAWN,Piece.WHITE);
			for(int j = 2; j<6; ++j){
				chessBoard[j][i] = new Piece(Piece.EMPTY);
			}
		}
		chessBoard[7][0] = new Piece(Piece.ROOK, Piece.WHITE);
		chessBoard[7][7] = new Piece(Piece.ROOK, Piece.WHITE);
		chessBoard[7][1] = new Piece(Piece.KNIGHT, Piece.WHITE);
		chessBoard[7][6] = new Piece(Piece.KNIGHT, Piece.WHITE);
		chessBoard[7][2] = new Piece(Piece.BISHOP, Piece.WHITE);
		chessBoard[7][5] = new Piece(Piece.BISHOP, Piece.WHITE);
		chessBoard[7][3] = new Piece(Piece.QUEEN, Piece.WHITE);
		chessBoard[7][4] = new Piece(Piece.KING, Piece.WHITE);

	}

	public Piece[][] getChessBoard() {
		return chessBoard;
	}
	
	public void move(int fromy, int fromx, int toy, int tox){
		
		chessBoard[toy][tox] = chessBoard[fromy][fromx];
		chessBoard[fromy][fromx] = new Piece(Piece.EMPTY);
		
	}
	
	public boolean isLegalMove(int fromy, int fromx, int toy, int tox, int color){
		
		Piece piece = chessBoard[fromy][fromx];
		if(piece.color != color){
			return false;
		}
		Piece other = chessBoard[toy][tox];
		
		if(piece.type == Piece.PAWN){
			if(piece.color == Piece.WHITE){
				if(((toy == fromy -1 && tox == fromx) || (toy == fromy-2 && tox == fromx && fromy == 6 && chessBoard[fromy-1][fromx].type == Piece.EMPTY)) && other.type == Piece.EMPTY){
					return true;
				}
				if(toy == fromy -1 && (tox == fromx-1 || tox == fromx+1) && other.color == Piece.BLACK){
					return true;
				}
			}
			
			if(piece.color == Piece.BLACK){

				if(((toy == fromy +1 && tox == fromx) || (toy == fromy+2 && tox == fromx && fromy == 1 && chessBoard[fromy+1][fromx].type == Piece.EMPTY)) && other.type == Piece.EMPTY){
					return true;
				}
				if(toy == fromy +1 && (tox == fromx-1 || tox == fromx+1) && other.color == Piece.WHITE){
					return true;
				}
			}
		}
		
		if(piece.type == Piece.ROOK){
			return isLegalRookMove(fromy, fromx, toy, tox, piece, other);
		}
		
		if(piece.type == Piece.BISHOP){
			return isLegalBishopMove(fromy, fromx, toy, tox, piece, other);
		}

		if(piece.type == Piece.QUEEN){
			return isLegalBishopMove(fromy, fromx, toy, tox, piece, other) || isLegalRookMove(fromy, fromx, toy, tox, piece, other);
		}
		
		if(piece.type == Piece.KNIGHT){
			
			int diffy = Math.abs(toy - fromy);
			int diffx = Math.abs(tox - fromx);
			
			if((diffy == 1 || diffy == 2)  && (diffx == 1 || diffx == 2) && diffy!=diffx){
				if(other.color != piece.color){
					return true;
				}
			}
		}
		
		if(piece.type == Piece.KING){
			int diffy = Math.abs(toy - fromy);
			int diffx = Math.abs(tox - fromx);
			if(diffy <= 1 && diffx <=1){
				if(other.color != piece.color){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean isLegalRookMove(int fromy, int fromx, int toy, int tox, Piece piece, Piece other){
		
		if(toy == fromy || tox == fromx){
			if(tox < fromx){
				for(int i = tox+1; i<fromx; i++){
					if(!(chessBoard[fromy][i].type == Piece.EMPTY)){
						return false;
					}
				}
			}
			if(tox > fromx){
				for(int i = fromx+1; i<tox; i++){
					if(!(chessBoard[fromy][i].type == Piece.EMPTY)){
						return false;
					}
				}
			}
			if(toy < fromy){
				for(int i = toy+1; i<fromy; i++){
					if(!(chessBoard[i][fromx].type == Piece.EMPTY)){
						return false;
					}
				}
			}
			if(toy > fromy){
				for(int i = fromy+1; i<toy; i++){
					if(!(chessBoard[i][fromx].type == Piece.EMPTY)){
						return false;
					}
				}
			}
			if(other.color != piece.color){
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isLegalBishopMove(int fromy, int fromx, int toy, int tox, Piece piece, Piece other){
		if(toy - fromy == tox - fromx || toy - fromy == fromx - tox){
			if(toy>fromy && tox > fromx){
				int j = fromx+1;
				for(int i = fromy+1; i< toy; i++){
					if(!(chessBoard[i][j].type == Piece.EMPTY)){
						return false;
					}
					j++;
				}
			}
			
			if(toy>fromy && tox < fromx){
				int j = fromx-1;
				for(int i = fromy+1; i< toy; i++){
					if(!(chessBoard[i][j].type == Piece.EMPTY)){
						return false;
					}
					j--;
				}
			}
			
			if(toy<fromy && tox < fromx){
				int j = fromx-1;
				for(int i = fromy-1; i> toy; i--){
					if(!(chessBoard[i][j].type == Piece.EMPTY)){
						return false;
					}
					j--;
				}
			}
			
			if(toy<fromy && tox > fromx){
				int j = fromx+1;
				for(int i = fromy-1; i> toy; i--){
					if(!(chessBoard[i][j].type == Piece.EMPTY)){
						return false;
					}
					j++;
				}
			}

			if(other.color != piece.color){
				return true;
			}
		}
		return false;
	}

}
