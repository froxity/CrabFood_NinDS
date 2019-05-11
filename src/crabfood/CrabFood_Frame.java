
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
        JTabbedPane tab = new JTabbedPane();
        //JTabbedPane tabContentMap = new JTabbedPane();
        tab.addTab("Map", new CrabFood_Panel(genMaker));
        tab.addTab("Crab Screen", new CrabFood_Title_Content(genMaker));
        //pass all contents inside crabfood panel into the Windows
        super.add(tab); 
        //super.add(tabContentMap,1); 
        
        super.setTitle("CrabFood MAP"); //Title of the windows
        super.setSize((x * 64)+10, (y * 64)+57); //Create size of the windows (pixel)
        super.setLocation(100, 150); //x and y coordinates for center pop up windows
        super.setResizable(false); //cannot resize the windows
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the program when hit 'X' icon
        super.setVisible(true); //display the windows
        
    }
    
    public void initComponents(){
        
    }

}
