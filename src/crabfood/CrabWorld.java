package crabfood;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class CrabWorld {

    private Rectangle[] blocks;
    private Image[] blockImg;
    private final int arrayNum = 25; //500 (5x5)
    private int x, y;

    //Block images
    private Image ROAD, CRUSTY_CRAB, PHUM_BUCKET, BURGER_KRUSTY;

    public CrabWorld(Generator gen) {
        Generator map = gen;
        ROAD = new ImageIcon("C:/Users/User/Documents/NetBeansProjects/CrabFood/images/road.png").getImage();
        CRUSTY_CRAB = new ImageIcon("C:/Users/User/Documents/NetBeansProjects/CrabFood/images/crusty_crab.png").getImage();
        PHUM_BUCKET = new ImageIcon("C:/Users/User/Documents/NetBeansProjects/CrabFood/images/phum_bucket.png").getImage();
        BURGER_KRUSTY = new ImageIcon("C:/Users/User/Documents/NetBeansProjects/CrabFood/images/burger_krusty.jpg").getImage();

        blocks = new Rectangle[25];
        blockImg = new Image[25];
        char[][] mainMap = map.getMainMap();
        loadArrays(mainMap);
    }
    
    //debug
    public void loadArrays(char[][] map) {
        ArrayList<Character> temp_map = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                //System.out.println(map[i][j]); //debug
                temp_map.add(map[i][j]);
            }
        }
        for (int i = 0; i < arrayNum; i++) {
            if (x >= 25) {
                x = 0;
                y += 20;
            }
            System.out.print(temp_map.get(i));
            char tile = temp_map.get(i);
            switch (tile) {
                case '0': //road
                    blockImg[i] = ROAD;
                    blocks[i] = new Rectangle(x, y, 100, 100);
                    break;
                case 'C': //Crusty Crab
                    blockImg[i] = CRUSTY_CRAB;
                    blocks[i] = new Rectangle(x, y, 100, 100);
                    break;
                case 'P'://Phum Bucket
                    blockImg[i] = PHUM_BUCKET;
                    blocks[i] = new Rectangle(x, y, 100, 100);
                    break;
                case 'B'://Burger Krusty
                    blockImg[i] = BURGER_KRUSTY;
                    blocks[i] = new Rectangle(x, y, 100, 100);
                    break;
                default:
                    break;
            }
            x += 20;

        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < arrayNum; i++) {
            g.drawImage(blockImg[i], blocks[i].x, blocks[i].y, null);
        }
    }
}
