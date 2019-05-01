package crabfood;

import crabfood.event.*;
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
                    xMax = tempRest.getBranch(i).getX();
                }
                if (tempRest.getBranch(i).getY() > yMax) {
                    yMax = tempRest.getBranch(i).getY();
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
                mainMap[save.getBranch(i).getX()][save.getBranch(i).getY()] = save.getName().charAt(0);
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

        //Priority follows the least amount of time taken to complete order from start to finish.
        final int XPOSITION = 0;
        final int YPOSITION = 0;
        for (int custIndex = 0; custIndex < customerList.size(); custIndex++) {
            Customer custNow = customerList.get(custIndex);
            eventQueue.add(new OrderStartEvent(custIndex + 1, custNow, custNow.getArrivalTime()));
            //Check the restaurant name.
            for (Restaurant res : restaurantList) {
                if (res.getName().equals(custNow.getRestaurantName())) {
                    //Initialise the time based on the event.
                    int arrivalTime = custNow.getArrivalTime();
                    int distTime = 0;
                    int prepTime = 0;
                    int actualTime = 0;
                    int totalTime = -1;
                    int branchIndex = -1;

                    //Start comparing between branches.
                    for (int currentBranch = 0; currentBranch < res.getBranchTotal(); currentBranch++) {
                        //Calculate distance
                        int currentDistTime = java.lang.Math.abs(XPOSITION - res.getBranch(currentBranch).getX())
                                + java.lang.Math.abs(YPOSITION - res.getBranch(currentBranch).getY());

                        //Calculate cooking time
                        int currentPrepTime = 0;
                        for (String foodName : custNow.getFoodList()) {
                            currentPrepTime += res.getPrepTime(foodName);
                        }

                        //Check if the branch has any previous order.
                        if (res.getBranch(currentBranch).getAvailTime() > actualTime) {
                            actualTime = res.getBranch(currentBranch).getAvailTime();
                        } else {
                            actualTime = arrivalTime;
                        }
                        //Calculate total time
                        if (totalTime == -1 || (actualTime + currentDistTime + currentPrepTime) < totalTime) {
                            distTime = currentDistTime;
                            prepTime = currentPrepTime;
                            totalTime = arrivalTime + distTime + prepTime;
                            branchIndex = currentBranch;
                        }
                    }
                    if (branchIndex == -1) {
                        System.err.println("Error: Branch not found.");
                    } else {
                        res.getBranch(branchIndex).setAvailTime(totalTime);
                        eventQueue.add(new OrderTakenEvent(res, res.getBranch(branchIndex).getX(), res.getBranch(branchIndex).getY(), arrivalTime, custIndex + 1));
                        eventQueue.add(new OrderCookedEvent(res.getName(), res.getBranch(branchIndex).getX(), res.getBranch(branchIndex).getY(), custIndex + 1, arrivalTime + prepTime));
                        eventQueue.add(new OrderDeliveredEvent(custIndex + 1, totalTime));
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
        System.out.println(eventTime + ": All customers are served and shops are closed.");
    }
}
