package de.maxdobler.teilnehmerverwaltung.customerDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.attendees.Customer;
import de.maxdobler.teilnehmerverwaltung.events.Event;
import de.maxdobler.teilnehmerverwaltung.events.EventViewHolder;
import de.maxdobler.teilnehmerverwaltung.util.FirebaseRef;
import de.maxdobler.teilnehmerverwaltung.util.RemoteConfigService;

import static java.lang.String.valueOf;

public class CustomerInfoFragment extends Fragment {
    private static final String CUSTOMER = "customer";
    private OnCustomerInfoFragmentListener mListener;
    private Customer mCustomer;

    @BindView(R.id.quota)
    TextView quotaTextView;

    @BindView(R.id.participationsHeadline)
    TextView participationsHeadline;

    @BindView(R.id.participationsRecyclerView)
    RecyclerView participationsRecyclerView;

    @BindView(R.id.noParticipationPlaceholder)
    TextView noParticipationsPlaceholder;

    public CustomerInfoFragment() {
    }

    public static CustomerInfoFragment newInstance(Customer customer) {
        CustomerInfoFragment fragment = new CustomerInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(CUSTOMER, customer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomer = (Customer) getArguments().getSerializable(CUSTOMER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setQuota();
        setupParticipations();

    }

    private void setupParticipations() {
        Set<String> eventKeys = mCustomer.getAttendedEvents().keySet();
        participationsHeadline.setText(getString(R.string.customer_info_participations_headline, eventKeys.size()));
        if (eventKeys.isEmpty()) {
            noParticipationsPlaceholder.setVisibility(View.VISIBLE);
            noParticipationsPlaceholder.setText(getString(R.string.customer_info_no_participations_placeholder_message, mCustomer.getName()));
        } else {
            noParticipationsPlaceholder.setVisibility(View.GONE);
            EventsAdapter recyclerAdapter = new EventsAdapter(new ArrayList<>(eventKeys));
            participationsRecyclerView.setAdapter(recyclerAdapter);
            participationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void setQuota() {
        quotaTextView.setText(valueOf(mCustomer.getQuota()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCustomerInfoFragmentListener) {
            mListener = (OnCustomerInfoFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCustomerInfoFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.add10Quota)
    void add10Quota() {

        RemoteConfigService.getInstance().getBuyPin(getActivity(), new RemoteConfigService.AsyncCallback<String>() {
            @Override
            public void success(final String expectedPin) {
                showPinDialog(expectedPin);
            }
        });


    }

    private void showPinDialog(final String expectedPin) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View pinInputView = inflater.inflate(R.layout.dialog_pin_input_view, null);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.quota_add_10_dialog_title)
                .setView(pinInputView)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.quota_add_10_dialog_positiv, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText pinInput = (EditText) pinInputView.findViewById(R.id.pinInput);
                                String pin = pinInput.getText().toString();

                                if (pin.equals(expectedPin)) {
                                    mListener.addQuota(10);
                                    dialog.dismiss();
                                } else {
                                    pinInput.setError(getString(R.string.pin_dialog_error_message));
                                    pinInput.setText("");
                                }
                            }
                        });
            }
        });

        dialog.show();
    }

    public interface OnCustomerInfoFragmentListener {
        void addQuota(int amount);
    }

    public class EventsAdapter extends RecyclerView.Adapter<EventViewHolder> {

        private final String TAG = EventsAdapter.class.getSimpleName();
        List<String> eventKeys;

        public EventsAdapter(List<String> eventKeys) {
            this.eventKeys = eventKeys;
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_item, parent, false);
            return new EventViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final EventViewHolder holder, int position) {
            final String eventKey = eventKeys.get(position);
            FirebaseRef.event(eventKey).child(Event.NAME).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String eventName = dataSnapshot.getValue(String.class);
                    holder.bind(eventName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Failed to load event: " + eventKey, databaseError.toException());
                }
            });

        }

        @Override
        public int getItemCount() {
            return eventKeys.size();
        }
    }
}
