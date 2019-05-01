package crabfood;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class CrabFood {

    public static void main(String[] args) {
        //Testing 12334
        //Main objects for everything
        LinkedList<Restaurant> restaurantList = new LinkedList<>();
        LinkedList<Customer> customerList = new LinkedList<>();
        Generator maker = new Generator();
        
        //Read file and create all restaurants
        maker.generateRestaurantList(restaurantList);
        
        //Display info of all restaurants
        for(int i = 0; i < restaurantList.size(); i++){
            System.out.println(restaurantList.get(i).toString());
        }
        
        //Map generation
        maker.generateMap(restaurantList);
        maker.printMap();
        
        //Customer side
        maker.generateCustomerList(customerList);
    }

}
