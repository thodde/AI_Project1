/**
 * Author: Trevor Hodde
 * 
 * Generates a board of size n x n (which is specified by the user) 
 */

import javax.swing.*;

import java.awt.*;

public class Board {

	private JFrame frame;
	private JPanel squares[][];
	private int size;

	public Board(int size) {
		this.setSize(size);
		squares = new JPanel[size][size];
	    frame = new JFrame(size + " Queens Problem");
	    frame.setSize(500, 500);
	    frame.setLayout(new GridLayout(size, size));
	
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
	    
	    // Get the size of the screen
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	     
	    // Determine the new location of the window
	    int w = frame.getSize().width;
	    int h = frame.getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	     
	    // Move the window
	    frame.setLocation(x, y);
	
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}
	
	public void clearQueens() {
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++){
				squares[y][x].removeAll();
				squares[y][x].validate();
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