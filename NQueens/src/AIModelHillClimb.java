import java.io.PrintWriter;

class PotentialMove {
	public int xCoordinate, yCoordinate;
	boolean isSideways;
	
	PotentialMove(){
	}
	
	PotentialMove(int newX, int newY, boolean newSideways) {
		xCoordinate = newX;
		yCoordinate = newY;
		isSideways = newSideways;
	}
}

public class AIModelHillClimb extends AIModel {
	protected int localBoard[][];
	protected int size;
	protected boolean foundBetterMove;
	protected long numberOfAttackingQueens;
	public enum testDirection { LEFT, UP, DOWN, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT; } 
	
	public AIModelHillClimb() {
		//grab the pertinent values from the NQueens board to make future references shorter
		size = NQueens.size;
		numberOfAttackingQueens = 0;
		localBoard = new int[size][size];		
	}
	
	public long getNumberOfAttackingQueens() {
		long retVal = 0;
		
		for (int x = 0; x <= size-1; x++)
			for (int y = 0; y <= size-1; y++)
				localBoard[y][x] = 0;
		
		populateHillValues();

		for (int i = 0; i < size; i++) {
			retVal = retVal + localBoard[NQueens.queens[i]][i] - 1;
		}
		return retVal;
	}
	
	public void writeOutlocalBoard(PrintWriter outLog) {
		String retVal;
		
		for (int y = 0; y <= size-1; y++) {
			retVal = "";
			for (int x = 0; x <= size-1; x++){
				if (NQueens.queens[x] == y)
					retVal = retVal + 'Q' + localBoard[y][x];
				else
					retVal = retVal + localBoard[y][x];
				retVal = retVal + "|";
			}
			outLog.println(retVal);
		}
		outLog.println("");
	}
	
	@Override
	public void performMove() {
		//initialize the local board to zero
		for (int x = 0; x < size; x++)
			for (int y = 0; y < size; y++)
				localBoard[y][x] = 0;
		
		//reset the flag that indicates if a move has been found that decreases the heuristic
		foundBetterMove = false;
		
		//fill in the appropriate heuristic values
		populateHillValues();
		
		PotentialMove bestMove = new PotentialMove();

		//Find the square with the lowest heuristic value.  this should really write the values to an array.  
		int lowestSquareValue = size+1;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++){
				if ((localBoard[y][x] < lowestSquareValue) && //Find the place to move with the lowest attack value
						(y != NQueens.queens[x]) &&  //and is not already occupied by a queen
						(localBoard[y][x] < localBoard[NQueens.queens[x]][x])) { //and in fact is better than the currently occupied square
					lowestSquareValue = localBoard[y][x];
					bestMove.xCoordinate = x;
					bestMove.yCoordinate = y;
				}
			}
		}
		
		//Only flag that a better move is available if the lowest square is better than all squares currently occupied by a queen 
		for (int i = 0; i < size; i++) {
			if (lowestSquareValue < localBoard[NQueens.queens[i]][i])
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
			localBoard[NQueens.queens[i]][i] += 1; 
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
			setSquareValues(direction, newX, newY, true);
		}
	}

}
