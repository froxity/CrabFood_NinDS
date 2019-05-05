package crabfood;

import crabfood.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Generator is the main class that contains methods that helps generates lists
 * of various objects and files. Basically the backbone for the entire
 * project.<p>
 *
 * The only local variables stored are the {@code fileInput} to read input files
 * and {@code mainMap} to store the map that visualise the locations of
 * restaurants.
 *
 * @param fileInput Scanner class to read various files.
 * @param mainMap multidimensional array to store the map.
 * @author Cheng Wai Jun
 */
public class Generator {

    private Scanner fileInput;
    private char[][] mainMap;
    private final int MAPSIZE = 5;

    /**
     * Creates the list of restaurant from a specified input file.
     *
     * @param restaurantList the list of restaurant to fill up
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

                //Creates the menu items with time taken to prepare it.
                //Terminates if there's a line break.
                fileInput.nextLine();
                while (fileInput.hasNextLine()) {
                    String temp = fileInput.nextLine();
                    if (temp.isEmpty()) {
                        break;
                    } else {
                        restaurantList.getLast().addMenuItem(temp, Integer.parseInt(fileInput.nextLine()));
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
    public void generateMap(LinkedList<Restaurant> restaurantList) {
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
     * Displays the created map.
     */
    public void printMap() {
        System.out.println("MAP");
        for (int i = 0; i < mainMap.length; i++) {
            for (int j = 0; j < mainMap[i].length; j++) {
                System.out.print(mainMap[i][j]);
            }
            System.out.println("");
        }
    }

    /**
     * Creates the list of customers from a specified input file
     *
     * @param customerList the list of customers to fill up.
     */
    public void generateCustomerList(LinkedList<Customer> customerList) {
        try {
            fileInput = new Scanner(new FileInputStream("Customer.txt"));
            while (fileInput.hasNextLine()) {
                //Get the arrival time of the customer.
                int arrivalTime = Integer.parseInt(fileInput.nextLine());

                //Get the coordinates of the customer.
                int xPos = fileInput.nextInt();
                int yPos = fileInput.nextInt();
                fileInput.nextLine();

                //Get the target restaurant name
                String restName = fileInput.nextLine();

                customerList.addLast(new Customer(arrivalTime, xPos, yPos, restName));

                //Get the list of ordered food.
                //Terminates if there's a line break.
                while (fileInput.hasNextLine()) {
                    String temp3 = fileInput.nextLine();
                    if (temp3.isEmpty()) {
                        break;
                    } else if (temp3.startsWith("Req:")) {
                        customerList.getLast().setSpReq(temp3.replace("Req:", ""));
                    } else {
                        customerList.getLast().addFood(temp3);
                    }
                }
            }
            fileInput.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found");
        }
    }

    /**
     * Literally starts the business with a set of customers. This will
     * continuously run until all customer orders are fulfilled.
     * <p>
     * The way this method works is that it calculate each time slot of every
     * event that occurs when a customer comes in. When a customer places the
     * order, it will calculate when will the order be taken, finished cooking
     * and delivered.
     * <p>
     * Each event is stored in a priority queue {@code eventQueue} which can
     * sort the events based on the time stamp of the events.
     * <p>
     * After calculating everything, it will start polling from the queue until
     * it is empty.
     *
     * @param customerList the list of customers for the day
     * @param restaurantList the available restaurant for the day
     * @param eventLogger FEATURE IN PROGRESS supposed to record events in
     * another file
     */
    public void startDay(LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList, LinkedList<EventLog> eventLogger) {

        //Create a priority queue first for the customer to know the order of the event.
        PriorityQueue<Event> eventQueue = new PriorityQueue<>((Event o1, Event o2) -> {
            return o1.getEventTime() - o2.getEventTime();
        });

        //Priority follows the least amount of time taken to complete order from start to finish.
        //Begin iterating through the list of customers
        for (int custIndex = 0; custIndex < customerList.size(); custIndex++) {
            Customer custNow = customerList.get(custIndex);
            //Get the coordinate of customer.
            int xCustCoord = customerList.get(custIndex).getX();
            int yCustCoord = customerList.get(custIndex).getY();

            //Event 1: Customer orders the food.
            eventQueue.add(new OrderStartEvent(custIndex + 1, custNow, custNow.getArrivalTime()));

            //Check the restaurant name.
            for (Restaurant res : restaurantList) {
                if (res.getName().equals(custNow.getRestaurantName())) {
                    //Initialise the time based on the event.
                    //Use these to store the required values.
                    int arrivalTime = custNow.getArrivalTime();
                    int distTime = 0;
                    int prepTime = 0;
                    int actualTime = 0;
                    int totalTime = -1;

                    //The index of branch to choose.
                    int branchIndex = -1;

                    //Start comparing between branches.
                    for (int currentBranch = 0; currentBranch < res.getBranchTotal(); currentBranch++) {
                        //Calculate distance from customer. NO I AM NOT DOING PYTHAGORAS
                        int currentDistTime = java.lang.Math.abs(xCustCoord - res.getBranch(currentBranch).getX())
                                + java.lang.Math.abs(yCustCoord - res.getBranch(currentBranch).getY());

                        //Calculate total time to cook the dish
                        int currentPrepTime = 0;
                        for (String foodName : custNow.getFoodList()) {
                            currentPrepTime += res.getPrepTime(foodName);
                        }

                        //Check if the branch has any previous order before moving to next one
                        //If yes, then it will only begin cooking when it delivers the previous one
                        //If not, then it will start on the spot.
                        if (res.getBranch(currentBranch).getAvailTime() > actualTime) {
                            actualTime = res.getBranch(currentBranch).getAvailTime();
                        } else {
                            actualTime = arrivalTime;
                        }

                        //Calculate total time. Store the value if the total time is lower.
                        if (totalTime == -1 || (actualTime + currentDistTime + currentPrepTime) < totalTime) {
                            distTime = currentDistTime;
                            prepTime = currentPrepTime;
                            totalTime = actualTime + distTime + prepTime;
                            branchIndex = currentBranch;
                        }
                    }
                    if (branchIndex == -1) {
                        System.err.println("Error: Branch not found.");
                    } else {
                        //Branch will not take more orders until other order is finished.
                        res.getBranch(branchIndex).setAvailTime(totalTime);

                        //Event 2: Branch takes the order (should be at the same time as when customer places it).
                        eventQueue.add(new OrderTakenEvent(res, res.getBranch(branchIndex).getX(), res.getBranch(branchIndex).getY(), arrivalTime, custIndex + 1));

                        //Event 3: Branch finish cooking the food.
                        eventQueue.add(new OrderCookedEvent(res.getName(), res.getBranch(branchIndex).getX(), res.getBranch(branchIndex).getY(), custIndex + 1, arrivalTime + prepTime));

                        //Event 4: Branch delivered the food.
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
