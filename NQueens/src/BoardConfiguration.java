
public class BoardConfiguration {
	public int queens[];
	private int localSize;
	
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
		for(int i = 0; i < localSize; i++) {
			queens[i] = newQueensList[i];
		}
	}
}