import java.util.ArrayList;
import java.util.Random;

//import javax.swing.JOptionPane;

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
		ArrayList<PotentialMove> myBestList = new ArrayList<PotentialMove>();

		//Find the square with the lowest heuristic value.  this should really write the values to an array.  
		int squareDifferential = -1;
		//String outValue = "";
		for (int y = 0; y <= size-1; y++) {
			for (int x = 0; x <= size-1; x++){
				//outValue = outValue + localBoard[y][x];
				if (squareDifferential < 0) //lowestSquareFound not found yet 
				{
					if ((NQueens.queens[x] != y) && //set if the current square isn't already occupied by a queen
							(localBoard[NQueens.queens[x]][x] >= localBoard[y][x])) {
						if (localBoard[y][x] == localBoard[NQueens.queens[x]][x])
							myBestList.add(new PotentialMove(x, y, true));
						else
							myBestList.add(new PotentialMove(x, y, false));
						squareDifferential = localBoard[NQueens.queens[x]][x] - localBoard[y][x];
					}
				}
				else if ((localBoard[NQueens.queens[x]][x] - localBoard[y][x]) > squareDifferential) { // find the square with the largest differential in value from the queen in the column
					myBestList.clear();
					myBestList.add(new PotentialMove(x, y, false));
					squareDifferential = localBoard[NQueens.queens[x]][x] - localBoard[y][x];
				}
				else if (((localBoard[NQueens.queens[x]][x] - localBoard[y][x]) == squareDifferential) && // the differential is equal to the current best differential
						(NQueens.queens[x] != y)) { // and isn't already occupied by a queen
					myBestList.add(new PotentialMove(x, y, true));
				}
				//else the square is higher, has a queen or isn't marginally better than the current queen's position in the row
			}
			//outValue = outValue + "\n";
		}
		//JOptionPane.showMessageDialog(null, outValue);
		
		if (myBestList.isEmpty())
			return;
		
		int listSize = myBestList.size();
		PotentialMove bestMove;
		
		//grab the non-Sideways moves first
		for (int i = 0; i < listSize; i++) {
			if (!(myBestList.get(i).isSideways)) {
				bestMove = myBestList.get(i);
				foundBetterMove = true;
				sidewaysMoves = 0;
				NQueens.queens[bestMove.xCoordinate] = bestMove.yCoordinate;
				return;
			}
		}
		
		if (sidewaysMoves > 20) { // hit 20 consecutive sideways moves, mark as unsolvable
			return;
		}
		
		//all available moves sideways moves, let's select one randomly
		Random generator = new Random();
		int randomElement = generator.nextInt(listSize);
		
		bestMove = myBestList.get(randomElement);
		foundBetterMove = true;
		sidewaysMoves++;
		NQueens.queens[bestMove.xCoordinate] = bestMove.yCoordinate;
	}
}
