package crabfood.event;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EventLog {

    public EventLog() {
        try {
            PrintWriter log = new PrintWriter(new FileOutputStream("eventlog.txt", true));
            log.println("|Customer \t|Arrival \t|Order Time \t|Finished Cooking Time \t|Delivery Time \t|Total Time \t|Restaurant \t\t|Branch\t|Food Ordered \t\t|Special Request");
            log.close();
        } catch (IOException e) {
            System.out.println("Log error");
        }
    }

    public void log(int custIndex, int arrivalTime, int finishCookingTime, int deliveryTime, String resName, int branchXCoord, int branchYCoord, ArrayList<String> foodList, String spReq) {
        try {
            PrintWriter log = new PrintWriter(new FileOutputStream("eventlog.txt", true));
            log.printf("|" + custIndex + " \t\t|" + arrivalTime + " \t\t|" + arrivalTime + " \t\t|" + finishCookingTime + " \t\t\t|" + deliveryTime + " \t\t|" + (finishCookingTime - arrivalTime + deliveryTime) + " \t\t|" + resName + " \t\t|" + branchXCoord + " " + branchYCoord + "\t|" + foodList + " \t\t|" + spReq);
            log.println();
            log.println();
            log.close();
        } catch (IOException e) {
            System.out.println("Log error");
        }
    }
}

