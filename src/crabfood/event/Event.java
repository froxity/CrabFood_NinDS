package crabfood.event;

public abstract class Event {

    private int eventTime;
    private int custNo;

    public void setEventTime(int eventTime, int custNo) {
        this.eventTime = eventTime;
    }

    public int getEventTime() {
        return eventTime;
    }

    public int getCustNo() {
        return custNo;
    }

    public void setCustNo(int custNo) {
        this.custNo = custNo;
    }
}
