 package crabfood.event;

public class OrderDeliveredEvent extends Event {
    
    public OrderDeliveredEvent(int custNo, int eventTime) {
        super.setEventTime(eventTime, custNo);
    }

    @Override
    public String toString() {
        String s = "";
        s += "The food has been delivered to customer " + super.getCustNo() + ".";
        return s;
    }

}
