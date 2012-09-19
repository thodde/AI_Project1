
public class AIModelHillClimbSidewaysMove extends AIModelHillClimb {
	private int sidewaysMoves;
	
	AIModelHillClimbSidewaysMove(){
		super();
		sidewaysMoves = 0;
	}
	
	public void performMove() {
		//initialize the local board to zero
		for (int x = 0; x <= size-1; x++)
			for (int y = 0; y <= size-1; y++)
				localBoard[y][x] = 0;
		
		//reset the flag that indicates if a move has been found that decreases the heuristic
		foundBetterMove = false;
		
		//fill in the appropriate heuristic values
		populateHillValues();
		
		PotentialMove bestMove = new PotentialMove();

		//Find the square with the lowest heuristic value.  this should really write the values to an array.  
		int lowestSquareValue = 100;
		for (int y = 0; y <= size-1; y++) {
			for (int x = 0; x <= size-1; x++){
				if ((localBoard[y][x] <= lowestSquareValue) && //Find the place to move with the lowest attack value //!< Only difference here is that lowestSquareValue is <= not <
						(y != NQueens.queens[x]) &&  //and is not already occupied by a queen
						(localBoard[y][x] < localBoard[NQueens.queens[x]][x])) { //and in fact is better than the currently occupied square
					lowestSquareValue = localBoard[y][x];
					bestMove.xCoordinate = x;
					bestMove.yCoordinate = y;
					
					//here is a recording of the sideways moves
					if (localBoard[y][x] == lowestSquareValue)
						sidewaysMoves++;
					else
						sidewaysMoves = 0;
				}
			}
		}
		
		//Only flag that a better move is available if the lowest square is better than all squares currently occupied by a queen 
		for (int i = 0; i < size; i++) {
			if (lowestSquareValue < (localBoard[NQueens.queens[i]][i]))
				foundBetterMove = true;
		}
		
		if (sidewaysMoves > 20) { //if there have been more than 20 consecutive sideways moves then error out and move on
			// trouble here is that there is no random detection of available sidways moves, so will continuously restest the same 2 moves.  I should change this
			foundBetterMove = false;
		}
		
		//make the move
		if (foundBetterMove) {
			NQueens.queens[bestMove.xCoordinate] = bestMove.yCoordinate;
		}
	}
}
