package crabfood;

import crabfood.event.*;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Generator {

    private Scanner fileInput;
    private char[][] mainMap;
    private final int MAPSIZE = 5;

    /**
     * Creates the list of restaurant from a specified input file.
     *
     * @param restaurantList
     */
    public void generateRestaurantList(LinkedList<Restaurant> restaurantList) {
        try {
            fileInput = new Scanner(new FileInputStream("Input.txt"));
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

    public void generateCustomerList(LinkedList<Customer> customerList) {
        try {
            fileInput = new Scanner(new FileInputStream("Customer.txt"));
            while (fileInput.hasNextLine()) {
                int temp = Integer.parseInt(fileInput.nextLine());
                String temp2 = fileInput.nextLine();
                customerList.addLast(new Customer(temp, temp2));
                while (fileInput.hasNextLine()) {
                    String a = fileInput.nextLine();
                    if (a.isEmpty()) {
                        break;
                    } else {
                        customerList.getLast().addFood(a);
                    }
                }
            }

            fileInput.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found");
        }
    }

    public void startDay(LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList) {

        //Create a priority queue first for the customer to know the order of the event.
        PriorityQueue<Event> eventQueue = new PriorityQueue<>((Event o1, Event o2) -> {
            return o1.getEventTime() - o2.getEventTime();
        });
        final int XPOSITION = 0;
        final int YPOSITION = 0;
        for (int i = 0; i < customerList.size(); i++) {
            Customer custNow = customerList.get(i);
            eventQueue.add(new OrderStartEvent(i + 1, custNow, custNow.getArrivalTime()));

            //Check the restaurant name.
            for (Restaurant res : restaurantList) {
                if (res.getName().equals(custNow.getRestaurantName())) {
                    //Search the nearest distance.
                    int minDist = 1000000000;
                    int xCoord = -1;
                    int yCoord = -1;
                    int index = -1;
                    for (int j = 0; j < res.getBranchTotal(); j++) {
                        int difference = java.lang.Math.abs(XPOSITION - (int) res.getBranch(j).getX())
                                + java.lang.Math.abs(YPOSITION - (int) res.getBranch(j).getY());
                        if (difference < minDist) {
                            xCoord = (int) res.getBranch(j).getX();
                            yCoord = (int) res.getBranch(j).getY();
                            index = j;
                            minDist = difference;
                        }
                    }
                    if (index > -1) {
                        //For now the customer arrival time = order taken time.
                        eventQueue.add(new OrderTakenEvent(res, index, custNow.getArrivalTime()));
                        int prepTime = 0;
                        //Now we calculate the time taken to process the food.
                        for (String foodName : custNow.getFoodList()) {
                            prepTime += res.getPrepTime(foodName);
                        }
                        eventQueue.add(new OrderCookedEvent(res.getName(), xCoord, yCoord, i + 1, custNow.getArrivalTime() + prepTime));

                        //Finally we calculate the time taken to deliver the food.
                        eventQueue.add(new OrderDeliveredEvent(i + 1, custNow.getArrivalTime() + prepTime + minDist));
                    } else {
                        System.err.println("Restaurant Not Found");
                    }
                }
            }
        }

        //Output the events according to the queue.
        System.out.println("0: A new day has started!");
        int eventTime = 0;
        while (!eventQueue.isEmpty()) {
            if (eventQueue.peek().getEventTime() == eventTime) {
                System.out.println(eventTime + ": " + eventQueue.poll());
            } else {
                eventTime++;
            }
        }
    }
}
