/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crabfood;

import javax.swing.JFrame;

/**
 *
 * @author Ahmad Afiq
 */
public class CrabFood_Title extends JFrame {

    public CrabFood_Title() {
        
        //pass all contents inside crabfood panel into the Windows
        super.add(new CrabFood_Title_Content()); 
        
        super.setTitle("CrabFood"); //Title of the windows
        super.setSize(720, 480); //Create size of the windows (pixel)
        super.setLocation(620, 150); //x and y coordinates for center pop up windows
        super.setResizable(false); //cannot resize the windows
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the program when hit 'X' icon
        super.setVisible(true); //display the windows
    }
}
