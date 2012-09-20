
public class BoardConfiguration {
	public int queens[];
	private int localSize;
	public long queenConfiguration;
	
	BoardConfiguration(int newSize) {
		localSize = newSize;
		queens = new int[newSize];
	}
	
	public BoardConfiguration clone() {
		BoardConfiguration newBoard = new BoardConfiguration(localSize);
		newBoard.setQueenList(queens);
		return newBoard;
	}
	
	public void setQueenList(int newQueensList[]) {
		queenConfiguration = 0;
		for(int i = 0; i < localSize; i++) {
			queenConfiguration = queenConfiguration * localSize + queens[i];
			queens[i] = newQueensList[i];
		}
	}
	
	public boolean equal(BoardConfiguration testBoard) {
		if (testBoard.queenConfiguration == queenConfiguration)
			return true;
		
		return false;
	}
	
}