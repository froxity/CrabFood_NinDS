package crabfood.event;

import crabfood.Customer;
import crabfood.Restaurant;

public class OrderTakenEvent extends Event {

    private Restaurant restaurant;
    private int posX;
    private int posY;

    public OrderTakenEvent(Restaurant restaurant, int index) {
        this.restaurant = restaurant;
        posX = (int)restaurant.getBranch(index).getX();
        posY = (int)restaurant.getBranch(index).getY();
    }

    @Override
    public String toString() {
        
    }

}
