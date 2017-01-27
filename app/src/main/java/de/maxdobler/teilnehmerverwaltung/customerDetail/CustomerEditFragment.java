package de.maxdobler.teilnehmerverwaltung.customerDetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.attendees.Customer;
import de.maxdobler.teilnehmerverwaltung.util.FirebaseRef;

import static de.maxdobler.teilnehmerverwaltung.customerDetail.CustomerDetailActivity.CUSTOMER_KEY;

public class CustomerEditFragment extends Fragment {
    private static final String TAG = CustomerEditFragment.class.getSimpleName();
    private OnCustomerEditFragmentListener mListener;

    @BindView(R.id.nameEditText)
    EditText nameEditText;

    @BindView(R.id.quotaEditText)
    EditText quotaEditText;

    private Customer mCustomer;
    private String mCustomerKey;

    public CustomerEditFragment() {
    }

    public static CustomerEditFragment newInstance(String customerKey) {
        CustomerEditFragment fragment = new CustomerEditFragment();
        if (customerKey != null) {
            Bundle args = new Bundle();
            args.putString(CUSTOMER_KEY, customerKey);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomerKey = getArguments().getString(CUSTOMER_KEY);
        }
    }

    private void loadCustomer() {
        FirebaseRef.customer(mCustomerKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCustomer = dataSnapshot.getValue(Customer.class);
                nameEditText.setText(mCustomer.getName());
                quotaEditText.setText(String.valueOf(mCustomer.getQuota()));
                mListener.setToolbarTitle(mCustomer.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load customer " + mCustomerKey, databaseError.toException());
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_edit_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        quotaEditText.setText("0");
        if (mCustomerKey != null) {
            loadCustomer();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCustomerEditFragmentListener) {
            mListener = (OnCustomerEditFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCustomerEditFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCustomerEditFragmentListener {
        void setToolbarTitle(String title);

        void customerSaved();
    }

    @OnClick(R.id.saveButton)
    void saveCustomer() {
        String name = nameEditText.getText().toString();
        if (name.length() == 0 || name.equals(" ")) {
            nameEditText.setError(getString(R.string.add_attendee_name_error_message));
            return;
        }

        String quotaText = quotaEditText.getText().toString();
        int quota = 0;
        if (quotaText.length() > 0) {
            quota = Integer.valueOf(quotaText);
        }
        if (quota < 0) {
            quotaEditText.setError(getString(R.string.customer_detail_quota_error));
            return;
        }

        if (mCustomer == null) {
            FirebaseRef.customers().push().setValue(new Customer(name, quota));
        } else {
            mCustomer.setName(name);
            mCustomer.setQuota(quota);
            FirebaseRef.customer(mCustomerKey).setValue(mCustomer);
        }

        mListener.customerSaved();
    }
}
