package crabfood;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Get the screensize of witdth and height
        int x = genMaker.getWidth();
        int y = genMaker.getHeight();
        CrabFood_Panel mapPanel = new CrabFood_Panel(genMaker);
        ReportPanel reportPanel = new ReportPanel(customerList,restaurantList,screenSize.width,screenSize.height);
        //Create tab pane(space for tab)
        JTabbedPane tab = new JTabbedPane();
        tab.setTabPlacement(JTabbedPane.LEFT);
        //---create tab inside tab pane---
        
        //(1ST TAB)
        tab.addTab("",new ImageIcon("images\\tabIcon_home.png"), reportPanel); //REPORT CRABFOOD PANEL
        //tab.addTab("huhu", new ImageIcon("images\\crab_food_logo.png"), mapPanel); //contoh

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
        mapLabel.setLocation(0, 0);
        mapScroll.setViewportView(mapLabel);
        mapScroll.getVerticalScrollBar().setUnitIncrement(16);
        mapScroll.getHorizontalScrollBar().setUnitIncrement(32);
        //(2ND TAB)
        tab.addTab("",new ImageIcon("images\\tabIcon_map.png"), mapScroll); //MAP PANEL
        //--create image tab---
        //(3RD TAB)
        tab.addTab("",new ImageIcon("images\\tabIcon_SS.png"), new CrabFood_Title_Content(screenSize.width,screenSize.height));//ScreenSaver
        
        /**
         * pass all contents inside map and screen panel into the Windows and
         * tab into CrabFood_Frame
         */
        super.add(tab);
        super.setTitle("CrabFood"); //Title of the windows
        super.setBounds(0, 0, screenSize.width, screenSize.height); //Create size of the windows (pixel)
        super.setResizable(true); //cannot resize the windows
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the program when hit 'X' icon
        super.setVisible(true); //display the windows

    }

}
