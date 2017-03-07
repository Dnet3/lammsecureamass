package org.lammsecure.lammsecureamass.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.lammsecure.lammsecureamass.R;

public class AccountDetailsActivity extends AppCompatActivity {

    public static final String USER_NAME = "user_name",
            REAL_NAME = "real_name",
            EMAIL_ADDRESS = "email_address";

    public static final int RESULT_CODE = 145;

    public static void startForResult(Activity activity, String userName, String realName, String emailAddress) {
        Intent starter = new Intent(activity, AccountDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(USER_NAME, userName);
        bundle.putString(REAL_NAME, realName);
        bundle.putString(EMAIL_ADDRESS, emailAddress);
        starter.putExtras(bundle);
        activity.startActivityForResult(starter, RESULT_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        String userName = "username", realName = "Real name", emailAddress = "email@domain.com";

        TextView uName = (TextView) findViewById(R.id.username_textview);
        TextView rName = (TextView) findViewById(R.id.realname_textview);
        TextView eAddress = (TextView) findViewById(R.id.email_address_textview);
        Button signOut = (Button) findViewById(R.id.sign_out_button);

        Bundle args = getIntent().getExtras();

        if (args != null) {
            userName = args.getString(USER_NAME);
            realName = args.getString(REAL_NAME);
            emailAddress = args.getString(EMAIL_ADDRESS);
        }

        uName.setText(userName);
        rName.setText(realName);
        eAddress.setText(emailAddress);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signOutIntent = new Intent();
                setResult(Activity.RESULT_OK, signOutIntent);
                finish();
            }
        });

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
    }
}
