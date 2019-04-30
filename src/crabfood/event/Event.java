package crabfood.event;

public abstract class Event implements Comparable<Event> {

    private int eventTime;

    public void setEventTime(int eventTime) {
        this.eventTime = this.eventTime;
    }

    public int getEventTime() {
        return eventTime;
    }

    @Override
    public int compareTo(Event o) {
        return o.eventTime - this.eventTime;
    }

}
