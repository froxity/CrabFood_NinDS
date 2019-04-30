
package crabfood;

import java.util.ArrayList;

public class Customer{
    private int arrivalTime;
    private int totalTime;
    private String restaurantName;
    private ArrayList<String> foodList;
    
    public Customer(int arrivalTime, String restaurantName){
        this.arrivalTime = arrivalTime;
        this.restaurantName = restaurantName;
        foodList = new ArrayList<>();
    }
    
    public void addFood(String foodName){
        foodList.add(foodName);
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public ArrayList<String> getFoodList() {
        return foodList;
    }
    
    
}
