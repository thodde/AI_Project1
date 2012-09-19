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
		//initialize the local board to zero
		for (int x = 0; x <= size-1; x++)
			for (int y = 0; y <= size-1; y++)
				currentBoard[y][x] = 0;
				
		//reset the flag that indicates if a move has been found that decreases the heuristic
		foundBetterMove = false;
				
		//fill in the appropriate heuristic values
		populateHillValues();
				
		PotentialMove bestMove = new PotentialMove();

		//Find the square with the lowest heuristic value.  this should really write the values to an array.  
		//printing functionality to be removed later
		String outValue = "";
		int lowestSquareValue = 100;
		for (int y = 0; y <= size-1; y++) {
			for (int x = 0; x <= size-1; x++){
				outValue = outValue + currentBoard[y][x] + "|";
						
			if ((currentBoard[y][x] < lowestSquareValue) && //Find the place to move with the lowest attack value
					(y != NQueens.queens[x]) &&  //and is not already occupied by a queen
					(currentBoard[y][x] < currentBoard[NQueens.queens[x]][x])) { //and in fact is better than the currently occupied square
				lowestSquareValue = currentBoard[y][x];
				bestMove.xCoordinate = x;
				bestMove.yCoordinate = y;
			}
		}
			outValue = outValue + "\n";
		}
		//JOptionPane.showMessageDialog(null, outValue);
				
		//Only flag that a better move is available if the lowest square is better than all squares currently occupied by a queen 
		for (int i = 0; i < size; i++) {
			if (lowestSquareValue < (currentBoard[NQueens.queens[i]][i]))
				foundBetterMove = true;
		}
				
		//make the move
		if (foundBetterMove) {
			NQueens.queens[bestMove.xCoordinate] = bestMove.yCoordinate;
		}	
	}

	@Override
	public boolean testCanPerformMove() {
		return foundBetterMove;
	}
	
	//the base method that marks attack directions for every queen
	public void populateHillValues(){
		for (int i = 0; i <= (size-1); i++){
			currentBoard[NQueens.queens[i]][i] += 1; 
			setSquareValues(testDirection.UPLEFT, i, NQueens.queens[i], false);
			setSquareValues(testDirection.UPRIGHT, i, NQueens.queens[i], false);
			setSquareValues(testDirection.DOWNLEFT, i, NQueens.queens[i], false);
			setSquareValues(testDirection.DOWNRIGHT, i, NQueens.queens[i], false);
			setSquareValues(testDirection.LEFT, i, NQueens.queens[i], false);
			setSquareValues(testDirection.RIGHT, i, NQueens.queens[i], false);
			setSquareValues(testDirection.DOWN, i, NQueens.queens[i], false);
			setSquareValues(testDirection.UP, i, NQueens.queens[i], false);
		}
	}
	
	//procedure that increments individual squares that can be attacked by a queen
		public void setSquareValues(testDirection direction, int x, int y, boolean incrementSquare){
			if ((x >= 0) && (x < size) && (y>= 0) && (y < size)){
				if (incrementSquare)
					currentBoard[y][x]++;
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
				setSquareValues(direction, newX, newY, true);
			}
		}
}
