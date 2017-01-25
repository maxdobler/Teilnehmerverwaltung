package de.maxdobler.teilnehmerverwaltung.attendees;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.Attendee;
import de.maxdobler.teilnehmerverwaltung.AttendeeViewHolder;
import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.util.FirebaseRef;

public class AttendeesFragment extends Fragment {
    public static final int THREE_COLUMNS = 3;
    public static final String TAG = AttendeesFragment.class.getSimpleName();
    private static final String EVENT_KEY = "eventKey";

    private OnAttendeesFragmentListener mListener;
    private String mEventKey;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public AttendeesFragment() {
    }

    public static AttendeesFragment newInstance(String eventKey) {
        AttendeesFragment fragment = new AttendeesFragment();
        Bundle args = new Bundle();
        args.putString(EVENT_KEY, eventKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventKey = getArguments().getString(EVENT_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.attendees_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        DatabaseReference attendeesRef = FirebaseRef.attendees();
        FirebaseRecyclerAdapter<Attendee, AttendeeViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Attendee, AttendeeViewHolder>(Attendee.class, R.layout.item_attendee, AttendeeViewHolder.class, attendeesRef) {
            @Override
            protected void populateViewHolder(final AttendeeViewHolder viewHolder, final Attendee attendee, final int position) {
                final String attendeeKey = getRef(position).getKey();
                FirebaseRef.eventAttendees(mEventKey).child(attendeeKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final boolean isAttendee = dataSnapshot.exists();
                        viewHolder.bind(attendee, isAttendee);
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference eventAttendeeRef = FirebaseRef.eventAttendee(mEventKey, attendeeKey);
                                if (isAttendee) {
                                    eventAttendeeRef.removeValue();
                                } else {
                                    eventAttendeeRef.setValue(true);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Error loading attendees for event : " + mEventKey, databaseError.toException());
                    }
                });

            }
        };

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), THREE_COLUMNS));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAttendeesFragmentListener) {
            mListener = (OnAttendeesFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAttendeesFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAttendeesFragmentListener {
        void onAttend(String attendeeKey);
    }
}
