package crabfood;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CrabFood {

    public static void main(String[] args) {
        //Main objects for everything
        LinkedList<Restaurant> restaurantList = new LinkedList<>();
        LinkedList<Customer> customerList = new LinkedList<>();
        Generator maker = new Generator();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //look documentation
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CrabFood.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CrabFood.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CrabFood.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(CrabFood.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Read file and create all restaurants
        maker.generateRestaurantList(restaurantList);

        //Map generation
        maker.generateMap(restaurantList);
        //maker.printMap();

        //Customer side
        maker.generateCustomerList(customerList);

        //Start the day
        CrabFood_Frame cfp = new CrabFood_Frame(maker,customerList,restaurantList);

        
    }

}