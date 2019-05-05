package crabfood;

import java.util.LinkedList;
import javax.swing.JFrame;

public class CrabFood extends JFrame {

    public CrabFood(Generator gen) {

    }

    public static void main(String[] args) {
        //Main objects for everything
        LinkedList<Restaurant> restaurantList = new LinkedList<>();
        LinkedList<Customer> customerList = new LinkedList<>();
        LinkedList<crabfood.event.EventLog> eventLogger = new LinkedList<>();
        Generator maker = new Generator();

        //Read file and create all restaurants
        maker.generateRestaurantList(restaurantList);

        //Display info of all restaurants
        for (int i = 0; i < restaurantList.size(); i++) {
            System.out.println(restaurantList.get(i).toString());
        }

        //Map generation
        maker.generateMap(restaurantList);
        maker.printMap();

        CrabFood cf = new CrabFood(maker); //create windows Map

        //Customer side
        maker.generateCustomerList(customerList);

        //Start the day
        maker.startDay(customerList, restaurantList, eventLogger);
    }

}
