package de.maxdobler.teilnehmerverwaltung.attendees;

import java.util.HashMap;
import java.util.Map;

public class Customer {

    public static final String ATTENDED_EVENTS = "attendedEvents";
    private String name;
    private int quota = 0;
    private Map<String, Boolean> attendedEvents = new HashMap<>();

    public Customer() {
    }

    public Customer(String name, int quota) {
        this.name = name;
        this.quota = quota;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public void addQuota(int numberOfDays) {
        this.quota += numberOfDays;
    }

    public void removeQuota(int numberOfDays) {
        this.quota -= numberOfDays;
    }

    public boolean hasQuota() {
        return this.quota > 0;
    }

    public Map<String, Boolean> getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(Map<String, Boolean> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    public void addAttendedEvent(String eventKey) {
        this.attendedEvents.put(eventKey, true);
    }

    public void removeAttendedEvent(String eventKey) {
        this.attendedEvents.remove(eventKey);
    }
}
