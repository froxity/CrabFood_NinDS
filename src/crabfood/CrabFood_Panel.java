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
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; //for better graphics

        //getting the source image
        ImageIcon roadIcon = new ImageIcon(this.getClass().getResource("road.png")); //grey color
        ImageIcon ccIcon = new ImageIcon(this.getClass().getResource("crusty_crab.png")); //choc color
        ImageIcon pbIcon = new ImageIcon(this.getClass().getResource("phum_bucket.png")); //green color
        ImageIcon bkIcon = new ImageIcon(this.getClass().getResource("burger_krusty.png")); //blue color
        ROAD = roadIcon.getImage();
        CRUSTY_CRAB = ccIcon.getImage();
        PHUM_BUCKET = pbIcon.getImage();
        BURGER_KRUSTY = bkIcon.getImage();
        //end of getting the source image

        /* //debug process
        g2d.drawImage(ROAD, 0, 0, this);
        g2d.drawImage(ROAD, 100, 0, this);
        g2d.drawImage(CRUSTY_CRAB, 0, 100, this);
        g2d.drawImage(PHUM_BUCKET, 0, 200, this);
        g2d.drawImage(PHUM_BUCKET, 0, 300, this);
        g2d.drawImage(PHUM_BUCKET, 0, 400, this);
         */
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
                g2d.drawImage(imgArr[i][j], coordinateX[i][j], coordinateY[i][j], this);
            }
        }
    }

}
