package org.lammsecure.lammsecureamass.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.base.BaseNavigationDrawerActivity;
import org.lammsecure.lammsecureamass.adapters.AccountPagerAdapter;
import org.lammsecure.lammsecureamass.fragments.ArduinoChoiceFragment;
import org.lammsecure.lammsecureamass.fragments.DriversFragment;
import org.lammsecure.lammsecureamass.fragments.ManagersFragment;
import org.lammsecure.lammsecureamass.models.LAMMAccountObject;

/**
 * A simple {@link BaseNavigationDrawerActivity} subclass.
 * Use the {@link MainActivity#start}  method to
 * start this Activity.
 */
public class MainActivity extends BaseNavigationDrawerActivity implements ArduinoChoiceFragment.OnFragmentInteractionListener,
        DriversFragment.OnFragmentInteractionListener,
        ManagersFragment.OnFragmentInteractionListener{

    private static final String ARG_ACCOUNT_NAME = "account_name",
            ARG_ACCOUNT_OBJECT = "account_object";;

    private String mAccountName;
    private LAMMAccountObject mAccountObject;

    private AccountPagerAdapter mPagerAdapter;

    private DatabaseReference mAccountDatabaseRef;
    private ValueEventListener mAccountRefListener;

    /**
     * @param accountName the name of the Account to display.
     * @return A new instance of activity MainActivity.
     */
    public static void start(Context context, String accountName) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAccountName = extras.getString(ARG_ACCOUNT_NAME);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_ACCOUNT_OBJECT)) {
                mAccountObject = savedInstanceState.getParcelable(ARG_ACCOUNT_OBJECT);
            }
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.fragment_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_layout_title_arduinos)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_layout_title_drivers)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_layout_title_managers)));

        final ViewPager fragmentViewPager = (ViewPager) findViewById(R.id.fragment_pager);
        mPagerAdapter = new AccountPagerAdapter(getSupportFragmentManager(), mAccountName);
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
    }

    /**
     * Android lifecycle method
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    /**
     * Android lifecycle method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_action_account:
                if (mAccountObject != null) {
                    AccountDetailsActivity.start(MainActivity.this, mAccountName, mAccountObject);
                }
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
    protected void onResume() {
        super.onResume();

        mAccountDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url)).child(getString(R.string.firebase_branch_accounts)).child(mAccountName);
        mAccountRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // User found in users table in Firebase Database
                    LAMMAccountObject accountObject = dataSnapshot.getValue(LAMMAccountObject.class);
                    if (accountObject != null) {


                        if (mAccountObject == null || !accountObject.equals(mAccountObject)) {
                            mAccountObject = accountObject;

                            if (mAccountObject.getArduinos() != null) {
                                mPagerAdapter.setArduinos(true);
                            }
                        }
                    }
                    else {
                        FirebaseCrash.report(new Exception("mAccountDatabaseRef: onDataChange() Account found in database, error parsing into Java."));
                        Log.e(MainActivity.class.getSimpleName(), "mAccountDatabaseRef: onDataChange() Account found in database, error parsing into Java.");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseCrash.report(new Exception("mAccountDatabaseRef: onCancelled() Error reading from account table: " + databaseError.toString()));
                Log.e(MainActivity.class.getSimpleName(), "mAccountDatabaseRef: onCancelled() Error reading from account table: " + databaseError.toString());
            }
        };
        mAccountDatabaseRef.addListenerForSingleValueEvent(mAccountRefListener);
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_ACCOUNT_OBJECT, mAccountObject);
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAccountRefListener != null) {
            if (mAccountDatabaseRef != null) {
                mAccountDatabaseRef.removeEventListener(mAccountRefListener);
            }
            mAccountRefListener = null;
        }
    }

    /**
     * ArduinoChoiceFragment listener method
     */
    @Override
    public void onAddArduinoClicked() {
        AddArduinoActivity.start(this, mAccountName);
    }

    /**
     * ArduinoChoiceFragment listener method
     */
    @Override
    public void hasNoArduinos() {
        mPagerAdapter.setArduinos(false);
    }

    /**
     * DriversFragment listener method
     */
    @Override
    public void onAddDriverClicked() {
        Toast.makeText(this, "This button will add a Driver to this account", Toast.LENGTH_SHORT).show();
    }

    /**
     * ManagersFragment listener method
     */
    @Override
    public void onAddManagerClicked() {
        Toast.makeText(this, "This button will add a Manager to this account", Toast.LENGTH_SHORT).show();
    }
}
