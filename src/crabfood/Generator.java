package crabfood;

import crabfood.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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
     * The way this method works is that it checks if there's a customer at a
     * specific timestamp. If there is, it will generate an event using the
     * {@code eventCreator} method with the relevant timestamp for each preceding
     * event that may occur. The console outputs the information every 1 second.
     * <p>
     * Each event is stored in a linked list {@code eventList}. Each time the 
     * loop is done, it will check if the event time matches any of the timestamps
     * before output.
     *
     * @param customerList the list of customers for the day
     * @param restaurantList the available restaurant for the day
     */
    public void startDay(LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList) {

        //Create a priority queue first for the customer to know the order of the event.
        //Order of sorting:
        //EventTime ==> CustNo ==> EventType
        LinkedList<Event> eventList = new LinkedList<>();
        EventLog eventLog = new EventLog();

        //Set all to default values
        for (Restaurant res : restaurantList) {
            for (int i = 0; i < res.getBranchTotal(); i++) {
                res.getBranch(i).setAvailTime(0);
            }
        }

        //Output the events according to the queue.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int eventTime = 0;
            int custNo = 1;
            int custServed = 0;
            int deliveryMen = 1;

            @Override
            public void run() {
                //Begin day
                if (eventTime == 0) {
                    System.out.println("0: A new day has started!");
                }
                //check if the event time reaches customer.
                for (Customer cust : customerList) {
                    if (eventTime == cust.getArrivalTime()) {
                        for (Restaurant res : restaurantList) {
                            if (res.getName().equals(cust.getRestaurantName())) {
                                eventList.add(eventCreator(cust, custNo, res, eventLog));
                                custNo++;
                            }
                        }
                    }
                }
                //Loops through the eventList if there's any match for the specified time
                for (Event event : eventList) {
                    //No event == -1
                    if (event.containsEvent(eventTime) > 0) {
                        //Finished cooking event == 3
                        if (event.containsEvent(eventTime) == 3) {
                            //If there's enough deliverymen, send it off at the same time
                            if (deliveryMen > 0) {
                                deliveryMen--;
                            } 
                            //If not, delay it.
                            else {
                                event.delayOrderDeliverTime();
                            }
                        }
                        
                        //Once reached, the delivery men gets replenished.
                        if (event.containsEvent(eventTime) == 4) {
                            custServed++;
                            deliveryMen++;
                        }
                        System.out.print(event.getEventString(eventTime, deliveryMen));
                    }
                }

                if (custServed == customerList.size()) {
                    System.out.println(eventTime + ": All customers served and shops are closed!\n");
                    for(Event event:  eventList){
                        eventLog.log(event);
                    }
                    System.out.println("RESTAURANT REPORT:");
                    for (Restaurant res : restaurantList) {
                        System.out.println(res.getName());
                        for (int i = 0; i < res.getBranchTotal(); i++) {
                            System.out.println("Branch (" + res.getBranch(i).getX() + ", "
                                    + res.getBranch(i).getY() + ") : " + res.getBranch(i).getBranchOrderComplete());
                        }
                        System.out.println("Total Orders: " + res.getOrderComplete() + "\n");
                    }
                    timer.cancel();
                }
                eventTime++;
            }
        }, 0, 1);
    }

    /**
     * Calculates the probable time stamp for a customer's order.
     * <p>
     * The way this method works is that it calculate each time slot of every
     * event that occurs when a customer comes in. When a customer places the
     * order, it will calculate when will the order be taken, finished cooking
     * and delivered.
     * <p>
     * After calculation, it will return an event object with the relevant info.
     *
     * @return the event with all expected timestamp.
     * @param custCurrent the current customer
     * @param custNo the index of said customer
     * @param resCurrent the current restaurant
     * @param eventQueue the priority queue to be added to
     */
    public Event eventCreator(Customer custCurrent, int custNo, Restaurant resCurrent, EventLog newLog) {
        //Priority follows the least amount of time taken to complete order from start to finish.
        //Get the coordinate of customer.
        int xCustCoord = custCurrent.getX();
        int yCustCoord = custCurrent.getY();
        //Initialise the time based on the event.
        //Use these to store the required values.
        int arrivalTime = custCurrent.getArrivalTime();
        int distanceDuration = 0;
        int cookingDuration = 0;
        int orderTakenTime = 0;
        int totalTime = -1;

        //The index of branch to choose.
        int branchIndex = -1;

        //Start comparing between branches.
        for (int currentBranch = 0; currentBranch < resCurrent.getBranchTotal(); currentBranch++) {
            //Calculate distance from customer. NO I AM NOT DOING PYTHAGORAS
            int tempDistanceDuration = java.lang.Math.abs(xCustCoord - resCurrent.getBranch(currentBranch).getX())
                    + java.lang.Math.abs(yCustCoord - resCurrent.getBranch(currentBranch).getY());

            //Calculate total time to cook the dish
            int tempCookingDuration = 0;
            for (String foodName : custCurrent.getFoodList()) {
                tempCookingDuration += resCurrent.getPrepTime(foodName);
            }

            //Check if the branch has any previous order before moving to next one
            //If yes, then it will only begin cooking when it delivers the previous one
            //If not, then it will start on the spot.
            int tempOrderTakenTime;
            if (resCurrent.getBranch(currentBranch).getAvailTime() > arrivalTime) {
                tempOrderTakenTime = resCurrent.getBranch(currentBranch).getAvailTime();
            } else {
                tempOrderTakenTime = arrivalTime;
            }

            //Calculate total time. Store the value if the total time is lower.
            if (totalTime == -1 || (tempOrderTakenTime + tempDistanceDuration + tempCookingDuration) < totalTime) {
                orderTakenTime = tempOrderTakenTime;
                distanceDuration = tempDistanceDuration;
                cookingDuration = tempCookingDuration;
                totalTime = orderTakenTime + distanceDuration + cookingDuration;
                branchIndex = currentBranch;
            }
        }

        //Branch will not take more orders until other order is finished.
        resCurrent.getBranch(branchIndex).setAvailTime(orderTakenTime + cookingDuration);
        resCurrent.orderComplete();
        resCurrent.getBranch(branchIndex).branchOrderComplete();

        Event event = new Event(custNo, custCurrent, resCurrent, branchIndex, arrivalTime, orderTakenTime + cookingDuration,
                orderTakenTime + cookingDuration, totalTime);
        /*newLog.log(custNo, arrivalTime, orderTakenTime + cookingDuration, distanceDuration, resCurrent.getName(), resCurrent.getBranch(branchIndex).getX(), 
                resCurrent.getBranch(branchIndex).getY(), custCurrent.getFoodList(), custCurrent.getSpReq());*/
        
        return event;
    }
}
