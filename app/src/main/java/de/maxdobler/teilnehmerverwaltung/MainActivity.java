package de.maxdobler.teilnehmerverwaltung;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.attendees.AttendeesFragment;

public class MainActivity extends AppCompatActivity implements AttendeesFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainActivity, AttendeesFragment.newInstance())
                .commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
