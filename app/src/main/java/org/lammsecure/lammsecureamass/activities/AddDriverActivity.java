package org.lammsecure.lammsecureamass.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.base.BaseAuthenticationActivity;

/**
 * A simple {@link BaseAuthenticationActivity} subclass.
 * Use the {@link AddDriverActivity#start}  method to
 * start this Activity.
 */
public class AddDriverActivity extends BaseAuthenticationActivity {

    private static final String ARG_ACCOUNT_NAME = "account_name";

    private String mAccountName;

    private DatabaseReference mUserDatabaseRef;
    private ValueEventListener mUserRefListener;

    /**
     * @param accountName the name of the Account to display.
     * @return A new instance of activity AddDriverActivity.
     */
    public static void start(Context context, String accountName) {
        Intent starter = new Intent(context, AddDriverActivity.class);
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT_NAME, accountName);
        starter.putExtras(args);
        context.startActivity(starter);
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_add_driver_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            mAccountName = args.getString(ARG_ACCOUNT_NAME);
        }

        //mUserDatabaseRef.child("users").orderByChild("name").equalTo("Alex");
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUserRefListener != null) {
            if (mUserDatabaseRef != null) {
                mUserDatabaseRef.removeEventListener(mUserRefListener);
            }
            mUserRefListener = null;
        }
    }
}
