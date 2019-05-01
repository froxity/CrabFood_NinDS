package crabfood;

import java.util.ArrayList;

public class Customer {

    private int arrivalTime;
    private String restaurantName;
    private ArrayList<String> foodList = new ArrayList<>();

    /**
     * Creates a customer that arrives at a certain time and wants to order from
     * a specific restaurant.
     *
     * @param arrivalTime the time of arrival of customer
     * @param restaurantName the name of the target restaurant
     */
    public Customer(int arrivalTime, String restaurantName) {
        this.arrivalTime = arrivalTime;
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
     *Returns the name of the target restaurant of the customer.
     * @return the name of target restaurant
     */
    public String getRestaurantName() {
        return restaurantName;
    }

    /**
     * Returns the entire order list of the customer in the
     * form of an ArrayList.
     * @return the order list of the customer
     */
    public ArrayList<String> getFoodList() {
        return foodList;
    }

}
