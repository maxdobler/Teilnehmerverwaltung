package de.maxdobler.teilnehmerverwaltung.attendees;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Customer implements Serializable {

    public static final String ATTENDED_EVENTS = "attendedEvents";
    public static final String DEACTIVATED = "deactivted";
    private String name;
    private int quota = 0;
    private Map<String, Boolean> attendedEvents = new HashMap<>();
    private boolean deactivted = false;

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

    public void addQuota(int amount) {
        this.quota += amount;
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

    public void setDeactivted(boolean deactivted) {
        this.deactivted = deactivted;
    }

    public boolean isDeactivted() {
        return deactivted;
    }

    public void deactivate() {
        this.deactivted = true;
    }

    @Exclude
    public boolean isActive() {
        return !this.deactivted;
    }

    public void activate() {
        this.deactivted = false;
    }
}
