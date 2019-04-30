package crabfood.event;

public abstract class Event implements Comparable<Event> {

    private int priorityTime;

    public void setPriorityTime() {
        this.priorityTime = priorityTime;
    }

    public int getPriorityTime() {
        return priorityTime;
    }

    @Override
    public int compareTo(Event o) {
        return o.priorityTime - this.priorityTime;
    }

}
