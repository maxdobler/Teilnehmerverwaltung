package de.maxdobler.teilnehmerverwaltung.events.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import de.maxdobler.teilnehmerverwaltung.R;
import de.maxdobler.teilnehmerverwaltung.events.Event;
import de.maxdobler.teilnehmerverwaltung.util.FirebaseRef;


public class AddEventDialog extends DialogFragment {

    private EditText nameEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        nameEditText = new EditText(getActivity());

        return new AlertDialog.Builder(getActivity())
                .setView(nameEditText)
                .setTitle(R.string.add_event_dialog_title)
                .setPositiveButton(R.string.add_event_dialog_positiv, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(R.string.add_event_dialog_negativ, null)
                .create();

    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog) getDialog();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                if (name.isEmpty() || name.length() == 0 || name.equals(" ")) {
                    nameEditText.setError(getString(R.string.add_event_name_error));
                    return;
                }
                Event event = new Event(name);
                FirebaseRef.events().push().setValue(event);
                dialog.dismiss();
            }
        });
    }
}
