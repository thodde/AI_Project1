import java.util.LinkedList;

public class AIModelBeamSearch extends AIModel {
	boolean foundBetterMove;
	int size;
	LinkedList<int[][]> solutionList;
	int currentBoard[][];
	public enum testDirection { LEFT, UP, DOWN, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT; }
	
	public AIModelBeamSearch() {
		size = NQueens.size;
		currentBoard = new int[size][size];
		solutionList = new LinkedList<int[][]>();
	}
	
	@Override
	public void performMove() {
		
	}

	@Override
	public boolean testCanPerformMove() {
		return foundBetterMove;
	}
}
