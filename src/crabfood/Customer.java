package crabfood;

import java.util.ArrayList;

/**
 * Customer is a class that stores the customer's information. This stores the
 * time of arrival of the customer and the name of the restaurant it wants to go
 * to. The order list is stored as an array list to store multiple food orders
 *
 * @param arrivalTime the time of arrival of the customer
 * @param restaurantName the name of the target restaurant
 * @param foodList the list of food the customer orders from said restaurant
 *
 * @author Cheng Wai Jun
 */
public class Customer {

    private int arrivalTime;
    private String restaurantName;
    private int x;
    private int y;
    private String spReq = "";
    private ArrayList<String> foodList = new ArrayList<>();

    /**
     * Creates a customer that arrives at a certain time and wants to order from
     * a specific restaurant.
     *
     * @param arrivalTime the time of arrival of customer
     * @param x the x-coordinate of customer
     * @param y the y-coordinate of customer
     * @param restaurantName the name of the target restaurant
     */
    public Customer(int arrivalTime, int x, int y, String restaurantName) {
        this.arrivalTime = arrivalTime;
        this.x = x;
        this.y = y;
        this.restaurantName = restaurantName;
        foodList = new ArrayList<>();
    }

    /**
     * Add a food to the order list of the customer.
     *
     * @param foodName the name of food to be added to the customer's order
     * list.
     */
    public void addFood(String foodName) {
        foodList.add(foodName);
    }

    /**
     * Returns the arrival time of the customer.
     *
     * @return arrival time of customer
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Returns the name of the target restaurant of the customer.
     *
     * @return the name of target restaurant
     */
    public String getRestaurantName() {
        return restaurantName;
    }

    /**
     * Returns the entire order list of the customer in the form of an
     * ArrayList.
     *
     * @return the order list of the customer
     */
    public ArrayList<String> getFoodList() {
        return foodList;
    }

    /**
     * Returns the current x-coordinate of the customer
     *
     * @return the x-coordinate of the customer
     */
    public int getX() {
        return x;
    }
    /**
     * Returns the current y-coordinate of the customer
     *
     * @return the y-coordinate of the customer
     */
    public int getY() {
        return y;
    }

    public void setSpReq(String s) {
        spReq = s;
    }

    public String getSpReq() {
        return spReq;
    }

    public boolean hasSpReq(){
        return !spReq.isEmpty();
    }
}
