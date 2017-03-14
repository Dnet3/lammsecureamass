package org.lammsecure.lammsecureamass.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.base.BaseAuthenticationActivity;
import org.lammsecure.lammsecureamass.adapters.ArduinoPagerAdapter;
import org.lammsecure.lammsecureamass.fragments.ImageCaptureHistoryFragment;
import org.lammsecure.lammsecureamass.fragments.JourneyHistoryFragment;

/**
 * Created by Max on 10/3/17.
 *
 * A simple {@link BaseAuthenticationActivity} subclass.
 * Use the {@link ArduinoActivity#start}  method to
 * start this Activity.
 */
public class ArduinoActivity extends BaseAuthenticationActivity implements ImageCaptureHistoryFragment.OnFragmentInteractionListener,
        JourneyHistoryFragment.OnFragmentInteractionListener {

    private static final String ARG_ARDUINO_ID = "arduino_id",
            ARG_ACCOUNT_NAME = "account_name";

    private String mArduinoID, mAccountName;

    private ArduinoPagerAdapter mPagerAdapter;

    /**
     * @param arduinoID the ID of the Arduino to display.
     * @param accountName the name of the Account to display.
     * @return A new instance of activity ArduinoActivity.
     */
    public static void start(Context context, String arduinoID, String accountName) {
        Intent starter = new Intent(context, ArduinoActivity.class);
        Bundle args = new Bundle();
        args.putString(ARG_ARDUINO_ID, arduinoID);
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
        setContentView(R.layout.activity_arduino);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_arduino_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(ARG_ARDUINO_ID)) {
                mArduinoID = extras.getString(ARG_ARDUINO_ID);
            }
            if (extras.containsKey(ARG_ACCOUNT_NAME)) {
                mAccountName = extras.getString(ARG_ACCOUNT_NAME);
            }
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.fragment_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_layout_title_arduino_details)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_layout_title_arduino_image_captures)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_layout_title_arduino_journeys)));

        final ViewPager fragmentViewPager = (ViewPager) findViewById(R.id.fragment_pager);
        mPagerAdapter = new ArduinoPagerAdapter(getSupportFragmentManager(), mArduinoID);
        fragmentViewPager.setAdapter(mPagerAdapter);
        fragmentViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        String arduinoID = getString(R.string.arduino_identifier) + mArduinoID;
        getSupportActionBar().setTitle(arduinoID);
    }

    /**
     * Android lifecycle method
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_arduino_activity, menu);
        return true;
    }

    /**
     * Android lifecycle method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_action_delete:

                // Show a dialog asking if the user is sure
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        switch (id){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if (user != null) {

                                    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url));
                                    firebaseDatabase.child(getString(R.string.firebase_branch_users)).child(user.getUid()).child(getString(R.string.firebase_branch_arduinos)).child(mArduinoID).removeValue();
                                    firebaseDatabase.child(getString(R.string.firebase_branch_arduinos)).child(mArduinoID).removeValue();
                                    firebaseDatabase.child(getString(R.string.firebase_branch_assignments)).child(mArduinoID).removeValue();
                                    firebaseDatabase.child(getString(R.string.firebase_branch_image_captures)).child(mArduinoID).removeValue();
                                    firebaseDatabase.child(getString(R.string.firebase_branch_journeys)).child(mArduinoID).removeValue();
                                    firebaseDatabase.child(getString(R.string.firebase_branch_accounts)).child(mAccountName).child(getString(R.string.firebase_branch_arduinos)).child(mArduinoID).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            ArduinoActivity.this.finish();
                                        }
                                    });
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.confirm_delete_arduino))
                        .setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show();

                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(ARG_ARDUINO_ID)) {
                mArduinoID = extras.getString(ARG_ARDUINO_ID);
                setIntent(intent);
            }
        }
    }

    /**
     * JourneyHistoryFragment listener method
     */
    @Override
    public void noJourneyFound() {
        mPagerAdapter.setJourneys(false);
    }

    /**
     * ImageCaptureHistoryFragment listener method
     */
    @Override
    public void noImageCaptureFound() {
        mPagerAdapter.setImageCaptures(false);
    }
}
