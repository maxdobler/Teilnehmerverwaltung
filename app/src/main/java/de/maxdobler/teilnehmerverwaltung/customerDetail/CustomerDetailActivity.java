package de.maxdobler.teilnehmerverwaltung.customerDetail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.attendees.Customer;
import de.maxdobler.teilnehmerverwaltung.util.AsyncCallback;
import de.maxdobler.teilnehmerverwaltung.util.DialogUtil;
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
                    replaceWithCustomerInfo();
                    supportInvalidateOptionsMenu();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            setNewCustomerFragment();
        }

    }

    private void replaceWithCustomerInfo() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.customer_detail_activity, CustomerInfoFragment.newInstance(mCustomer))
                .commit();
    }

    private void setNewCustomerFragment() {
        setupToolbar(getString(R.string.add_customer_title));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.customer_detail_activity, CustomerEditFragment.newInstance(null))
                .commit();
    }

    @Override
    protected void onDestroy() {
        if (mCustomerFirebaseListener != null) {
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
        if (mCustomerKey != null && mCustomer != null) {
            if (mCustomer.isActive()) {
                getMenuInflater().inflate(R.menu.customer_detail_actions, menu);
            } else {
                getMenuInflater().inflate(R.menu.customer_detail_actions_deactivted, menu);
            }
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

        if (itemId == R.id.action_edit_customer) {
            DialogUtil.getInstance().askForPin(this, new AsyncCallback<Void>() {
                @Override
                public void success(Void value) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.customer_detail_activity, CustomerEditFragment.newInstance(mCustomer))
                            .commit();
                }
            });
            return true;
        }
        if (itemId == R.id.action_deaktivate_customer) {
            new AlertDialog.Builder(this)
                    .setTitle("Kunde deaktivieren")
                    .setMessage("Soll der Kunde wirklich deaktiviert werden? Es können danach keine Veranstaltungeteilnahmen mehr eingetragen werden.")
                    .setNegativeButton("Nein", null)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeListener();
                            mCustomer.deactivate();
                            FirebaseRef.customer(mCustomerKey).setValue(mCustomer);
                            finish();
                        }
                    })
                    .create()
                    .show();
            return true;
        }
        if (itemId == R.id.action_aktivate_customer) {
            new AlertDialog.Builder(this)
                    .setTitle("Kunde aktivieren")
                    .setMessage("Soll der Kunde wieder aktiviert werden?")
                    .setNegativeButton("Nein", null)
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeListener();
                            mCustomer.activate();
                            FirebaseRef.customer(mCustomerKey).setValue(mCustomer);
                            finish();
                        }
                    })
                    .create()
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToolbarTitle(String title) {
        setupToolbar(title);
    }

    @Override
    public void saveCustomer(Customer customer) {
        if (mCustomerKey == null) {
            FirebaseRef.customers().push().setValue(customer);
            finish();
        } else {
            FirebaseRef.customer(mCustomerKey).setValue(customer);
            replaceWithCustomerInfo();
        }
    }

    @Override
    public void addQuota(int amount) {
        mCustomer.addQuota(amount);
        FirebaseRef.customer(mCustomerKey).setValue(mCustomer);
    }
}
