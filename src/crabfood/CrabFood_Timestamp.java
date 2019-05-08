package crabfood;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Ahmad Afiq Azmi
 */
public class CrabFood_Timestamp extends JFrame {

    public CrabFood_Timestamp() {
        JTextArea textArea = new JTextArea(10,20);
        textArea.setDoubleBuffered(true);
        String str = "";
        try {
            Scanner scan = new Scanner(new FileInputStream("timestamp.txt"));
            while (scan.hasNextLine()) {
                str += scan.nextLine() + "\n";
                //textArea.setText(scan.nextLine());
            }
            scan.close();
        } catch (IOException e) {
            System.err.println("File Input Error");
        }
        Font font = new Font("Verdana", Font.PLAIN, 12);
        textArea.setFont(font);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.green);
        textArea.setText(str);
        textArea.setText("hi");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        //super.add(textArea);
        
        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        super.add(scroll);
        super.setTitle("CrabFood Timestamp Event"); //Title of the windows
        super.setSize(500, 300); //Create size of the windows (pixel)
        super.setLocation(620, 150); //x and y coordinates for center pop up windows
        super.setResizable(false); //cannot resize the windows
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the program when hit 'X' icon
        super.setVisible(true); //display the windows
        super.setLocationRelativeTo(null);

    }

}
