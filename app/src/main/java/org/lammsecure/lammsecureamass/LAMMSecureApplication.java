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

    /**
     * Use this method to retrieve a single LAMMSecureApplication instance
     */
    public static LAMMSecureApplication getInstance() {

        if (sInstance == null) {
            sInstance = new LAMMSecureApplication();
        }
        return sInstance;
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        mGoogleApiClientSingleton = new GoogleApiClientSingleton(sInstance);
    }

    /**
     * Use this method to retrieve LAMM Secure Application context
     */
    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

    /**
     * Use this method to retrieve single GoogleApiClient instance
     */
    public GoogleApiClientSingleton getGoogleApiClientSingletonInstance() {

        if (mGoogleApiClientSingleton == null) {
            mGoogleApiClientSingleton = new GoogleApiClientSingleton(sInstance);
        }
        return mGoogleApiClientSingleton;
    }

    /**
     * Use this method to retrieve single GoogleApiClient instance
     */
    public static GoogleApiClientSingleton getGoogleApiClientSingleton() {
        return getInstance().getGoogleApiClientSingletonInstance();
    }
}
