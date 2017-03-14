package org.lammsecure.lammsecureamass.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.base.BaseAuthenticationActivity;
import org.lammsecure.lammsecureamass.models.LAMMArduinoObject;
import org.lammsecure.lammsecureamass.models.LAMMAssignmentObject;

import java.util.HashMap;

/**
 * A simple {@link BaseAuthenticationActivity} subclass.
 * Use the {@link AddArduinoActivity#start}  method to
 * start this Activity.
 */
public class AddArduinoActivity extends BaseAuthenticationActivity {

    private static final String ARG_ACCOUNT_NAME = "account_name",
            ARG_ARD_ID = "arduino_id",
            ARG_ARD_NAME = "arduino_name",
            ID_IN_USE = "id_in_use";

    private HashMap<String, Boolean> mArduinos;
    private String mAccountName;

    private TextInputEditText mArduinoIDEditText, mArduinoNameEditText;
    private TextInputLayout mArduinoIDLayout, mArduinoNameLayout;

    private boolean mArduinoIDInUse;

    private DatabaseReference mAccountArduinosDatabaseRef, mArduinoDatabaseRef;
    private ValueEventListener mAccountArduinosRefListener, mArduinoRefListener;

    /**
     * @param accountName the name of the Account to display.
     * @return A new instance of activity AddArduinoActivity.
     */
    public static void start(Context context, String accountName) {
        Intent starter = new Intent(context, AddArduinoActivity.class);
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
        setContentView(R.layout.activity_add_arduino);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_add_arduino_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mArduinos = new HashMap<>();

        Bundle args = getIntent().getExtras();
        if (args != null) {
            mAccountName = args.getString(ARG_ACCOUNT_NAME);
        }

        if (savedInstanceState != null) {
            mArduinoIDInUse = savedInstanceState.getBoolean(ID_IN_USE);
        } else {
            mArduinoIDInUse = false;
        }

        // Create DatabaseReference and set data listener
        mAccountArduinosDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url)).child(getString(R.string.firebase_branch_accounts)).child(mAccountName).child(getString(R.string.firebase_branch_arduinos));
        mAccountArduinosRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // Account has Arduino/s
                    mArduinos = (HashMap<String, Boolean>) snapshot.getValue();
                }
                else {
                    // Account has no Arduinos
                    mArduinos = new HashMap<>();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(AddArduinoActivity.class.getSimpleName(), "FirebaseDatabaseReference: Error reading accounts table: " + databaseError.toString());
            }
        };
        mAccountArduinosDatabaseRef.addListenerForSingleValueEvent(mAccountArduinosRefListener);
        setUpViews(savedInstanceState);
    }

    /**
     * Use this method to set up the views used in this Activity
     */
    private void setUpViews(Bundle savedInstanceState) {
        mArduinoIDEditText = (TextInputEditText) findViewById(R.id.arduino_id_edittext);
        mArduinoNameEditText = (TextInputEditText) findViewById(R.id.arduino_name_edittext);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_ARD_ID)) {
                mArduinoIDEditText.setText(savedInstanceState.getString(ARG_ARD_ID));
            }
            if (savedInstanceState.containsKey(ARG_ARD_NAME)) {
                mArduinoNameEditText.setText(savedInstanceState.getString(ARG_ARD_NAME));
            }
        }

        mArduinoIDLayout = (TextInputLayout) findViewById(R.id.input_layout_arduino_id);
        mArduinoNameLayout = (TextInputLayout) findViewById(R.id.input_layout_arduino_name);

        mArduinoRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // Arduino ID is already in the arduinos table in Firebase Database
                    mArduinoIDInUse = true;
                    mArduinoIDEditText.requestFocus();
                    mArduinoIDLayout.setErrorEnabled(true);
                    mArduinoIDLayout.setError(getString(R.string.arduino_id_already_exists));
                }
                else {
                    mArduinoIDInUse = false;
                    mArduinoIDLayout.setError(null);
                    mArduinoIDLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(AddArduinoActivity.class.getSimpleName(), "FirebaseDatabaseReference: Error reading arduinos table: " + databaseError.toString());
            }
        };

        final String firebaseUrl = getString(R.string.lamm_firebase_url),
        arduinosBranch = getString(R.string.firebase_branch_arduinos),
                assignmentsBranch = getString(R.string.firebase_branch_assignments),
                decommissionDate = getString(R.string.default_decommission_date),
        arduinoIDNotBlank = getString(R.string.arduino_id_cannot_be_blank);

        mArduinoIDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Check if the arduino ID already exists:
                String arduinoID = mArduinoIDEditText.getText().toString();
                if (!arduinoID.isEmpty()) {
                    mArduinoDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseUrl).child(arduinosBranch).child(arduinoID);
                    mArduinoDatabaseRef.addListenerForSingleValueEvent(mArduinoRefListener);
                }
                else {
                    mArduinoIDLayout.setErrorEnabled(true);
                    mArduinoIDLayout.setError(arduinoIDNotBlank);
                }
            }
        });

        final String arduinoNameNotBlank = getString(R.string.arduino_name_cannot_be_blank);
        mArduinoNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String arduinoName = mArduinoNameEditText.getText().toString();
                if (!arduinoName.isEmpty()) {
                    mArduinoNameLayout.setErrorEnabled(false);
                    mArduinoNameLayout.setError(null);
                }
                else {
                    mArduinoNameLayout.setErrorEnabled(true);
                    mArduinoNameLayout.setError(arduinoNameNotBlank);
                }
            }
        });

        Button sveArduinoButton = (Button) findViewById(R.id.save_arduino_details_button);
        sveArduinoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String arduinoID = mArduinoIDEditText.getText().toString();
                String arduinoName = mArduinoNameEditText.getText().toString();

                if (!arduinoID.isEmpty()) {
                    if (!arduinoName.isEmpty()) {
                        if (!mArduinoIDInUse) {

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            if (firebaseUser != null) {
                                // Send an Account object to the database
                                mArduinos.put(arduinoID, true);
                                mAccountArduinosDatabaseRef.setValue(mArduinos);

                                // Send an Arduino Assignment
                                HashMap<String, Boolean> userAssignment = new HashMap<>();
                                userAssignment.put(firebaseUser.getUid(), true);
                                Long activationDate = System.currentTimeMillis() / 1000;
                                LAMMAssignmentObject assignmentObject = new LAMMAssignmentObject(decommissionDate, String.valueOf(activationDate), userAssignment);
                                FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseUrl).child(assignmentsBranch).child(arduinoID).setValue(assignmentObject);

                                // Send an Arduino object to the database
                                HashMap<String, Boolean> accounts = new HashMap<>();
                                accounts.put(mAccountName, true);
                                LAMMArduinoObject arduinoObject = new LAMMArduinoObject(accounts, String.valueOf(activationDate), decommissionDate, arduinoName);
                                FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseUrl).child(arduinosBranch).child(arduinoID).setValue(arduinoObject);

                                finish();
                            }
                        }
                    }
                    else {
                        mArduinoNameEditText.requestFocus();
                        mArduinoNameLayout.setErrorEnabled(true);
                        mArduinoNameLayout.setError(getString(R.string.arduino_name_cannot_be_blank));
                    }
                }
                else {
                    mArduinoIDEditText.requestFocus();
                    mArduinoIDLayout.setErrorEnabled(true);
                    mArduinoIDLayout.setError(getString(R.string.arduino_id_cannot_be_blank));
                }
            }
        });
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String arduinoID = mArduinoIDEditText.getText().toString();
        if (!arduinoID.isEmpty()) {
            outState.putString(ARG_ARD_ID, arduinoID);
        }

        String arduinoName = mArduinoNameEditText.getText().toString();
        if (!arduinoName.isEmpty()) {
            outState.putString(ARG_ARD_NAME, arduinoName);
        }
        outState.putBoolean(ID_IN_USE, mArduinoIDInUse);
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove listeners
        if (mArduinoRefListener != null) {
            if (mArduinoDatabaseRef != null) {
                mArduinoDatabaseRef.removeEventListener(mArduinoRefListener);
            }
            mArduinoRefListener = null;
        }

        if (mAccountArduinosRefListener != null) {
            if (mAccountArduinosDatabaseRef != null) {
                mAccountArduinosDatabaseRef.removeEventListener(mAccountArduinosRefListener);
            }
            mAccountArduinosRefListener = null;
        }
    }
}
