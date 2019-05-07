package crabfood.event;

public class OrderDeliveredEvent extends Event {

    public OrderDeliveredEvent(int custNo, int eventTime) {
        super.setEventTime(eventTime, custNo, 4);
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "The food has been delivered to customer " + super.getCustNo() + ".";
        return s;
    }

}
