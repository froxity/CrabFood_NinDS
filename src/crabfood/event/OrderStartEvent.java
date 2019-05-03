package crabfood.event;

import crabfood.Customer;

public class OrderStartEvent extends Event {

    private Customer customer;

    public OrderStartEvent(int custNo, Customer customer, int eventTime) {
        this.customer = customer;
        super.setEventTime(eventTime, custNo);
    }

    @Override
    public String toString() {
        String s = "";
        s += "Customer " + super.getCustNo();
        s += " at (" + customer.getX() + ", " + customer.getY() + ")";
        s += " wants to order ";
        for (int i = 0; i < customer.getFoodList().size(); i++) {
            s += customer.getFoodList().get(i) + " ";
            if (i < customer.getFoodList().size() - 1) {
                s += ", ";
            }
        }
        s += "from " + customer.getRestaurantName() + ".";
        return s;
    }

}
