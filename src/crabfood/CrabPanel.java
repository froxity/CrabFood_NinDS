package crabfood;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

public class CrabPanel extends JPanel implements Runnable {

    //Double buffering
    private Image dbImage;
    private Graphics dbg;

    //JPanel variables
    static final int GWIDTH = 500, GHEIGHT = 500;
    static final Dimension gameDim = new Dimension(GWIDTH, GHEIGHT);

    //Window variables
    private Thread game;
    private volatile boolean running = false;

    //CrabWorld Objects
    CrabWorld world;

    public CrabPanel(Generator gen){
        world = new CrabWorld(gen);
        setPreferredSize(gameDim);
        setBackground(Color.WHITE);
        setFocusable(true);
        requestFocus();
    }
    
    @Override
    public void run() {
        mapRender();
        paintScreen();
    }

    public void mapRender(){
        if (dbImage == null) { //create the buffer
            dbImage = createImage(GWIDTH, GHEIGHT);
            if (dbImage == null) {
                System.err.println("dbImage is still null");
                return;
            } else {
                dbg = dbImage.getGraphics();
            }
        }
        //clear the screen
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0, 0, GWIDTH, GHEIGHT);

        //draw game elements
        draw(dbg);
    }
    
    public void paintScreen() {
        Graphics g;
        try {
            g = this.getGraphics();
            if (dbImage != null && g != null) {
                g.drawImage(dbImage, 0, 0, null);
            }
            Toolkit.getDefaultToolkit().sync(); //for some OS
            //g.dispose();
        } catch (Exception e) {
            System.err.println("e");
        }
    }
    
    public void draw(Graphics g){
        world.draw(g);
    }
}
