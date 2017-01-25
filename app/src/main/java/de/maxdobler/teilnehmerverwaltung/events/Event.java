package de.maxdobler.teilnehmerverwaltung.events;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event {
    public static final String ATTENDEES = "attendees";
    private String name;
    private long attendeesCount;

    public Event() {
    }

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public long getAttendeesCount() {
        return attendeesCount;
    }

    @Exclude
    public void setAttendeesCount(long attendeesCount) {
        this.attendeesCount = attendeesCount;
    }
}
