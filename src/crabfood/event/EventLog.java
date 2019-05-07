package crabfood.event;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class EventLog {

    public EventLog() {
        try {
            PrintWriter log = new PrintWriter(new FileOutputStream("eventlog.txt"));
            log.println("|Customer \t|Arrival \t|Order Time \t|Finished Cooking Time \t|Delivery Time \t|Total Time");
            log.close();
        } catch (IOException e) {
            System.out.println("Log error");
        }
    }

    public void log(int custIndex, int arrivalTime, int finishCookingTime, int deliveryTime) {
        try {
            PrintWriter log = new PrintWriter(new FileOutputStream("eventlog.txt", true));
            log.println("|" + custIndex + " \t\t|" + arrivalTime + " \t\t|" + arrivalTime + " \t\t|" + finishCookingTime + " \t\t\t|" + deliveryTime + " \t\t|" + (finishCookingTime - arrivalTime + deliveryTime));
            log.close();
        } catch (IOException e) {
            System.out.println("Log error");
        }
    }
}

