package org.lammsecure.lammsecureamass.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.base.BaseAuthenticationActivity;

/**
 * An activity for handling application start-up
 */
public class SplashScreenActivity extends BaseAuthenticationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // A button for logging in users
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the SignUpActivity
                Intent newUserIntent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
                SplashScreenActivity.this.startActivity(newUserIntent);
            }
        });
    }

    // This method is called when the FirebaseAuth.AuthStateListener is set,
    // which happens every time a BaseAuthenticationActivity is started or resumed
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        // If the Firebase authentication state changes
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in, start the MainActivity
            Log.d(SplashScreenActivity.class.getSimpleName(), "onAuthStateChanged:signed_in: ID=" + user.getUid() + ", EMAIL=" + user.getEmail() + ", NAME=" + user.getDisplayName());
            Intent mainActivityIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            SplashScreenActivity.this.startActivity(mainActivityIntent);
            finish();
        }
    }
}