package crabfood.event;

public class OrderCookedEvent extends Event {

    private String restaurantName;
    private int xPos;
    private int yPos;
    private int custNo;

    public OrderCookedEvent(String restaurantName, int xPos, int yPos, int custNo, int eventTime) {
        super.setEventTime(eventTime);
        this.restaurantName = restaurantName;
        this.xPos = xPos;
        this.yPos = yPos;
        this.custNo = custNo;
    }

    @Override
    public String toString() {
        String s = "Branch of " + restaurantName + " at (" + xPos + " ," + yPos
                + ") finish the order and delivery the food to customer " + custNo;
        return s;
    }

}
