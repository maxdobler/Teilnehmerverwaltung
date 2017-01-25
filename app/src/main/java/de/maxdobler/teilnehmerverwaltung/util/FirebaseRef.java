package de.maxdobler.teilnehmerverwaltung.util;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.maxdobler.teilnehmerverwaltung.events.Event;

public class FirebaseRef {

    public static DatabaseReference customers() {
        return FirebaseDatabase.getInstance().getReference("customers");
    }

    public static DatabaseReference events() {
        return FirebaseDatabase.getInstance().getReference("events");
    }

    public static DatabaseReference event(String eventKey) {
        return events().child(eventKey);
    }

    public static DatabaseReference eventAttendees(String eventKey) {
        return event(eventKey).child(Event.ATTENDEES);
    }

    public static DatabaseReference eventAttendee(String eventKey, String attendeeKey) {
        return event(eventKey).child(Event.ATTENDEES).child(attendeeKey);
    }
}
