package de.maxdobler.teilnehmerverwaltung;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseRemoteConfig.getInstance().setDefaults(R.xml.remote_config_defaults);
    }
}
