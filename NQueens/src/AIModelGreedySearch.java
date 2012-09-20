import java.util.ArrayList;

public class AIModelGreedySearch extends AIModel {
	private boolean initialRunPerformed;
	protected boolean foundBetterMove;
	protected long numberOfAttackingQueens;
	public enum testDirection { LEFT, UP, DOWN, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT; } 
	
	private int size;
	private ArrayList<BoardConfiguration> myBoards;
	
	AIModelGreedySearch(){
		myBoards = new ArrayList<BoardConfiguration>();
		numberOfAttackingQueens = 0;
		initialRunPerformed = false;
	}
	
	@Override
	public void performMove() {
		foundBetterMove = false;
		if (myBoards.isEmpty()){ //duplicate the current queen list
			if (!initialRunPerformed) {
				size = NQueens.size;
				BoardConfiguration tmpBoard = new BoardConfiguration(size);
				tmpBoard.setQueenList(NQueens.queens);
				initialRunPerformed = true;
			}
			else {
				//exhausted all available options, solution not found, reset entire board
				return;
			}
		}
		
		int localBoard[][] = new int[size][size];
		ArrayList<BoardConfiguration> newBoardList = new ArrayList<BoardConfiguration>();
		ArrayList<PotentialMove> bestMoveList;
		
		while(!myBoards.isEmpty()){
			BoardConfiguration tempBoard = myBoards.remove(0);
			bestMoveList = new ArrayList<PotentialMove>();
			
			for (int x = 0; x < size; x++)
				for (int y = 0; y < size; y++)
					localBoard[y][x] = 0;
			
			populateHillValues(localBoard, tempBoard.queens);
				
			int squareDifferential = -1;
			for (int y = 0; y <= size-1; y++) {
				for (int x = 0; x <= size-1; x++){
					if (squareDifferential < 0) //lowestSquareFound not found yet 
					{
						if ((tempBoard.queens[x] != y) && //set if the current square isn't already occupied by a queen
								(localBoard[tempBoard.queens[x]][x] >= localBoard[y][x])) {
							if (localBoard[y][x] == localBoard[tempBoard.queens[x]][x])
								bestMoveList.add(new PotentialMove(x, y, true));
							else
								bestMoveList.add(new PotentialMove(x, y, false));
							squareDifferential = localBoard[tempBoard.queens[x]][x] - localBoard[y][x];
						}
					}
					else if ((localBoard[tempBoard.queens[x]][x] - localBoard[y][x]) > squareDifferential) { // find the square with the largest differential in value from the queen in the column
						bestMoveList.clear();
						bestMoveList.add(new PotentialMove(x, y, false));
						squareDifferential = localBoard[tempBoard.queens[x]][x] - localBoard[y][x];
					}
					else if (((localBoard[tempBoard.queens[x]][x] - localBoard[y][x]) == squareDifferential) && // the differential is equal to the current best differential
							(tempBoard.queens[x] != y)) { // and isn't already occupied by a queen
						bestMoveList.add(new PotentialMove(x, y, true));
					}
					//else the square is higher, has a queen or isn't marginally better than the current queen's position in the row
				}

				PotentialMove tmpMove;				
				while(!bestMoveList.isEmpty()) { //cycle through all potential moves and "play" them
					foundBetterMove = true;
					tmpMove = bestMoveList.remove(0);
					BoardConfiguration newBoard = tempBoard.clone();
					newBoard.queens[tmpMove.xCoordinate] = tmpMove.yCoordinate;

					if(testGameSolved(newBoard.queens)) { //found a solution, write to the base list
						for (int i = 0; i < size; i++) {
							NQueens.queens[i] = newBoard.queens[i];
						}
						//stop here, solution found
						return;
					}

					newBoardList.add(newBoard);
				}
			}
			myBoards = newBoardList;
		}
	}
	
	public boolean testGameSolved(int queens[]){
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
	
	public void populateHillValues(int localBoard[][], int queenList[]){
		for (int i = 0; i <= (size-1); i++){
			localBoard[queenList[i]][i] += 1; 
			setSquareValues(testDirection.UPLEFT, i, NQueens.queens[i], false, localBoard);
			setSquareValues(testDirection.UPRIGHT, i, NQueens.queens[i], false, localBoard);
			setSquareValues(testDirection.DOWNLEFT, i, NQueens.queens[i], false, localBoard);
			setSquareValues(testDirection.DOWNRIGHT, i, NQueens.queens[i], false, localBoard);
			setSquareValues(testDirection.LEFT, i, NQueens.queens[i], false, localBoard);
			setSquareValues(testDirection.RIGHT, i, NQueens.queens[i], false, localBoard);
			setSquareValues(testDirection.DOWN, i, NQueens.queens[i], false, localBoard);
			setSquareValues(testDirection.UP, i, NQueens.queens[i], false, localBoard);
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
