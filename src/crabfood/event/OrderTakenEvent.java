package crabfood.event;

import crabfood.Restaurant;

public class OrderTakenEvent extends Event {

    private Restaurant restaurant;
    private int posX;
    private int posY;

    public OrderTakenEvent(Restaurant restaurant, int index, int eventTime) {
        super.setEventTime(eventTime);
        this.restaurant = restaurant;
        posX = (int)restaurant.getBranch(index).getX();
        posY = (int)restaurant.getBranch(index).getY();
    }

    @Override
    public String toString() {
        String s = "";
        return s;
    }

}
