package de.maxdobler.teilnehmerverwaltung.customerSettings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.R;

public class CustomerSettingsActivity extends AppCompatActivity {

    public static final String CUSTOMER_KEY = "customerKey";
    private String mCustomerKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_settings_actitivty);
        ButterKnife.bind(this);

        mCustomerKey = getIntent().getExtras().getString(CUSTOMER_KEY);
    }
}
