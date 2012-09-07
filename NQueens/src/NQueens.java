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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class NQueens {
	private static int[] queens;
	
	public static void main (String[] args) {
		int size = getInput();
		Board board = new Board(size);
		queens = new int[size];
		JPanel squares[][] = board.getSquares();
		initiallyPlaceQueens(squares, size, queens);
		setTimer();
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
	
	public static void initiallyPlaceQueens(JPanel[][] squares, int numberOfQueens, int[] queens) {
		int randomRow;
		Random generator = new Random();

	    for (int i = 0; i <= numberOfQueens-1; i++) {
	    	randomRow = generator.nextInt(numberOfQueens-1);
	        squares[randomRow][i].add(new JLabel(new ImageIcon("res/queen.jpg")));
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
}