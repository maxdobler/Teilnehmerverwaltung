package de.maxdobler.teilnehmerverwaltung.customerDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.attendees.Customer;
import de.maxdobler.teilnehmerverwaltung.util.FirebaseRef;

public class CustomerDetailActivity extends AppCompatActivity implements CustomerEditFragment.OnCustomerEditFragmentListener, CustomerInfoFragment.OnCustomerInfoFragmentListener {
    private static final String TAG = CustomerDetailActivity.class.getSimpleName();
    public static final String CUSTOMER_KEY = "customerKey";
    private String mCustomerKey = null;
    private Customer mCustomer;
    private ValueEventListener mCustomerFirebaseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_detail_activity);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mCustomerKey = extras.getString(CUSTOMER_KEY);
            mCustomerFirebaseListener = FirebaseRef.customer(mCustomerKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCustomer = dataSnapshot.getValue(Customer.class);
                    setupToolbar(mCustomer.getName());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.customer_detail_activity, CustomerInfoFragment.newInstance(mCustomer))
                            .commit();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            setNewCustomerFragment();
        }

    }

    private void setNewCustomerFragment() {
        setupToolbar(getString(R.string.add_customer_title));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.customer_detail_activity, CustomerEditFragment.newInstance(null))
                .commit();
    }

    @Override
    protected void onDestroy() {
        if (mCustomerKey != null) {
            removeListener();
        }
        super.onDestroy();
    }

    private void removeListener() {
        FirebaseRef.customer(mCustomerKey).removeEventListener(mCustomerFirebaseListener);
    }

    private void setupToolbar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCustomerKey != null) {
            getMenuInflater().inflate(R.menu.customer_detail_actions, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }

        if (itemId == R.id.action_delete_customer) {
            removeListener();
            mCustomer.deactivate();
            FirebaseRef.customer(mCustomerKey).setValue(mCustomer);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToolbarTitle(String title) {
        setupToolbar(title);
    }

    @Override
    public void customerSaved() {
        finish();
    }

    @Override
    public void addQuota(int amount) {
        mCustomer.addQuota(amount);
        FirebaseRef.customer(mCustomerKey).setValue(mCustomer);
    }
}
