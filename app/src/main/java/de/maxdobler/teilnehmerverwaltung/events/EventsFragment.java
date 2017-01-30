package de.maxdobler.teilnehmerverwaltung.events;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.util.FirebaseRef;

public class EventsFragment extends Fragment {
    public static final String TAG = EventsFragment.class.getSimpleName();
    private static final String SELECTED_EVENT = "selectedEvent";
    private OnEventFragmentListener mListener;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String mSelectedEventKey;

    public EventsFragment() {
    }

    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.events_fragment, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SELECTED_EVENT, mSelectedEventKey);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectedEventKey = savedInstanceState.getString(SELECTED_EVENT);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        FirebaseRecyclerAdapter<Event, EventViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(Event.class, R.layout.event_item, EventViewHolder.class, FirebaseRef.events()) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, final Event event, final int position) {
                final String eventKey = getRef(position).getKey();
                getRef(position).child(Event.ATTENDEES).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final long attendeesCount = dataSnapshot.getChildrenCount();
                        event.setAttendeesCount(attendeesCount);
                        viewHolder.bind(event);
                        boolean isSelected = eventKey.equals(mSelectedEventKey);
                        viewHolder.setSelected(isSelected);
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mListener.onEventSelected(eventKey);
                                mSelectedEventKey = eventKey;
                                notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Error loading event attendees count for event " + getRef(position).getKey(), databaseError.toException());
                    }
                });

            }
        };
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration devider = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(devider);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventFragmentListener) {
            mListener = (OnEventFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEventFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.addEventButton)
    void openAddEventDialog() {
        new AddEventDialog().show(getFragmentManager(), "AddEventDialog");
    }

    public interface OnEventFragmentListener {
        void onEventSelected(String eventKey);
    }
}
