package org.lammsecure.lammsecureamass.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.base.BaseAuthenticationActivity;
import org.lammsecure.lammsecureamass.models.LAMMAccountObject;

/**
 * A simple {@link BaseAuthenticationActivity} subclass.
 * Use the {@link AccountDetailsActivity#start}  method to
 * start this Activity.
 */
public class AccountDetailsActivity extends BaseAuthenticationActivity {

    private static final String ARG_ACCOUNT_NAME = "account_name",
            ARG_ACCOUNT_OBJECT = "account_object";

    private String mAccountName;
    private LAMMAccountObject mAccountObject;

    /**
     * @param accountName the name of the Account to display.
     * @param accountObject the AccountObject to display.
     * @return A new instance of activity AccountDetailsActivity.
     */
    public static void start(Context context, String accountName, LAMMAccountObject accountObject) {
        Intent starter = new Intent(context, AccountDetailsActivity.class);
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT_NAME, accountName);
        args.putParcelable(ARG_ACCOUNT_OBJECT, accountObject);
        starter.putExtras(args);
        context.startActivity(starter);
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_account_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            mAccountName = args.getString(ARG_ACCOUNT_NAME);
            mAccountObject = args.getParcelable(ARG_ACCOUNT_OBJECT);
        }

        TextView accountNameTextView = (TextView) findViewById(R.id.textview_account_name);
        accountNameTextView.setText(mAccountName);

        TextView accountTypeTextView = (TextView) findViewById(R.id.textview_account_type);
        TextView accountAddressTextView = (TextView) findViewById(R.id.textview_account_address);
        TextView phoneNumberTextView = (TextView) findViewById(R.id.textview_account_phone_number);

        accountTypeTextView.setText(mAccountObject.getAccountType());
        accountAddressTextView.setText(mAccountObject.getAddress());
        phoneNumberTextView.setText(mAccountObject.getPhoneNumber());

        Button signOut = (Button) findViewById(R.id.sign_out_button);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Firebase sign out
                FirebaseAuth.getInstance().signOut();

                // Google sign out
                Auth.GoogleSignInApi.signOut(LAMMSecureApplication.getGoogleApiClientSingleton().getGoogleApiClient());

                finish();
            }
        });
    }

    /**
     * Firebase Authentication listener method
     */
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            TextView realNameTextView = (TextView) findViewById(R.id.textview_realname);
            TextView emailAddressTextView = (TextView) findViewById(R.id.textview_email_address);

            realNameTextView.setText(user.getDisplayName());
            emailAddressTextView.setText(user.getEmail());
        }

        super.onAuthStateChanged(firebaseAuth);
    }
}
