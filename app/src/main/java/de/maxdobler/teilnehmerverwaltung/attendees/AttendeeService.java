package de.maxdobler.teilnehmerverwaltung.attendees;

import com.google.firebase.database.DatabaseReference;

import de.maxdobler.teilnehmerverwaltung.util.FirebaseRef;

public class AttendeeService {
    private static AttendeeService ourInstance = new AttendeeService();

    public static AttendeeService getInstance() {
        return ourInstance;
    }

    private AttendeeService() {
    }

    public boolean attendEvent(String customerKey, Customer customer, String eventKey) {
        DatabaseReference eventAttendeeRef = FirebaseRef.eventAttendee(eventKey, customerKey);
        if (customer.hasQuota()) {
            eventAttendeeRef.setValue(true);
            customer.removeQuota(1);
            customer.addAttendedEvent(eventKey);
            saveCustomer(customerKey, customer);
            return true;
        }
        return false;
    }

    public void removeAttendeeFromEvent(String customerKey, String eventKey, Customer customer) {
        FirebaseRef.eventAttendee(eventKey, customerKey).removeValue();
        customer.addQuota(1);
        customer.removeAttendedEvent(eventKey);
        saveCustomer(customerKey, customer);
    }

    private void saveCustomer(String customerKey, Customer customer) {
        FirebaseRef.customer(customerKey).setValue(customer);
    }
}
