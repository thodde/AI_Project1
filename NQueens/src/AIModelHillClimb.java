import javax.swing.JOptionPane;

//import NQueens.testDirection;

public class AIModelHillClimb extends AIModel {
	static int localBoard[][];
	static int size;
	public static enum testDirection { LEFT, UP, DOWN, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT; } 
	
	@Override
	public void performMove() {
		//grab the pertinent values from the NQueens board to make future references shorter
		size = NQueens.size;
		localBoard = new int[size][size];
		
		//initialize the local board to zero
		for (int x = 0; x < size-1; x++)
			for (int y = 0; y < size-1; y++)
				localBoard[y][x] = 0;
		
		//fill in the appropriate utility values
		populateHillValues();

		//temporary output to test values.  to be removed later
		String outValue = "";
		for (int y = 0; y < size-1; y++) {
			for (int x = 0; x < size-1; x++)
				outValue = outValue + localBoard[y][x] + "|";
			outValue = outValue + "\n";
		}
		JOptionPane.showMessageDialog(null, outValue);
	}

	@Override
	public boolean testCanPerformMove() {
		// TODO Auto-generated method stub
		return true;
	}
	
	//the base method that marks attack directions for every queen
	public static void populateHillValues(){
		for (int i = 0; i < (size-1); i++){
			localBoard[NQueens.queens[i]][i] = 100; //some dummy impossibly high value
			setSquareValues(testDirection.UPLEFT, i, NQueens.queens[i]);
			setSquareValues(testDirection.UPRIGHT, i, NQueens.queens[i]);
			setSquareValues(testDirection.DOWNLEFT, i, NQueens.queens[i]);
			setSquareValues(testDirection.DOWNRIGHT, i, NQueens.queens[i]);
			setSquareValues(testDirection.LEFT, i, NQueens.queens[i]);
			setSquareValues(testDirection.RIGHT, i, NQueens.queens[i]);
		}
	}

	//procedure that increments individual squares that can be attacked by a queen
	public static void setSquareValues(testDirection direction, int x, int y){
		if ((x >= 0) && (x < size) && (y>= 0) && (y < size)){
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
			setSquareValues(direction, newX, newY);
		}
	}

}
