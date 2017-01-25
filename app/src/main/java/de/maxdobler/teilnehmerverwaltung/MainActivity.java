package de.maxdobler.teilnehmerverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.addAttendee.AddAttendeeActivity;
import de.maxdobler.teilnehmerverwaltung.attendees.AttendeesFragment;
import de.maxdobler.teilnehmerverwaltung.events.EventsFragment;

public class MainActivity extends AppCompatActivity implements AttendeesFragment.OnAttendeesFragmentListener, EventsFragment.OnEventFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        Fragment eventFragment = getSupportFragmentManager().findFragmentByTag(EventsFragment.TAG);
        if (eventFragment == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainContent, EventsFragment.newInstance(), EventsFragment.TAG)
                    .commit();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showAddAttendee();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttend(String attendeeKey) {

    }

    @Override
    public void onEventSelected(String eventKey) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment oldAttendees = fragmentManager.findFragmentByTag(AttendeesFragment.TAG);
        if (oldAttendees != null) {
            fragmentTransaction = fragmentTransaction
                    .remove(oldAttendees);

        }
        fragmentTransaction
                .add(R.id.mainContent, AttendeesFragment.newInstance(eventKey), AttendeesFragment.TAG)
                .commit();

    }

    void showAddAttendee() {
        startActivity(new Intent(this, AddAttendeeActivity.class));
    }
}
