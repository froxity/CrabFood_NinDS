
package crabfood;

import javax.swing.*;

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
        
        //Create tab pane(space for tab)
        JTabbedPane tab = new JTabbedPane();
        //create multiple tab inside tab pane
        tab.addTab("Map", new CrabFood_Panel(genMaker));
        tab.addTab("Crab Screen", new CrabFood_Title_Content(genMaker));
        tab.addTab("TimeStamp", new TimeStamp());
        /**
         * pass all contents inside map and screen panel into the Windows
         * and tab into CrabFood_Frame
         */
        super.add(tab); 
        super.setTitle("CrabFood"); //Title of the windows
        super.setSize((x * 64)+10, (y * 64)+57); //Create size of the windows (pixel)
        super.setLocation(100, 150); //x and y coordinates for center pop up windows
        super.setResizable(false); //cannot resize the windows
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the program when hit 'X' icon
        super.setVisible(true); //display the windows
        
    }
    
    public void initComponents(){
        
    }

}
