package crabfood.event;

import crabfood.Customer;

public class OrderStartEvent extends Event {

    private int custNo;
    private Customer customer;

    public OrderStartEvent(int custNo, Customer customer) {
        this.custNo = custNo;
        this.customer = customer;
        super.setPriorityTime();
    }

    @Override
    public String toString() {
        String s = "";
        s += "Customer " + custNo;
        s += " wants to order ";
        for (int i = 0; i < customer.getFoodList().size(); i++) {
            s += customer.getFoodList().get(i) + " ";
            if (i < customer.getFoodList().size() - 1) {
                s += ", ";
            }
        }
        s += " from " + customer.getRestaurantName();
        return s;
    }

}
