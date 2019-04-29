package crabfood;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Generator {

    private Scanner fileInput;
    private char[][] mainMap;
    private final int MAPSIZE = 5;

    public void generateRestaurantList(LinkedList<Restaurant> restaurantList) {
        try {
            fileInput = new Scanner(new FileInputStream("input.txt"));
            while (fileInput.hasNextLine()) {
                //Create new restaurant with name
                restaurantList.addLast(new Restaurant(fileInput.nextLine()));

                //Create all branches with coordinates
                while (fileInput.hasNextInt()) {
                    restaurantList.getLast().addBranch(fileInput.nextInt(), fileInput.nextInt());
                }

                //
                fileInput.nextLine();
                while (fileInput.hasNextLine()) {
                    String a = fileInput.nextLine();
                    if (a.isEmpty()) {
                        break;
                    } else {
                        restaurantList.getLast().addMenuItem(a, Integer.parseInt(fileInput.nextLine()));
                    }
                }
            }
            fileInput.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found");
        }
    }

    public void generateMap(LinkedList<Restaurant> restaurantList) {
        int xMax = -1;
        int yMax = -1;
        
        //get max size of the map;
        for (Restaurant tempRest : restaurantList) {
            for (int i = 0; i < tempRest.getBranchTotal(); i++) {
                if (tempRest.getBranch(i).getX() > xMax) {
                    xMax = (int) tempRest.getBranch(i).getX();
                }
                if (tempRest.getBranch(i).getY() > yMax) {
                    yMax = (int) tempRest.getBranch(i).getY();
                }
            }
        }
        
        mainMap = new char[xMax + 1][yMax + 1];
        //Create blank state.
        for (int i = 0; i < mainMap.length; i++) {
            for (int j = 0; j < mainMap[i].length; j++) {
                mainMap[i][j] = '0';
            }
        }

        //Add all info
        for (Restaurant save : restaurantList) {
            for (int i = 0; i < save.getBranchTotal(); i++) {
                mainMap[(int) save.getBranch(i).getX()][(int) save.getBranch(i).getY()] = save.getName().charAt(0);
            }
        }
    }

    public void printMap() {
        System.out.println("MAP");
        for (int i = 0; i < mainMap.length; i++) {
            for (int j = 0; j < mainMap[i].length; j++) {
                System.out.print(mainMap[i][j]);
            }
            System.out.println("");
        }
    }
}
