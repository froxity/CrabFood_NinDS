package crabfood;

import java.util.LinkedList;

public class CrabFood {

    public static void main(String[] args) {
        //Main objects for everything
        LinkedList<Restaurant> restaurantList = new LinkedList<>();
        Generator gen = new Generator();
        //heklsdfa
        
        //Read file and create all restaurants
        gen.generateRestaurantList(restaurantList);
        
        //Display info of all restaurants
        for(int i = 0; i < restaurantList.size(); i++){
            System.out.println(restaurantList.get(i).toString());
        }
        
        //Map generation
        gen.generateMap(restaurantList);
        gen.printMap();
    }

}
