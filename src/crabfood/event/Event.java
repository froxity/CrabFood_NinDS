package crabfood.event;

public abstract class Event {

    private int eventTime;

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    public int getEventTime() {
        return eventTime;
    }
}
