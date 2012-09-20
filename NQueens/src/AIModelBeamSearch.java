import java.util.Hashtable;
import java.util.LinkedList;

public class AIModelBeamSearch extends AIModel {
	private boolean initialRunPerformed;
	protected boolean foundBetterMove;
	protected long numberOfAttackingQueens;
	
	protected LinkedList<BoardConfiguration> solutionsList;
	protected Hashtable<Long, BoardConfiguration> possibleStates;
	private int size;
	
	public enum testDirection { LEFT, UP, DOWN, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT; } 
	
	public AIModelBeamSearch() {
		solutionsList = new LinkedList<BoardConfiguration>();
		possibleStates = new Hashtable<Long, BoardConfiguration>();
		initialRunPerformed = false;
		numberOfAttackingQueens = 0;
	}
	
	@Override
	public void performMove() {
		foundBetterMove = false;
		
		if(solutionsList.isEmpty()) {
			if(!initialRunPerformed) {
				size = NQueens.size;
				BoardConfiguration tmpBoard = new BoardConfiguration(size);
				tmpBoard.setQueenList(NQueens.queens);
				solutionsList.push(tmpBoard);
				possibleStates.put(tmpBoard.queenConfiguration, tmpBoard);
				initialRunPerformed = true;
			}
			else {
				return;
			}
		}
		
		int maxBoardsSaved = 8;
		int localBoard[][] = new int[size][size];
		LinkedList<BoardConfiguration> newBoardList = new LinkedList<BoardConfiguration>();
		LinkedList<PotentialMove> bestMoveList;
		
		while((!solutionsList.isEmpty()) && (newBoardList.size() < maxBoardsSaved)) {
			BoardConfiguration tempBoard = solutionsList.pop();
			
			if(tempBoard != possibleStates.get(tempBoard.queenConfiguration)) {
				possibleStates.put(tempBoard.queenConfiguration, tempBoard);
			}
			bestMoveList = new LinkedList<PotentialMove>();
			
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					localBoard[j][i] = 0;
				}
			}
			
			populateHillValues(localBoard, tempBoard.queens);
			
			int squareDifferential = -1;
			for(int j = 0; j < size; j++) {
				for(int i = 0; i < size; i++) {
					if(squareDifferential < 0) {
						if((tempBoard.queens[i] != j) && (localBoard[tempBoard.queens[i]][i] >= localBoard[j][i])) {
							if(localBoard[j][i] == localBoard[tempBoard.queens[i]][i])
								bestMoveList.push(new PotentialMove(i, j, true));
							else
								bestMoveList.push(new PotentialMove(i, j, false));
							squareDifferential = localBoard[tempBoard.queens[i]][i] - localBoard[j][i];
						}
					}
					else if((localBoard[tempBoard.queens[i]][i] - localBoard[j][i]) > squareDifferential) {
						bestMoveList.clear();
						bestMoveList.push(new PotentialMove(i, j, false));
						squareDifferential = localBoard[tempBoard.queens[i]][i] - localBoard[j][i];
					}
					else if(((localBoard[tempBoard.queens[i]][i] - localBoard[j][i]) == squareDifferential) && 
							(tempBoard.queens[i] != j)) {
						bestMoveList.push(new PotentialMove(i, j, true));
					}
				}
			
				PotentialMove tmpMove;
				while(!bestMoveList.isEmpty()) {
					foundBetterMove = true;
					tmpMove = bestMoveList.pop();
					
					BoardConfiguration newBoard = tempBoard.clone();
					newBoard.queens[tmpMove.xCoordinate] = tmpMove.yCoordinate;
					
					if(testGameSolved(newBoard.queens)) {
						for(int i = 0; i < size; i++) {
							NQueens.queens[i] = newBoard.queens[i];
						}
						//stop here, solution found
						return;
					}
					//if the queen configuration has already been loaded, then don't add it in
					if (!newBoardList.contains(newBoard))
						newBoardList.push(newBoard);
				}
			}
			solutionsList = newBoardList;
		}
	}
	
	public boolean testGameSolved(int queens[]) {
		boolean retVal;
		for (int i = 0; i < size; i++){
			retVal = (testBoardSquare(testDirection.UPLEFT, i, queens[i], false, queens) && testBoardSquare(testDirection.UPRIGHT, i, queens[i], false, queens) && 
					testBoardSquare(testDirection.DOWNLEFT, i, queens[i], false, queens) && testBoardSquare(testDirection.DOWNRIGHT, i, queens[i], false, queens) && 
					testBoardSquare(testDirection.LEFT, i, queens[i], false, queens) && testBoardSquare(testDirection.RIGHT, i, queens[i], false, queens));
			if (retVal == false)
				return false;
		}
		return true;
	}
	
	public boolean testBoardSquare(testDirection direction, int x, int y, boolean testCurrentSquare, int queens[]){
		if ((x >= 0) && (x < size) && (y>= 0) && (y < size)){
			if ((queens[x] == y) && testCurrentSquare) 
				return false;
			else	{
				int newX = x;
				int newY = y;
				switch (direction){
				case UPLEFT:
					newX--;
					newY--;
					break;
				case UPRIGHT:
					newX++;
					newY--;
					break;
				case DOWNLEFT:
					newX--;
					newY++;
					break;
				case DOWNRIGHT:
					newX++;
					newY++;
					break;
				case UP:
					newY--;
					break;
				case DOWN:
					newY++;
					break;
				case RIGHT:
					newX++;
					break;
				case LEFT:
					newX--;
					break;
				default:
					break;
				}
				return testBoardSquare(direction, newX, newY, true, queens);
			}
		}
		else
			return true;
	}
	
	//the base method that marks attack directions for every queen
	public void populateHillValues(int localBoard[][], int queensList[]){
		for (int i = 0; i <= (size-1); i++){
			localBoard[queensList[i]][i] += 1; 
			setSquareValues(testDirection.UPLEFT, i, queensList[i], false, localBoard);
			setSquareValues(testDirection.UPRIGHT, i, queensList[i], false, localBoard);
			setSquareValues(testDirection.DOWNLEFT, i, queensList[i], false, localBoard);
			setSquareValues(testDirection.DOWNRIGHT, i, queensList[i], false, localBoard);
			setSquareValues(testDirection.LEFT, i, queensList[i], false, localBoard);
			setSquareValues(testDirection.RIGHT, i, queensList[i], false, localBoard);
			setSquareValues(testDirection.DOWN, i, queensList[i], false, localBoard);
			setSquareValues(testDirection.UP, i, queensList[i], false, localBoard);
		}
	}
	
	//procedure that increments individual squares that can be attacked by a queen
	public void setSquareValues(testDirection direction, int x, int y, boolean incrementSquare, int localBoard[][]){
		if ((x >= 0) && (x < size) && (y>= 0) && (y < size)){
			if (incrementSquare)
				localBoard[y][x]++;
			int newX = x;
			int newY = y;
			switch (direction){
			case UPLEFT:
				newX--;
				newY--;
				break;
			case UPRIGHT:
				newX++;
				newY--;
				break;
			case DOWNLEFT:
				newX--;
				newY++;
				break;
			case DOWNRIGHT:
				newX++;
				newY++;
				break;
			case UP:
				newY--;
				break;
			case DOWN:
				newY++;
				break;
			case RIGHT:
				newX++;
				break;
			case LEFT:
				newX--;
				break;
			default:
				break;
			}
			setSquareValues(direction, newX, newY, true, localBoard);
		}
	}
	
	@Override
	public boolean testCanPerformMove() {
		return foundBetterMove;
	}
}
