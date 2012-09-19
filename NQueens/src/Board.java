/**
 * Author: Trevor Hodde
 * 
 * Generates a board of size n x n (which is specified by the user) 
 */

import javax.swing.*;

import java.awt.*;

public class Board {

	private JPanel frame;
	private JFrame masterFrame;
	private JPanel squares[][];
	private JLabel attemptLabel, completionLabel, restartLabel;
	private JLabel logFileLabel;
	private int size;

	public Board(int size) {
		this.setSize(size);
		squares = new JPanel[size][size];
		masterFrame = new JFrame(size + " Queens Problem");
		masterFrame.setSize(515, 700);
		masterFrame.setLayout(null);

	    frame = new JPanel();
	    frame.setLayout(new GridLayout(size, size));
	    frame.setSize(500, 500);
	
	    for (int i = 0; i < size; i++) {
	        for (int j = 0; j < size; j++) {
	            squares[i][j] = new JPanel();
	
	            if ((i + j) % 2 == 0) {
	                squares[i][j].setBackground(Color.black);
	            } else {
	                squares[i][j].setBackground(Color.white);
	            }   
	            frame.add(squares[i][j]);
	        }
	    }
	    
	    Insets insets = masterFrame.getInsets();

	    JLabel tmpLabel = new JLabel("Size: " + size);
	    masterFrame.add(tmpLabel);
	    tmpLabel.setBounds(insets.left + 5, insets.top + 505, 200, 20);
	    tmpLabel.setVisible(true);

	    attemptLabel = new JLabel("Attempts: 0");
	    masterFrame.add(attemptLabel);
	    attemptLabel.setBounds(insets.left + 5, insets.top + 530, 200, 20);
	    attemptLabel.setVisible(true);

	    completionLabel = new JLabel("Completions: 0");
	    masterFrame.add(completionLabel);
	    completionLabel.setBounds(insets.left + 5, insets.top + 555, 200, 20);
	    completionLabel.setVisible(true);

	    restartLabel = new JLabel("Restarts: 0");
	    masterFrame.add(restartLabel);
	    restartLabel.setBounds(insets.left + 5, insets.top + 580, 200, 20);
	    restartLabel.setVisible(true);
	    
	    logFileLabel = new JLabel("Log File:");
	    masterFrame.add(logFileLabel);
	    logFileLabel.setBounds(insets.left + 5, insets.top + 605, 400, 20);
	    logFileLabel.setVisible(true);
	    
	    // Move the window
	    masterFrame.add(frame);
	    frame.setVisible(true);
	
	    masterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    masterFrame.setVisible(true);
	}
	
	public void updateLabels(long attempts, long completions, long restarts){
		attemptLabel.setText("Attempts: " + attempts);
		attemptLabel.invalidate();
		completionLabel.setText("Completions: " + completions);
		completionLabel.invalidate();
		restartLabel.setText("Restarts: " + restarts);
		restartLabel.invalidate();
	}
	
	public void setLogFile(String logName) {
		logFileLabel.setText("Log File: " + logName);
	}
	
	
	public void clearQueens() {
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++){
				squares[y][x].removeAll();
				squares[y][x].invalidate();
				squares[y][x].repaint();
			}
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public JPanel[][] getSquares() {
		return squares;
	}
}