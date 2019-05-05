package crabfood.event;

public abstract class Event {

    private int eventTime;
    private int custNo;
    private int eventID;

    public void setEventTime(int eventTime, int custNo, int eventID) {
        this.eventTime = eventTime;
        this.custNo = custNo;
        this.eventID = eventID;
    }

    public int getEventTime() {
        return eventTime;
    }

    public int getCustNo() {
        return custNo;
    }
    
    public int getEventID(){
        return eventID;
    }
}
