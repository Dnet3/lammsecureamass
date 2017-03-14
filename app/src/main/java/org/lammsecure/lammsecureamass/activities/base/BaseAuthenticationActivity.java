package org.lammsecure.lammsecureamass.activities.base;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.lammsecure.lammsecureamass.activities.LoginActivity;

/**
 * Created by Max on 28/2/17.
 *
 * A base class for activities that the user should be signed in to use
 */

public class BaseAuthenticationActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    // On startup, add this activity as a Firebase AuthState listener
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    // On stop, remove this Firebase AuthState listener
    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    // If the Firebase AuthState changes, this method will be called
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.i(getClass().getSimpleName(), "onAuthStateChanged:signed_in: ID=" + user.getUid() + ", EMAIL=" + user.getEmail() + ", NAME=" + user.getDisplayName());
        } else {
            // User is signed out
            Log.d(getClass().getSimpleName(), "onAuthStateChanged:signed_out");
            LoginActivity.start(this);
            finish();
        }
    }
}
