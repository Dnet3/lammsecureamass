package org.lammsecure.lammsecureamass.sync;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.lammsecure.lammsecureamass.R;

/**
 * Created by Max on 28/2/17.
 *
 * A GoogleApiClient singleton for checking user account details
 */

public class GoogleApiClientSingleton implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static GoogleApiClient sGoogleApiClient;
    private Context mContext;

    /**
     * Constructor
     */
    public GoogleApiClientSingleton(Context context) {
        mContext = context;
        buildClient();
        connect();
    }

    /**
     * Use this method to retrieve a single GoogleApiClient instance
     */
    public GoogleApiClient getGoogleApiClient() {
        return sGoogleApiClient;
    }

    /**
     * Use this method to connect to a GoogleApiClient
     */
    public void connect() {
        if (sGoogleApiClient != null) {
            sGoogleApiClient.connect();
        }
    }

    /**
     * Use this method to disconnect from a GoogleApiClient
     */
    public void disconnect() {
        if (sGoogleApiClient != null && sGoogleApiClient.isConnected()){
            sGoogleApiClient.disconnect();
        }
    }

    /**
     * Use this method to check if a GoogleApiClient is connected
     */
    public boolean isConnected() {
        if (sGoogleApiClient != null) {
            return sGoogleApiClient.isConnected();
        } else {
            return false;
        }
    }

    /**
     * Use this method to create a GoogleApiClient
     */
    private void buildClient() {

        // Configure Google Sign In Options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build the Google API Client
        sGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    /**
     * GoogleApiClient listener method
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(GoogleApiClientSingleton.class.getSimpleName(), "onConnected()");
    }

    /**
     * GoogleApiClient listener method
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(GoogleApiClientSingleton.class.getSimpleName(), "onConnectionSuspended()");
    }

    /**
     * GoogleApiClient listener method
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(GoogleApiClientSingleton.class.getSimpleName(), "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
        Toast.makeText(mContext, "Google Play Services error", Toast.LENGTH_LONG).show();
    }
}
