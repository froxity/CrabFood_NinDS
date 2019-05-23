package crabfood;

import java.util.ArrayList;
import java.util.Collections;

public class Delivery {

    private int maxTotal;
    private int currentAvailable;
    private ArrayList<Integer> lastAvailTimeList;

    public Delivery(int total) {
        this.maxTotal = total;
        lastAvailTimeList = new ArrayList<>();
    }

    public boolean isAvailable() {
        return lastAvailTimeList.size() < maxTotal;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getCurrentAvailable() {
        return currentAvailable;
    }

    public int getLastAvailableTime() {
        return lastAvailTimeList.get(0);
    }

    public void addTime(int lastAvailableTime) {
        this.lastAvailTimeList.add(lastAvailableTime);
        Collections.sort(lastAvailTimeList);
    }
}
