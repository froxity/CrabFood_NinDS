package crabfood;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Generator is the main class that contains methods that helps generates lists
 * of various objects and files. Basically the backbone for the entire
 * project.<p>
 *
 * The only local variables stored are the {@code fileInput} to read input files
 * and {@code mainMap} to store the map that visualize the locations of
 * restaurants.
 *
 * @param fileInput Scanner class to read various files.
 * @param mainMap multidimensional array to store the map.
 * @author Cheng Wai Jun
 */
public class Generator {

    private Scanner fileInput;
    private char[][] mainMap;
    private int width, height; //for pass value getter

    /**
     * Creates the list of restaurant from a specified input file.
     *
     * @param restaurantList the list of restaurant to fill up
     */
    public void generateRestaurantList(ArrayList<Restaurant> restaurantList) {
        try {
            fileInput = new Scanner(new FileInputStream("files\\input\\Input.txt"));
            while (fileInput.hasNextLine()) {
                //Create new restaurant with name
                restaurantList.add(new Restaurant(fileInput.nextLine()));

                //Create all branches with coordinates
                while (fileInput.hasNextInt()) {
                    restaurantList.get(restaurantList.size() - 1).addBranch(fileInput.nextInt(), fileInput.nextInt());
                }

                //Creates the menu items with time taken to prepare it.
                //Terminates if there's a line break.
                fileInput.nextLine();
                while (fileInput.hasNextLine()) {
                    String temp = fileInput.nextLine();
                    if (temp.isEmpty()) {
                        break;
                    } else {
                        restaurantList.get(restaurantList.size() - 1).addMenuItem(temp, Integer.parseInt(fileInput.nextLine()));
                    }
                }
            }
            fileInput.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found");
        }
    }

    /**
     * Creates the map based on the coordinates of all branches. The size of the
     * map is determined by the furthest restaurant in terms of x and y
     * coordinates.
     *
     * @param restaurantList
     */
    public void generateMap(ArrayList<Restaurant> restaurantList) {
        int xMax = -1;
        int yMax = -1;

        //get max size of the map;
        for (Restaurant tempRest : restaurantList) {
            for (int i = 0; i < tempRest.getBranchTotal(); i++) {
                //get the furthest x-coordinate
                if (tempRest.getBranch(i).getX() > xMax) {
                    xMax = tempRest.getBranch(i).getX();
                }

                //get the furthest y-coordinate
                if (tempRest.getBranch(i).getY() > yMax) {
                    yMax = tempRest.getBranch(i).getY();
                }
            }
        }

        this.width = xMax + 1;
        this.height = yMax + 1;

        //Creates a new map from given max coordinates.
        mainMap = new char[xMax + 1][yMax + 1];

        //Create blank state.
        for (int i = 0; i < mainMap.length; i++) {
            for (int j = 0; j < mainMap[i].length; j++) {
                mainMap[i][j] = '0';
            }
        }

        //Add all restaurant information
        for (Restaurant save : restaurantList) {
            for (int i = 0; i < save.getBranchTotal(); i++) {
                mainMap[save.getBranch(i).getX()][save.getBranch(i).getY()] = save.getName().charAt(0);
            }
        }
    }

    /**
     * Creates the list of customers from a specified input file
     *
     * @param customerList the list of customers to fill up.
     */
    public void generateCustomerList(ArrayList<Customer> customerList) {
        try {
            fileInput = new Scanner(new FileInputStream("files\\input\\Customer.txt"));
            while (fileInput.hasNextLine()) {
                //Get the arrival time of the customer.
                int arrivalTime = Integer.parseInt(fileInput.nextLine());

                //Get the coordinates of the customer.
                int xPos = fileInput.nextInt();
                int yPos = fileInput.nextInt();
                fileInput.nextLine();

                //Get the target restaurant name
                String restName = fileInput.nextLine();

                customerList.add(new Customer(arrivalTime, xPos, yPos, restName));

                //Get the list of ordered food.
                //Terminates if there's a line break.
                while (fileInput.hasNextLine()) {
                    String temp3 = fileInput.nextLine();
                    if (temp3.isEmpty()) {
                        break;
                    } else if (temp3.startsWith("Req:")) {
                        customerList.get(customerList.size() - 1).setSpReq(temp3.replace("Req:", ""));
                    } else {
                        customerList.get(customerList.size() - 1).addFood(temp3);
                    }
                }
            }
            fileInput.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found");
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char[][] getMainMap() {
        return mainMap;
    }
}
