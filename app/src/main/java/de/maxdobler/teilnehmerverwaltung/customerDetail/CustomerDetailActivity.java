package de.maxdobler.teilnehmerverwaltung.customerDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.R;

public class CustomerDetailActivity extends AppCompatActivity implements CustomerEditFragment.OnCustomerEditFragmentListener {
    private static final String TAG = CustomerDetailActivity.class.getSimpleName();
    public static final String CUSTOMER_KEY = "customerKey";
    private String mCusstomerKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_detail_activity);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mCusstomerKey = extras.getString(CUSTOMER_KEY);
        }

        setupToolbar(getString(R.string.customer_detail_title));

        if (mCusstomerKey == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.customer_detail_activity, CustomerEditFragment.newInstance(mCusstomerKey))
                    .commit();
        }

    }

    private void setupToolbar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
}
