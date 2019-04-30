package crabfood.event;

import crabfood.Restaurant;

public class OrderTakenEvent extends Event {

    private Restaurant restaurant;
    private int posX;
    private int posY;

    public OrderTakenEvent(Restaurant restaurant, int branchIndex, int eventTime) {
        super.setEventTime(eventTime);
        this.restaurant = restaurant;
        posX = (int)restaurant.getBranch(branchIndex).getX();
        posY = (int)restaurant.getBranch(branchIndex).getY();
    }

    @Override
    public String toString() {
        String s = "Branch of " + restaurant.getName()
                + " at (" + posX + ", " + posY + ") take the order.";
        return s;
    }

}
