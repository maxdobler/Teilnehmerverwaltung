package de.maxdobler.teilnehmerverwaltung.addAttendee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.maxdobler.teilnehmerverwaltung.Customer;
import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.util.FirebaseRef;

public class AddAttendeeActivity extends AppCompatActivity {

    @BindView(R.id.nameEditText)
    EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_attendee_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.saveButton)
    void saveAttendee() {

        String name = nameEditText.getText().toString();
        if (name.isEmpty() || name.length() == 0 || name.equals(" ")) {
            nameEditText.setError(getString(R.string.add_attendee_name_error_message));
            return;
        }
        FirebaseRef.customers().push().setValue(new Customer(name));
        finish();
    }
}
