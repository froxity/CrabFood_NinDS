package crabfood.event;

public class OrderDeliveredEvent extends Event {

    private int custNo;

    public OrderDeliveredEvent(int custNo, int eventTime) {
        super.setEventTime(eventTime);
        this.custNo = custNo;
    }

    @Override
    public String toString() {
        String s = "";
        s += "The food has been delivered to customer " + custNo;
        return s;
    }

}
