package de.maxdobler.teilnehmerverwaltung.customerDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

public class CustomerDetailActivity extends AppCompatActivity {
    private static final String TAG = CustomerDetailActivity.class.getSimpleName();
    public static final String CUSTOMER_KEY = "customerKey";

    @BindView(R.id.nameEditText)
    EditText nameEditText;

    @BindView(R.id.quotaEditText)
    EditText quotaEditText;

    private Customer mCustomer;
    private String mCustomerKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_detail_activity);
        ButterKnife.bind(this);
        quotaEditText.setText("0");

        Bundle extras = getIntent().getExtras();

        setupToolbar(getString(R.string.customer_detail_title));

        if (extras != null) {
            loadCustomer(extras);
        }

    }

    private void setupToolbar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadCustomer(Bundle extras) {
        mCustomerKey = extras.getString(CUSTOMER_KEY);
        FirebaseRef.customer(mCustomerKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCustomer = dataSnapshot.getValue(Customer.class);
                nameEditText.setText(mCustomer.getName());
                quotaEditText.setText(String.valueOf(mCustomer.getQuota()));
                setupToolbar(mCustomer.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load customer " + mCustomerKey, databaseError.toException());
            }
        });
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

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
