package crabfood.event;

import crabfood.Restaurant;

public class OrderTakenEvent extends Event {

    private Restaurant restaurant;
    private int posX;
    private int posY;

    public OrderTakenEvent(Restaurant restaurant,int posX, int posY, int eventTime, int custNo) {
        super.setEventTime(eventTime, custNo);
        this.restaurant = restaurant;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public String toString() {
        String s = "Branch of " + restaurant.getName()
                + " at (" + posX + ", " + posY + ") take the order.";
        return s;
    }

}
