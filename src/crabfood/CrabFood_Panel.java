package crabfood;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Ahmad Afiq
 */
public class CrabFood_Panel extends JPanel {

    private Image ROAD, CRUSTY_CRAB, PHUM_BUCKET, BURGER_KRUSTY;
    private Image[][] imgArr;
    private int[][] coordinateX;
    private int[][] coordinateY;
    private char[][] map;
    private int X, Y;

    public CrabFood_Panel(Generator genMaker) {
        this.X = genMaker.getWidth(); //get the size of map
        this.Y = genMaker.getHeight(); //get the size of map
        imgArr = new Image[genMaker.getWidth()][genMaker.getHeight()]; //array of Image for the Map
        coordinateX = new int[genMaker.getWidth()][genMaker.getHeight()]; //array of coordinate x
        coordinateY = new int[genMaker.getWidth()][genMaker.getHeight()]; //array of coordinate y
        map = genMaker.getMainMap();
        super.setDoubleBuffered(true); //perfomance of the frame
        super.setSize((X * 64), (Y * 64));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; //for better graphics

        //getting the source image
        ImageIcon roadIcon = new ImageIcon("files\\images\\road2.png"); //grey color
        ImageIcon ccIcon = new ImageIcon("files\\images\\crusty_crab.png", "Crusty Crab"); //choc color
        ImageIcon pbIcon = new ImageIcon("files\\images\\phum_bucket.png", "Phum Bucket"); //green color
        ImageIcon bkIcon = new ImageIcon("files\\images\\burger_krusty.png", "Burger Krusty"); //blue color
        ROAD = roadIcon.getImage();
        CRUSTY_CRAB = ccIcon.getImage();
        PHUM_BUCKET = pbIcon.getImage();
        BURGER_KRUSTY = bkIcon.getImage();
        //end of getting the source image

        //Load the map into array
        int xParameter = 0, yParameter = 0;
        for (int i = 0; i < map.length; i++) {
            xParameter = 0;
            for (int j = 0; j < map[i].length; j++) {
                switch (map[i][j]) {
                    case '0':
                        imgArr[i][j] = ROAD;
                        coordinateX[i][j] = xParameter * 64;
                        coordinateY[i][j] = yParameter * 64;
                        break;
                    case 'C':
                        imgArr[i][j] = CRUSTY_CRAB;
                        coordinateX[i][j] = xParameter * 64;
                        coordinateY[i][j] = yParameter * 64;
                        break;
                    case 'P':
                        imgArr[i][j] = PHUM_BUCKET;
                        coordinateX[i][j] = xParameter * 64;
                        coordinateY[i][j] = yParameter * 64;
                        break;
                    case 'B':
                        imgArr[i][j] = BURGER_KRUSTY;
                        coordinateX[i][j] = xParameter * 64;
                        coordinateY[i][j] = yParameter * 64;
                        break;
                    default:
                }
                xParameter++;
            }
            yParameter++;
        }

        //draw the map
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                g2d.drawImage(imgArr[i][j], coordinateY[i][j], coordinateX[i][j], this);
            }
        }
    }

}
