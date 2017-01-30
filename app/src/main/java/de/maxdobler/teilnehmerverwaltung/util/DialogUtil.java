package de.maxdobler.teilnehmerverwaltung.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.maxdobler.teilnehmerverwaltung.R;

public class DialogUtil {
    private static DialogUtil ourInstance = new DialogUtil();

    public static DialogUtil getInstance() {
        return ourInstance;
    }

    private DialogUtil() {
    }

    public void askForPin(final Activity activity, final AsyncCallback<Void> callback) {
        RemoteConfigService.getInstance().getBuyPin(activity, new RemoteConfigService.AsyncCallback<String>() {
            @Override
            public void success(final String expectedPin) {
                showPinDialog(activity, expectedPin, callback);
            }
        });
    }

    private void showPinDialog(final Activity activity, final String expectedPin, final AsyncCallback<Void> callback) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final View pinInputView = inflater.inflate(R.layout.dialog_pin_input_view, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.pin_dialog_title)
                .setView(pinInputView)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog1) {
                ((AlertDialog) dialog1).getButton(DialogInterface.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText pinInput = (EditText) pinInputView.findViewById(R.id.pinInput);
                                String pin = pinInput.getText().toString();

                                if (pin.equals(expectedPin)) {
                                    callback.success(null);
                                    dialog1.dismiss();
                                } else {
                                    pinInput.setError(activity.getString(R.string.pin_dialog_error_message));
                                    pinInput.setText("");
                                }
                            }
                        });
            }
        });

        dialog.show();
    }
}
