package crabfood;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Ahmad Afiq
 */
public class CrabFood_Frame extends JFrame {

    JScrollPane mapScroll = new JScrollPane();
    JLabel mapLabel = new JLabel();

    /**
     * CrabFood_Frame is a windows of CrabFood Application
     *
     * @param genMaker
     */
    public CrabFood_Frame(Generator genMaker,LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList) {
        int x = genMaker.getWidth();
        int y = genMaker.getHeight();
        CrabFood_Panel mapPanel = new CrabFood_Panel(genMaker);
        ReportPanel reportPanel = new ReportPanel(customerList,restaurantList);
        //Create tab pane(space for tab)
        JTabbedPane tab = new JTabbedPane();
        tab.setTabPlacement(JTabbedPane.TOP);
        //---create tab inside tab pane---
        tab.addTab("Crab Screen", new CrabFood_Title_Content(genMaker));

        //-----Create image of Map----- 
        BufferedImage bufImage = new BufferedImage(mapPanel.getSize().width, mapPanel.getSize().height, BufferedImage.TYPE_INT_RGB);
        mapPanel.paint(bufImage.createGraphics());
        try {
            File file = new File("images\\myimage.png");
            ImageIO.write(bufImage, "png", file);
        } catch (Exception ex) {
            System.err.println("File output error");
        }
        //-----create image of map-----

        //--create image tab---
        mapLabel.setIcon(new ImageIcon("images\\myimage.png"));
        mapScroll.setViewportView(mapLabel);
        tab.addTab("Map", mapScroll);
        //--create image tab---
        tab.addTab("Report CrabFood", reportPanel);
        /**
         * pass all contents inside map and screen panel into the Windows and
         * tab into CrabFood_Frame
         */
        super.add(tab);
        super.setTitle("CrabFood"); //Title of the windows
        //super.setSize((x * 64)+10, (y * 64)+57); //Create size of the windows (pixel)
        super.setSize(500, 500); //Create size of the windows (pixel)
        super.setLocation(100, 150); //x and y coordinates for center pop up windows
        super.setResizable(false); //cannot resize the windows
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the program when hit 'X' icon
        super.setVisible(true); //display the windows
        //super.setLocationRelativeTo(null);

    }

}
