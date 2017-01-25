package de.maxdobler.teilnehmerverwaltung.events;

public class Event {
    public static final String ATTENDEES = "attendees";
    private String name;

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
}
