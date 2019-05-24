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
            log.println("|Customer \t|Arrival \t|Order Time \t|Finished Cooking Time \t|Delivery Time \t|Total Time \t|Restaurant \t\t|Branch\t|Food Ordered \t\t|Special Request");
            for (Event event : eventList) {
                int deliveryTime = java.lang.Math.abs(event.getCustomer().getX() - event.getBranch().getX())
                        + java.lang.Math.abs(event.getCustomer().getY() - event.getBranch().getY());
                log.println("|" + event.getCustNo() + " \t\t|" + event.getOrderTakenTime() + " \t\t|" + event.getOrderTakenTime() + " \t\t|"
                        + event.getOrderCookedTime() + " \t\t\t|" + deliveryTime + " \t\t|"
                        + (event.getOrderCookedTime() - event.getOrderTakenTime() + deliveryTime) + " \t\t|" + event.getRestaurant().getName() + " \t\t|"
                        + event.getBranch().getX() + " " + event.getBranch().getY() + "\t|" + event.getCustomer().getFoodList() + " \t\t|" + event.getCustomer().getSpReq());
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
                    log.println("|Customer \t|Arrival \t|Food Ordered \t\t|Special Request");
                    for (int j = 0; j < res.getBranch(i).getCustomerList().size(); j++) {
                        log.println("|(" + res.getBranch(i).getCustomerList().get(j).getX() + ", " + res.getBranch(i).getCustomerList().get(j).getY() + ")\t\t|"
                                + res.getBranch(i).getCustomerList().get(j).getArrivalTime() + "\t\t|" + res.getBranch(i).getCustomerList().get(j).getFoodList()
                                + "\t\t|" + res.getBranch(i).getCustomerList().get(j).getSpReq());
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
