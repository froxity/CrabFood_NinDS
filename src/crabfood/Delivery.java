
package crabfood;

public class Delivery {
    private int maxTotal;
    private int currentAvailable;
    private int lastAvailableTime;
    
    public Delivery(int total){
        this.maxTotal = total;
        currentAvailable = total;
        lastAvailableTime = 0;
    }
    
    public boolean isAvailable(){
        return currentAvailable < maxTotal;
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

    public void setCurrentAvailable(int currentAvailable) {
        this.currentAvailable = currentAvailable;
    }

    public int getLastAvailableTime() {
        return lastAvailableTime;
    }

    public void setLastAvailableTime(int lastAvailableTime) {
        this.lastAvailableTime = lastAvailableTime;
    }
    
    
}
