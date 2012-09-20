/**
 * Author: Trevor Hodde
 * 
 * Randomly places the queens around the board
 * and tries to solve the puzzle. 
 */

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NQueens {
	public static int[] queens;
	public static int size;
	public static enum testDirection { LEFT, UP, DOWN, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT; } 
	public static Board board;
	private static long completions, attempts, restarts;
	public static boolean displayChessBoard;
	private static AIModel myAIModel;
	
	public static void main (String[] args) {
		// set this to true to show the chess board graphic.  If setting above 20 queens the board is forced hidden
		displayChessBoard = false;
		int currentBoardSize = getInput();
		board = new Board(currentBoardSize, displayChessBoard);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
		Date date = new Date();
		String logFile;
		logFile = "Log_" + dateFormat.format(date) + ".txt";
		board.setLogFile(logFile);
		PrintWriter outLog;
		try {
			outLog = new PrintWriter(new FileWriter(logFile));
			outLog.println("AI Model\tAdditional Details\tBoard Size\tAttempts\tTime Outs\tCompletions\tRestarts\tAverage Attacking Queens\tAverage Completion Time (ms)");
		}
		catch (Exception exc)
		{
			JOptionPane.showMessageDialog(null, "Error creating log file: " + logFile);
			return;
		}
		
		long elapsedTime, startTime;
		long completionTimes, numberOfAttackingQueens;
		
		while ((attempts == 0) || (completions > 24)) { //repeatedly iterate through until completion rate is less than 25
			initializeNewBoardSize(currentBoardSize);
			initiallyPlaceQueens();
			startTime = System.currentTimeMillis();
			completionTimes = 0;
			numberOfAttackingQueens = 0;
			
			redrawScreen(board);
			refreshAIModel();
		
			while (attempts < 100){
				myAIModel.performMove();
				elapsedTime = System.currentTimeMillis() - startTime;
				
				if (elapsedTime > 5000){ // timed out
					attempts++;
					numberOfAttackingQueens += myAIModel.getNumberOfAttackingQueens();
					initiallyPlaceQueens();
					startTime = System.currentTimeMillis();
					refreshAIModel();
				}
				else if (testGameSolved()){ //game completed
					completions++;
					attempts++;
					numberOfAttackingQueens += myAIModel.getNumberOfAttackingQueens();
					initiallyPlaceQueens();
					completionTimes += elapsedTime;
					startTime = System.currentTimeMillis();
					refreshAIModel();
				}
				else if (!myAIModel.testCanPerformMove()){ //stuck, force reset
					restarts++;
					initiallyPlaceQueens();
					refreshAIModel();
				}
				redrawScreen(board);
				board.updateLabels(size, attempts,  completions,  restarts);
			}
			//output to log before board size is increased
			outLog.print(myAIModel.getClass() + "\t" + myAIModel.printModelAdditionalInfo() + "\t" + size + "\t" + attempts + "\t" + (attempts - completions) + "\t" + completions + "\t" + restarts + "\t" + numberOfAttackingQueens / attempts  + "\t");
			if (completions == 0 )
				outLog.println("N/A");
			else
				outLog.println(completionTimes / completions);
			outLog.flush();
			
			currentBoardSize = currentBoardSize * 2;
		}

		outLog.println("Successfully complete");
		outLog.close();
	}
	
	/**
	 * Use this as a one stop location to reset the AI model.  Reduces the chance that multiple AI models will be accidentally combined 
	 */
	private static void refreshAIModel() {
		myAIModel = new AIModelHillClimb();
		//myAIModel = new AIModelHillClimbSidewaysMove();
		//myAIModel = new AIModelBeamSearch();
		//myAIModel = new AIModelRecursion();
	}
	
	private static void initializeNewBoardSize(int newSize) {
		size = newSize;
		queens = new int[size];
		completions = 0;
		attempts = 0;
		restarts = 0;
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
		} while(size <= 3);
		
		if (size > 20)
			displayChessBoard = false;
		
		return size;
	}
	
	public static void redrawScreen(Board myBoard) {
		if (displayChessBoard) {
			myBoard.clearQueens();
		    for (int i = 0; i < size; i++) {
		    	(myBoard.getSquares())[queens[i]][i].add(new JLabel(new ImageIcon("res/queen.jpg")));
		    	(myBoard.getSquares())[queens[i]][i].validate();
		    }
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
		for (int i = 0; i < size; i++){
			retVal = (testBoardSquare(testDirection.UPLEFT, i, queens[i], false) && testBoardSquare(testDirection.UPRIGHT, i, queens[i], false) && 
					testBoardSquare(testDirection.DOWNLEFT, i, queens[i], false) && testBoardSquare(testDirection.DOWNRIGHT, i, queens[i], false) && 
					testBoardSquare(testDirection.LEFT, i, queens[i], false) && testBoardSquare(testDirection.RIGHT, i, queens[i], false));
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
	public static boolean testBoardSquare(testDirection direction, int x, int y, boolean testCurrentSquare){
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
				return testBoardSquare(direction, newX, newY, true);
			}
		}
		else
			return true;
	}

}
