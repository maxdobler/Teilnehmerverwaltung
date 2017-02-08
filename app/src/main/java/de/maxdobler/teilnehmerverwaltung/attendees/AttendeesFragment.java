package de.maxdobler.teilnehmerverwaltung.attendees;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.customerDetail.CustomerDetailActivity;
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
        Query query = FirebaseRef.customers().orderByChild(Customer.DEACTIVATED);
        FirebaseRecyclerAdapter<Customer, CustomerViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Customer, CustomerViewHolder>(Customer.class, R.layout.customer_item, CustomerViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final CustomerViewHolder viewHolder, final Customer customer, final int position) {
                final String customerKey = getRef(position).getKey();
                FirebaseRef.customerEventParticipation(customerKey, mEventKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final boolean isAttendee = dataSnapshot.exists();
                        viewHolder.bind(customer, isAttendee);
                        if (customer.isActive()) {
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isAttendee) {
                                        AttendeeService.getInstance().removeAttendeeFromEvent(customerKey, mEventKey, customer);
                                    } else {
                                        AttendeeService.getInstance().attendEvent(customerKey, customer, mEventKey);
                                    }
                                }
                            });
                        }
                        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                Intent intent = new Intent(getContext(), CustomerDetailActivity.class);
                                intent.putExtra(CustomerDetailActivity.CUSTOMER_KEY, customerKey);
                                startActivity(intent);
                                return true;
                            }
                        });
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Error loading customers for event : " + mEventKey, databaseError.toException());
                    }
                });

            }
        };

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), THREE_COLUMNS));
    }

    private void showEmptyQuotaDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.attendee_empty_quota_dialog_title)
                .setMessage(R.string.attendee_empty_quota_dialog_message)
                .setNegativeButton(R.string.attendee_empty_dialog_quota_dialog_negative, null)
                .show();
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
    }
}
