package de.maxdobler.teilnehmerverwaltung.customerDetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.attendees.Customer;

public class CustomerEditFragment extends Fragment {
    private static final String TAG = CustomerEditFragment.class.getSimpleName();
    private static final String CUSTOMER = "customer";
    private OnCustomerEditFragmentListener mListener;

    @BindView(R.id.nameEditText)
    EditText nameEditText;

    @BindView(R.id.quotaEditText)
    EditText quotaEditText;

    private Customer mCustomer;

    public CustomerEditFragment() {
    }

    public static CustomerEditFragment newInstance(Customer customer) {
        CustomerEditFragment fragment = new CustomerEditFragment();
        if (customer != null) {
            Bundle args = new Bundle();
            args.putSerializable(CUSTOMER, customer);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomer = (Customer) getArguments().getSerializable(CUSTOMER);
        }
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
        if (mCustomer != null) {
            nameEditText.setText(mCustomer.getName());
            quotaEditText.setText(String.valueOf(mCustomer.getQuota()));
            mListener.setToolbarTitle(mCustomer.getName());
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

        void saveCustomer(Customer mCustomer);
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
            mCustomer = new Customer(name, quota);
        } else {
            mCustomer.setName(name);
            mCustomer.setQuota(quota);
        }

        mListener.saveCustomer(mCustomer);
    }
}
