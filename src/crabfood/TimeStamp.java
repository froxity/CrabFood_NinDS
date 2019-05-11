/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crabfood;

import java.awt.TextArea;
import javax.swing.JPanel;

/**
 *
 * @author User
 */
public class TimeStamp extends JPanel {

    public TimeStamp() {
        TextArea output = new TextArea("Hello World");
        output.setBounds(0, 0, 200, 100);
        output.setEditable(false);
        output.setText("my name is afiq");
        super.add(output);
        //super.
    }
}
