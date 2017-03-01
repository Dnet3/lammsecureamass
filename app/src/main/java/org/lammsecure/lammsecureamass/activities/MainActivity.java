package org.lammsecure.lammsecureamass.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.lammsecure.lammsecureamass.LAMMSecureApplication;
import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.base.BaseNavigationDrawerActivity;

public class MainActivity extends BaseNavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        Log.i(MainActivity.class.getSimpleName(), "onCreate()");
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i(MainActivity.class.getSimpleName(), "onOptionsItemSelected() menuItem=" + item.toString());

        switch (item.getItemId()) {

            case R.id.menu_action_account:

                // Start the AccountDetailsActivity
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    AccountDetailsActivity.startForResult(MainActivity.this, "testing", user.getDisplayName(), user.getEmail());
                }
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AccountDetailsActivity.RESULT_CODE) {

            if (resultCode == Activity.RESULT_OK){

                // Firebase sign out
                FirebaseAuth.getInstance().signOut();

                // Google sign out
                Auth.GoogleSignInApi.signOut(LAMMSecureApplication.getGoogleApiClientSingleton().getGoogleApiClient()).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                finish();
                            }
                        });
            }
        }
    }
}
