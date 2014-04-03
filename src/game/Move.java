package game;

public class Move {

	public int fromy, fromx, toy, tox;

	public Move(int fromy, int fromx, int toy, int tox) {
		this.fromy = fromy;
		this.fromx = fromx;
		this.toy = toy;
		this.tox = tox;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fromx;
		result = prime * result + fromy;
		result = prime * result + tox;
		result = prime * result + toy;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (fromx != other.fromx)
			return false;
		if (fromy != other.fromy)
			return false;
		if (tox != other.tox)
			return false;
		if (toy != other.toy)
			return false;
		return true;
	}
	
	public String toString(){
		return fromy + " " + fromx + " " + toy + " " + tox;
	}
	
}
