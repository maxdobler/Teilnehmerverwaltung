package de.maxdobler.teilnehmerverwaltung.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class RemoteConfigService {
    public static final String BUY_PIN = "buy_pin";
    private static RemoteConfigService ourInstance = new RemoteConfigService();

    public static RemoteConfigService getInstance() {
        return ourInstance;
    }

    private RemoteConfigService() {
    }

    public void fetch(final Activity activity, final AsyncCallback<Void> callback) {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.fetch()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            remoteConfig.activateFetched();
                        } else {
                            Log.w(activity.getClass().getSimpleName(), "Failed to fetch remote Config", task.getException());
                        }
                        callback.success(null);
                    }
                });
    }

    public void getBuyPin(Activity activity, final AsyncCallback<String> callback) {
        fetch(activity, new AsyncCallback<Void>() {
            @Override
            public void success(Void value) {
                callback.success(FirebaseRemoteConfig.getInstance().getString(BUY_PIN));
            }

        });
    }


    public interface AsyncCallback<T> {
        void success(T value);
    }
}
