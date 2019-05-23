package crabfood;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Ahmad Afiq
 */
public class CrabFood_Title_Content extends JPanel implements ActionListener { //Crab screen saver

    private Image crabfood, crabfoodRed, crabfoodCyan, crabfoodPurple, background;
    private int x = 100, y = 100;
    private Timer t;
    int xV = 1, yV = 1;
    private int roll = 0;
    private int sizeX,sizeY;

    public CrabFood_Title_Content(int width,int height) {
        super.setDoubleBuffered(true); //perfomance of the frame
        super.setBackground(Color.black);
        t = new Timer(7, this);
        t.start();
        this.sizeX = width;
        this.sizeY = height;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
    
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon cf = new ImageIcon("files\\images\\crab_food.png");
        ImageIcon cfr = new ImageIcon("files\\images\\crab_food_red.png");
        ImageIcon cfc = new ImageIcon("files\\images\\crab_food_cyan.png");
        ImageIcon cfp = new ImageIcon("files\\images\\crab_food_purple.png");
        ImageIcon bg = new ImageIcon("files\\images\\background.png");
        crabfood = cf.getImage();
        crabfoodRed = cfr.getImage();
        crabfoodCyan = cfc.getImage();
        crabfoodPurple = cfp.getImage();
        background = bg.getImage();
        Graphics2D g2d = (Graphics2D) g;

        switch (roll) {
            case 0:
                g2d.drawImage(background, 0, 0, this);
                g2d.drawImage(crabfood, x, y, this);
                break;
            case 1:
                g2d.drawImage(background, 0, 0, this);
                g2d.drawImage(crabfoodRed, x, y, this);
                break;
            case 2:
                g2d.drawImage(background, 0, 0, this);
                g2d.drawImage(crabfoodCyan, x, y, this);
                break;
            case 3:
                g2d.drawImage(background, 0, 0, this);
                g2d.drawImage(crabfoodPurple, x, y, this);
                break;
            default:
        }

    }

    public void move() {
        x = x + xV;
        y = y + yV;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(roll); //debug
        move();
        if (x == 0) {
            xV = 1;
            roll = 0;
            repaint();
        } else if (x >=  getSizeX() - 490) { //sizeX*64
            xV = -1;
            roll = 1;
            repaint();
        }
        if (y == 0) {
            yV = 1;
            roll = 2;
            repaint();

        } else if (y >= getSizeY() - 150) { //sizeY*64
            yV = -1;
            roll = 3;
            repaint();

        }
        repaint();

    }
}
