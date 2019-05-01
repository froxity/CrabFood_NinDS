package crabfood.event;

public class OrderCookedEvent extends Event {

    private String restaurantName;
    private int xPos;
    private int yPos;

    public OrderCookedEvent(String restaurantName, int xPos, int yPos, int custNo, int eventTime) {
        super.setEventTime(eventTime, custNo);
        this.restaurantName = restaurantName;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public String toString() {
        String s = "Branch of " + restaurantName + " at (" + xPos + " ," + yPos
                + ") finish the order and delivery the food to customer " + super.getCustNo();
        return s;
    }

}
