package org.lammsecure.lammsecureamass;

import android.app.Application;
import android.content.Context;

import org.lammsecure.lammsecureamass.sync.GoogleApiClientSingleton;

/**
 * Created by Max on 21/2/17.
 *
 * An application class for providing Application context and configuring account authentication
 */

public class LAMMSecureApplication extends Application {

    private static LAMMSecureApplication sInstance;
    private GoogleApiClientSingleton mGoogleApiClientSingleton;

    public static LAMMSecureApplication getInstance() {

        if (sInstance == null) {
            sInstance = new LAMMSecureApplication();
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        mGoogleApiClientSingleton = new GoogleApiClientSingleton(sInstance);
    }

    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

    public GoogleApiClientSingleton getGoogleApiClientSingletonInstance() {

        if (mGoogleApiClientSingleton == null) {
            mGoogleApiClientSingleton = new GoogleApiClientSingleton(sInstance);
        }
        return mGoogleApiClientSingleton;
    }

    public static GoogleApiClientSingleton getGoogleApiClientSingleton() {
        return getInstance().getGoogleApiClientSingletonInstance();
    }
}
