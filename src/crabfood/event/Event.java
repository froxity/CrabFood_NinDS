package crabfood.event;

import crabfood.Customer;
import crabfood.Restaurant;

public class Event {

    private int custNo;
    private Customer customer;
    private Restaurant restaurant;
    private int branchIndex;

    private int orderArrivalTime;
    private int orderCookedTime;
    private int orderDeliverTime;
    private int orderReachTime;

    private boolean deliveryStart = false;

    public Event(int custNo, Customer customer, Restaurant restaurant, int branchIndex, int orderArrivalTime, int orderCookedTime, int orderDeliverTime, int orderReachTime) {
        this.custNo = custNo;
        this.customer = customer;
        this.restaurant = restaurant;
        this.branchIndex = branchIndex;
        this.orderArrivalTime = orderArrivalTime;
        this.orderCookedTime = orderCookedTime;
        this.orderDeliverTime = orderDeliverTime;
        this.orderReachTime = orderReachTime;
    }

    public void delayOrderDeliverTime() {
        this.orderDeliverTime++;
        this.orderReachTime++;
    }

    public int getCustNo() {
        return custNo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public int getOrderTakenTime() {
        return orderArrivalTime;
    }

    public int getOrderCookedTime() {
        return orderCookedTime;
    }

    public int getOrderDeliverTime() {
        return orderDeliverTime;
    }

    public int getOrderReachTime() {
        return orderReachTime;
    }

    public int containsEvent(int eventTime) {
        if (eventTime == orderArrivalTime) {
            return 1;
        } else if (eventTime == orderCookedTime && !deliveryStart) {
            deliveryStart = true;
            return 2;
        } else if (eventTime == orderDeliverTime) {
            return 3;
        } else if (eventTime == orderReachTime) {
            return 4;
        } else {
            return -1;
        }
    }

    public String getEventString(int eventTime, int deliveryMen) {
        String s = "";
        if (eventTime == orderArrivalTime) {
            s += orderArrivalTime + ": Customer " + custNo + " at (" + customer.getX() + ", " + customer.getY() + ")";
            s += " wants to order ";
            for (int i = 0; i < customer.getFoodList().size(); i++) {
                s += customer.getFoodList().get(i) + " ";
                if (i < customer.getFoodList().size() - 1) {
                    s += ", ";
                }
            }
            s += "from " + customer.getRestaurantName() + ". ";
            if (customer.hasSpReq()) {
                s += "Special Request: " + customer.getSpReq();
            }
            s += "\n" + orderArrivalTime + ": Branch of " + restaurant.getName() + " at (" + restaurant.getBranch(branchIndex).getX() + ", "
                    + restaurant.getBranch(branchIndex).getY() + ") take the order.\n";
        }
        if (eventTime == orderCookedTime) {
            s += orderCookedTime + ": Branch of " + restaurant.getName() + " at (" + restaurant.getBranch(branchIndex).getX() + " ,"
                    + restaurant.getBranch(branchIndex).getY() + ") finish the order for customer " + custNo + ".\n";
        }
        if (eventTime == orderDeliverTime) {
            s += orderDeliverTime + ": Food for customer " + custNo + " is on the way from " + restaurant.getName()
                    + " at (" + restaurant.getBranch(branchIndex).getX() + " ," + restaurant.getBranch(branchIndex).getY() + "). \n";
        }
        if (eventTime == orderReachTime) {
            s += orderReachTime + ": The food has been delivered to customer " + custNo + ".\n";
        }
        return s;
    }
}