package de.maxdobler.teilnehmerverwaltung.util;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRef {

    public static DatabaseReference attendees() {
        return FirebaseDatabase.getInstance().getReference("attendees");
    }
}
