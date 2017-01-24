package de.maxdobler.teilnehmerverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.maxdobler.teilnehmerverwaltung.addAttendee.AddAttendeeActivity;
import de.maxdobler.teilnehmerverwaltung.util.FirebaseRef;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        DatabaseReference attendeesRef = FirebaseRef.attendees();
        FirebaseRecyclerAdapter<Attendee, AttendeeViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Attendee, AttendeeViewHolder>(Attendee.class, R.layout.item_attendee, AttendeeViewHolder.class, attendeesRef) {
            @Override
            protected void populateViewHolder(AttendeeViewHolder viewHolder, Attendee attendee, int position) {
                viewHolder.bind(attendee);
            }
        };

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
    }

    @OnClick(R.id.addFab)
    void addAttendee() {
        startActivity(new Intent(this, AddAttendeeActivity.class));
    }
}
