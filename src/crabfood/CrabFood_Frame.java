
package crabfood;

import javax.swing.JFrame;

/**
 *
 * @author Ahmad Afiq
 */
public class CrabFood_Frame extends JFrame {
    
    /**
     * CrabFood_Frame is a windows of CrabFood Application
     * @param genMaker 
     */
    public CrabFood_Frame(Generator genMaker) {
        int x = genMaker.getWidth();
        int y = genMaker.getHeight();
        
        //pass all contents inside crabfood panel into the Windows
        super.add(new CrabFood_Panel(genMaker)); 
        
        super.setTitle("CrabFood"); //Title of the windows
        super.setSize(x * 100, (y * 100)+30); //Create size of the windows (pixel)
        super.setLocation(400, 150); //x and y coordinates for center pop up windows
        super.setResizable(false); //cannot resize the windows
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the program when hit 'X' icon
        super.setVisible(true); //display the windows
        
    }

}
