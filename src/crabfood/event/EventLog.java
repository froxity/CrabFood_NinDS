package crabfood.event;

import crabfood.Restaurant;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class EventLog {

    private LinkedList<Event> eventList = new LinkedList<>();
    private String strDate;

    public EventLog() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a, E dd/MM/yyyy");
        strDate = dateFormat.format(date);
    }

    public void startLog() {
        try {
            PrintWriter log = new PrintWriter(new FileOutputStream("files\\logs\\eventlog.txt", true));
            log.println();
            log.println("Open Time: " + strDate);
            log.println(String.format("|%-9s|%-9s|%-8s|Order Time |Finished Cooking Time |Delivery Time |Total Time |%-20s|Branch  |%-50s|%-30s|", "Customer", "Location", "Arrival", "Restaurant", "Food Ordered", "Special Request"));
            for (Event event : eventList) {
                int deliveryTime = java.lang.Math.abs(event.getCustomer().getX() - event.getBranch().getX())
                        + java.lang.Math.abs(event.getCustomer().getY() - event.getBranch().getY());
                String str = String.format("|%-9d|(%-2d,%-2d)  |%-8d|%-11d|%-22d|%-14d|%-11d|%-20s|(%-2d,%-2d) |%-50s|%-30s|",
                        event.getCustNo(),
                        event.getCustomer().getX(),
                        event.getCustomer().getY(),
                        event.getOrderTakenTime(),
                        event.getOrderTakenTime(),
                        event.getOrderCookedTime(),
                        deliveryTime,
                        (event.getOrderCookedTime() - event.getOrderTakenTime() + deliveryTime),
                        event.getRestaurant().getName(),
                        event.getBranch().getX(),
                        event.getBranch().getY(),
                        event.getCustomer().getFoodList(),
                        event.getCustomer().getSpReq());
                log.println(str);
            }
            log.println();
            log.close();
        } catch (IOException e) {
            System.out.println("Log error");
        }
    }

    public void addToList(Event event) {
        eventList.add(event);
    }

    public void logRestaurant(Restaurant res) {
        try {
            PrintWriter log = new PrintWriter(new FileOutputStream("files\\logs\\" + res.getName() + ".txt", true));
            log.println("Open Time: " + strDate);
            log.println(res.getName());
            for (int i = 0; i < res.getBranchTotal(); i++) {
                log.println("Branch (" + res.getBranch(i).getX() + ", " + res.getBranch(i).getY() + ") : " + res.getBranch(i).getBranchOrderComplete());
                if (res.getBranch(i).getCustomerList().isEmpty()) {
                    log.println();
                } else {
                    log.println(String.format("|%-9s|%-8s|%-50s|%-30s|", "Customer", "Arrival", "Food Ordered", "Special Request"));
                    for (int j = 0; j < res.getBranch(i).getCustomerList().size(); j++) {
                        String str = String.format("|(%-2d,%-2d)  |%-8d|%-50s|%-30s|", res.getBranch(i).getCustomerList().get(j).getX(),
                                res.getBranch(i).getCustomerList().get(j).getY(),
                                res.getBranch(i).getCustomerList().get(j).getArrivalTime(),
                                res.getBranch(i).getCustomerList().get(j).getFoodList(),
                                res.getBranch(i).getCustomerList().get(j).getSpReq());
                        log.println(str);
                    }
                    log.println();
                }
            }
            log.println("Total Orders: " + res.getOrderComplete() + "\n");
            log.println();
            log.close();
        } catch (IOException e) {
            System.out.println("Log error");
        }
    }
}
