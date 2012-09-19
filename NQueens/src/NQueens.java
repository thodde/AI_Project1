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
	
	public static void main (String[] args) {
		size = getInput();
		board = new Board(size);
		queens = new int[size];
		completions = 0;
		attempts = 0;
		restarts = 0;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
		Date date = new Date();
		String logFile;
		logFile = "Log_" + dateFormat.format(date) + ".txt";
		board.setLogFile(logFile);
		PrintWriter outLog;
		try {
			outLog = new PrintWriter(new FileWriter(logFile));
			outLog.println("AI Model\tBoard Size\tAttempts\tTime Outs\tCompletions\tRestarts");
		}
		catch (Exception exc)
		{
			JOptionPane.showMessageDialog(null, "Error creating log file: " + logFile);
			return;
		}
		
//		int totalIterations = 0;
//		int beamSearchCompletions = 0;
//		int hillClimbCompletions = 0;
		long elapsedTime;

		// temporarily disabled while I worked on the core application code 
		//setTimer();
		
		initiallyPlaceQueens();
		long startTime = System.currentTimeMillis();
		redrawScreen(board);
		AIModel myAIModel = new AIModelHillClimbSidewaysMove();
		//AIModel myAIModel = new AIModelHillClimb();
	
		while (attempts < 100){
			myAIModel.performMove();
			elapsedTime = System.currentTimeMillis() - startTime;
			
			if (elapsedTime > 5000){
				attempts++;
				initiallyPlaceQueens();
				startTime = System.currentTimeMillis();
				//myAIModel = new AIModelHillClimb();
				myAIModel = new AIModelHillClimbSidewaysMove();
			}
			else if (testGameSolved()){
				completions++;
				attempts++;
				initiallyPlaceQueens();
				startTime = System.currentTimeMillis();
				//myAIModel = new AIModelHillClimb();
				myAIModel = new AIModelHillClimbSidewaysMove();
			}
			else if (!myAIModel.testCanPerformMove()){ //stuck, force reset
				restarts++;
				initiallyPlaceQueens();
				//myAIModel = new AIModelHillClimb();
				myAIModel = new AIModelHillClimbSidewaysMove();
			}
			redrawScreen(board);
			board.updateLabels(attempts,  completions,  restarts);
		}
		//when iterating across multiple board sizes this line below has to be called between each iteration.  the other two lines are called when the program is closing down.
		outLog.println(myAIModel.getClass() + "\t" + size + "\t" + attempts + "\t" + (attempts - completions) + "\t" + completions + "\t" + restarts);

		outLog.println("Successfully complete");
		outLog.close();
		/*
		do {
			initiallyPlaceQueens();
			redrawScreen(board);
			AIModel hillClimbAIModel = new AIModelHillClimb();
			AIModel beamSearchAIModel = new AIModelBeamSearch();
			
			//The different types of searches can be separated into different loops or something later
			//so that we can keep track of their metrics in different ways, but for now it's
			//just easier to test them all at once since nothing fully works yet anyway.
			hillClimbCompletions = hillClimbProcedure(hillClimbAIModel, hillClimbCompletions);
			beamSearchCompletions = beamSearchProcedure(beamSearchAIModel, beamSearchCompletions);
			totalIterations++;
			
		} while (totalIterations < 10); //this will be changed to 99999999 at some point*/
	}

	/*
	public static int beamSearchProcedure(AIModel beamSearchAIModel, int completions) {
		//eventually this can be used to repeat testing when we have to complete multiple iterations
			boolean done = false;
				
			while (!done)
			{
				//do everything required for one move
				beamSearchAIModel.performMove();
				
				if (testGameSolved()){ //is it solved?  if so mark as complete and keep going until time is done
					completions++;
					//initiallyPlaceQueens();
					//beamSearchAIModel = new AIModelBeamSearch();
					done = true;
				}
				else if (!beamSearchAIModel.testCanPerformMove()){ //stuck, force reset
					//initiallyPlaceQueens(squares);
					//beamSearchAIModel = new AIModelBeamSearch();
					done = true;
				}
				redrawScreen(board);
			}
				
			//temporary diagnostic output.  to be removed later
			String outValue = "";
			outValue = outValue + " Beam Search Completions:" + completions;
			JOptionPane.showMessageDialog(null, outValue);
			return completions;
	}

	public static int hillClimbProcedure(AIModel hillClimbAIModel, int completions) {
		//eventually this can be used to repeat testing when we have to complete multiple iterations
		boolean done = false;
		
		while (!done)
		{
			//do everything required for one move
			hillClimbAIModel.performMove();
			
			if (testGameSolved()){ //is it solved?  if so mark as complete and keep going until time is done
				completions++;
				//initiallyPlaceQueens();
				//hillClimbAIModel = new AIModelHillClimb();
				done = true;
			}
			else if (!hillClimbAIModel.testCanPerformMove()){ //stuck, force reset
				//initiallyPlaceQueens(squares);
				//hillClimbAIModel = new AIModelHillClimb();
				done = true;
			}
			redrawScreen(board);
		}
		
		//temporary diagnostic output.  to be removed later
		String outValue = "";
		outValue = outValue + " Hill Climbing Completions:" + completions;
		JOptionPane.showMessageDialog(null, outValue);
		return completions;
	}*/
	
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
			retVal = (testBoardSquare(testDirection.UPLEFT, i, queens[i], false) && testBoardSquare(testDirection.UPRIGHT, i, queens[i], false) && 
					testBoardSquare(testDirection.DOWNLEFT, i, queens[i], false) && testBoardSquare(testDirection.DOWNRIGHT, i, queens[i], false)	&& 
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
