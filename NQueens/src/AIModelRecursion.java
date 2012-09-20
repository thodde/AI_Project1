/**
 * I have no idea how close/far this class is to working (it doesn't work at all yet).
 * I just whipped it up late last night to see if I could hack together some recursive stuff.
 * I may end up just deleting it if I can't get it to work, but it was 
 * worth a try. Feel free to play around with it if you think its
 * salvagable.
 */

public class AIModelRecursion extends AIModel {
	protected int[][] localBoard;
	protected int[] columns;
	protected int size;
	protected int count;
	protected boolean foundBetterMove;
	protected long numberOfAttackingQueens;
	public enum testDirection { LEFT, UP, DOWN, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT; } 
	
	public AIModelRecursion() {
		//grab the pertinent values from the NQueens board to make future references shorter
		count = 0;
		size = NQueens.size;
		numberOfAttackingQueens = 0;
		localBoard = new int[size][size];
		columns = new int[size];
	}
	
	public void placeQueen(int row) {
		PotentialMove bestMove = new PotentialMove();
        if (promising(row)) {
             if (row == (size-1)) {
                  //print out result
                  for (int i = 0; i < size; i++)
                	  NQueens.queens[bestMove.xCoordinate] = bestMove.yCoordinate;
               }
               else {
                   for (int j = 0; j < size; j++) {
                        columns[row + 1] = j;
                        placeQueen(row + 1);
                   }
               }
        }
   }

   private boolean promising(int index) {
        int k = 0;
        boolean flag = true;
        while (k < index && flag) {
             if ( columns[index] == columns[k] ||
                      Math.abs(columns[index] - columns[k]) == index - k)
                  flag = false;
        }
        return flag;
   }

   public void placeNQueens ( ) {
        for (int i = 0; i < size; i++) {
             columns[0] = i;
             placeQueen(0);
        }
   }
	
	@Override
	public void performMove() {
		placeNQueens();
	}

	@Override
	public boolean testCanPerformMove() {
		return foundBetterMove;
	}
}
