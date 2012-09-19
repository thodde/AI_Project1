/**
 * Author: Trevor Hodde
 * 
 * This keeps track of the 5 second time limit. * 
 */

import java.awt.event.*;
import javax.swing.*;


public class Countdown {
    private Timer timer;
    private JProgressBar progressBar;

    public Countdown() {
    	//create the progress bar with starting value of 0
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 5);
        progressBar.setValue(0);
        
        //This actionlistener is updated each second to
        //keep the progress bar accurate. When the progress bar
        //reaches 5, it is complete and the timer is stopped
        ActionListener listener = new ActionListener() {
            int counter = 0;
            public void actionPerformed(ActionEvent ae) {
                counter++;
                progressBar.setValue(counter);
                if (counter==5) {
                    timer.stop();
                    NQueens.setCountOver();
                    JOptionPane.showMessageDialog(null, "TIME'S UP!");
                } 
            }
        };
        
        //Each second, tell the actionlistener to update the progress bar
        timer = new Timer(1000, listener);
        timer.start();
        JOptionPane.showMessageDialog(null, progressBar);
    }
}