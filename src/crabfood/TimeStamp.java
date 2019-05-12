/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crabfood;

import crabfood.event.*;
import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

/**
 *
 * @author User
 */
public class TimeStamp extends JFrame {
    
    JTextArea textArea = new JTextArea();
    private LinkedList<Customer> CList;
    private LinkedList<Restaurant> RList;
    
    public TimeStamp(LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList) {
        this.CList = customerList;
        this.RList = restaurantList;
        startDay(CList, RList);
        //super.add(scroll);
        super.setTitle("CrabFood Timestamp Event"); //Title of the windows
        super.setSize(500, 300); //Create size of the windows (pixel)
        super.setLocation(620, 150); //x and y coordinates for center pop up windows
        super.setResizable(false); //cannot resize the windows
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close the program when hit 'X' icon
        super.setVisible(true); //display the windows
        //super.setLocationRelativeTo(null);
    }
    
    public void startDay(LinkedList<Customer> customerList, LinkedList<Restaurant> restaurantList) {
        //Setting for textArea----
        //textArea.setDoubleBuffered(true);
        Font font = new Font("Verdana", Font.PLAIN, 12);
        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textArea.setSize(480, 300);
        textArea.setFont(font);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.green);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        
        super.add(scroll);
        //---Setting for textArea

        //Create a priority queue first for the customer to know the order of the event.
        //Order of sorting:
        //EventTime ==> CustNo ==> EventType
        LinkedList<crabfood.event.Event> eventList = new LinkedList<>();
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
                    //System.out.println("0: A new day has started!");
                    textArea.append("0: A new day has started!\n");
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
                for (crabfood.event.Event event : eventList) {
                    //No event == -1
                    if (event.containsEvent(eventTime) > 0) {
                        //Finished cooking event == 3
                        if (event.containsEvent(eventTime) == 3) {
                            //If there's enough deliverymen, send it off at the same time
                            if (deliveryMen > 0) {
                                deliveryMen--;
                            } //If not, delay it.
                            else {
                                event.delayOrderDeliverTime();
                            }
                        }

                        //Once reached, the delivery men gets replenished.
                        if (event.containsEvent(eventTime) == 4) {
                            custServed++;
                            deliveryMen++;
                        }
                        String str = event.getEventString(eventTime, deliveryMen);
                        //System.out.print(str);
                        textArea.append(str);
                        
                    }
                }
                
                if (custServed == customerList.size()) {
                    String str = eventTime + ": All customers served and shops are closed!";
                    //System.out.println(str+"\n");
                    textArea.append(str);
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
        }, 1, 1000);
    }
    
    public crabfood.event.Event eventCreator(Customer custCurrent, int custNo, Restaurant resCurrent, EventLog newLog) {
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
        
        crabfood.event.Event event = new crabfood.event.Event(custNo, custCurrent, resCurrent, branchIndex, arrivalTime, orderTakenTime + cookingDuration,
                orderTakenTime + cookingDuration, totalTime);
        newLog.log(custNo, arrivalTime, orderTakenTime + cookingDuration, distanceDuration, resCurrent.getName(), resCurrent.getBranch(branchIndex).getX(), resCurrent.getBranch(branchIndex).getY(), custCurrent.getFoodList(), custCurrent.getSpReq());
        
        return event;
    }
}
