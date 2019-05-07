package crabfood.event;

public class OrderCookedEvent extends Event {

    private String restaurantName;
    private int xPos;
    private int yPos;
    private boolean late = false;

    public OrderCookedEvent(String restaurantName, int xPos, int yPos, int custNo, int eventTime) {
        super.setEventTime(eventTime, custNo,3);
        this.restaurantName = restaurantName;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public String toString() {
        String s = "Branch of " + restaurantName + " at (" + xPos + " ," + yPos
                + ") finish the order for customer " + super.getCustNo();
        if(late){
            s += "Will be late. ";
        }
        return s;
    }

}
