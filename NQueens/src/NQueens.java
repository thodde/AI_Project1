/**
 * Author: Trevor Hodde
 * 
 * Randomly places the queens around the board
 * and tries to solve the puzzle. 
 */

import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NQueens {
	public static int[] queens;
	public static int size;
	public static enum testDirection { LEFT, UP, DOWN, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT; } 
	
	public static void main (String[] args) {
		size = getInput();
		int completions = 0;
		int totalGames = 0;
		Board board = new Board(size);
		queens = new int[size];
		//JPanel squares[][] = board.getSquares();

		// temporarily disabled while I worked on the core application code 
		//setTimer();

		do {
			initiallyPlaceQueens();
			redrawScreen(board);
			AIModel myAIModel = new AIModelHillClimb();
			
			//eventually this can be used to repeat testing when we have to complete multiple iterations
			boolean done = false;
			while (!done)
			{
				//do everything required for one move
				myAIModel.performMove();
				
				if (testGameSolved()){ //is it solved?  if so mark as complete and keep going until time is done
					completions++;
					totalGames++;
					//initiallyPlaceQueens();
					//myAIModel = new AIModelHillClimb();
					done = true;
				}
				else if (!myAIModel.testCanPerformMove()){ //stuck, force reset
					//initiallyPlaceQueens(squares);
					//myAIModel = new AIModelHillClimb();
					totalGames++;
					done = true;
				}
				redrawScreen(board);
			}
	
			//temporary diagnostic output.  to be removed later
			String outValue = "";
			outValue = outValue + " Completions:" + completions + "\nTotal Games: " + totalGames;
			JOptionPane.showMessageDialog(null, outValue);
		} while (totalGames < 10);
	}
	
	/**
	 * Makes sure that the number of queens used is greater than 3
	 * and less than 20.
	 * 
	 * @return size
	 */
	public static int getInput() {
		int size;
		String inputNumber;
		
		do {
			inputNumber = JOptionPane.showInputDialog("How many queens would you like to play with?");	
			size = Integer.parseInt(inputNumber);
		} while(size <= 3 || size > 20);
		
		return size;
	}
	
	public static void redrawScreen(Board myBoard) {
		myBoard.clearQueens();
	    for (int i = 0; i < size; i++) {
	    	(myBoard.getSquares())[queens[i]][i].add(new JLabel(new ImageIcon("res/queen.jpg")));
	    	(myBoard.getSquares())[queens[i]][i].validate();
	    }
	}
		
	public static void initiallyPlaceQueens() {
		int randomRow;
		Random generator = new Random();

	    for (int i = 0; i <= size-1; i++) {
	    	randomRow = generator.nextInt(size-1);
	        queens[i] = randomRow;
	    }
	} 

	public static void setTimer() {
		SwingUtilities.invokeLater( new Runnable() {
            public void run() {
            	new Countdown();
            }
        });
	}
	
	/**
	 * This function iterates through each queen and initiates the testBoardSquare function in every available direction for attack
	 * ignore the UP and DOWN directions
	 *  
	 * @return true if no queens contest each other, false otherwise
	 */
	public static boolean testGameSolved(){
		boolean retVal;
		for (int i = 0; i < (size-1); i++){
			retVal = (testBoardSquare(testDirection.UPLEFT, i, queens[i]) && testBoardSquare(testDirection.UPRIGHT, i, queens[i]) && 
					testBoardSquare(testDirection.DOWNLEFT, i, queens[i]) && testBoardSquare(testDirection.DOWNRIGHT, i, queens[i])	&& 
					testBoardSquare(testDirection.LEFT, i, queens[i]) && testBoardSquare(testDirection.RIGHT, i, queens[i]));
			if (retVal == false)
				return false;
		}
		return true;
	}
	
	/**
	 * This function checks to see if a queen resides on this test square.  If a queen is present then this means that
	 * two queens can attack each other
	 * 
	 * @param direction The test direction
	 * @param x
	 * @param y
	 * @return false if a queen resides on the square, true if there are no queens in this call and any recursive calls.
	 */
	public static boolean testBoardSquare(testDirection direction, int x, int y){
		if ((x >= 0) && (x < size) && (y>= 0) && (y < size)){
			if (queens[x] == y) 
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
				return testBoardSquare(direction, newX, newY);
			}
		}
		else
			return true;
	}

}
