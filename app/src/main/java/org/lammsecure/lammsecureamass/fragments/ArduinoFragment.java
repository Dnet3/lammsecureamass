package org.lammsecure.lammsecureamass.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.models.LAMMArduinoObject;
import org.lammsecure.lammsecureamass.models.LAMMAssignmentObject;
import org.lammsecure.lammsecureamass.models.LAMMUserObject;
import org.lammsecure.lammsecureamass.utilities.ApplicationUtilities;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArduinoFragment#newInstance} method to
 * create an instance of this fragment.
 */
public class ArduinoFragment extends Fragment {

    private static final String ARG_ARDUINO_FRAGMENT_ID = "arduino_fragment_id";

    private String mArduinoID;
    private LAMMArduinoObject mArduinoObject;

    private TextInputEditText mArduinoNameEditText;
    private TextInputLayout mArduinoNameTextInputLayout;
    private TextView mArduinoActivationDateTextView, mArduinoDriverTextView;

    private DatabaseReference mArduinoDatabaseRef,
            mAssignmentsDatabaseRef,
            mUsersDatabaseRef;

    private ValueEventListener mArduinoRefListener,
            mAssignmentsRefListener,
            mUsersRefListener;

    /**
     * Required empty public constructor
     */
    public ArduinoFragment() {}

    /**
     * @param arduinoID the ID of the Arduino details to display.
     * @return A new instance of fragment ArduinoFragment.
     */
    public static ArduinoFragment newInstance(String arduinoID) {
        ArduinoFragment fragment = new ArduinoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARDUINO_FRAGMENT_ID, arduinoID);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the Bundle that was used to start this Fragment
        Bundle extras = getArguments();
        if (extras != null) {
            if (extras.containsKey(ARG_ARDUINO_FRAGMENT_ID)) {
                mArduinoID = extras.getString(ARG_ARDUINO_FRAGMENT_ID);
            }
        }
    }

    /**
     * Android lifecycle method
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_arduino, container, false);
        setUpViews(view);
        return view;
    }

    /**
     * Use this method to set up the views used in this Fragment
     */
    public void setUpViews(View rootView) {

        //ImageView oneSheeldImageView = (ImageView) rootView.findViewById(R.id.onesheeld_imageview);

        mArduinoActivationDateTextView = (TextView) rootView.findViewById(R.id.arduino_activation_date_textview);
        mArduinoDriverTextView = (TextView) rootView.findViewById(R.id.arduino_driver_name);

        mArduinoNameTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.input_layout_arduino_name);
        mArduinoNameEditText = (TextInputEditText) rootView.findViewById(R.id.arduino_name_edittext);
        mArduinoNameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        mArduinoNameEditText.clearFocus();
                        mArduinoNameTextInputLayout.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onResume() {
        super.onResume();

        if (mArduinoID != null) {
            if (!mArduinoID.isEmpty()) {
                mArduinoDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url)).child(getString(R.string.firebase_branch_arduinos)).child(mArduinoID);
                final String activatedString = getString(R.string.arduino_activation_date),
                        branchName = getString(R.string.firebase_branch_child_name),
                        nameCanNotBeBlank = getString(R.string.arduino_name_cannot_be_blank);

                mArduinoRefListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            mArduinoObject = dataSnapshot.getValue(LAMMArduinoObject.class);
                            if (mArduinoObject != null) {

                                mArduinoNameEditText.setText(mArduinoObject.getName());
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
                                            mArduinoNameTextInputLayout.setErrorEnabled(false);
                                            mArduinoNameTextInputLayout.setError(null);

                                            if (mArduinoObject != null) {
                                                mArduinoObject.setName(arduinoName);
                                                mArduinoDatabaseRef.child(branchName).setValue(arduinoName);
                                            }
                                        } else {
                                            mArduinoNameEditText.requestFocus();
                                            mArduinoNameTextInputLayout.setErrorEnabled(true);
                                            mArduinoNameTextInputLayout.setError(nameCanNotBeBlank);
                                        }
                                    }
                                });

                                String date = activatedString + ApplicationUtilities.convertTimeStampToDate(mArduinoObject.getActivationDate());
                                mArduinoActivationDateTextView.setText(date);
                            } else {
                                FirebaseCrash.report(new Exception("mArduinoDatabaseRef: onDataChange() Arduino found in database, error parsing into Java."));
                                Log.e(ArduinoFragment.class.getSimpleName(), "mArduinoDatabaseRef: onDataChange() Arduino found in database, error parsing into Java.");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        FirebaseCrash.report(new Exception("mArduinoDatabaseRef: onCancelled() Error reading from arduinos table: " + databaseError.toString()));
                        Log.e(ArduinoFragment.class.getSimpleName(), "mArduinoDatabaseRef: onCancelled() Error reading from arduinos table: " + databaseError.toString());
                    }
                };
                mArduinoDatabaseRef.addListenerForSingleValueEvent(mArduinoRefListener);

                mAssignmentsDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url)).child(getString(R.string.firebase_branch_assignments)).child(mArduinoID);
                final String firebaseUrl = getString(R.string.lamm_firebase_url),
                        users = getString(R.string.firebase_branch_users),
                        driverString = getString(R.string.arduino_driver);

                mAssignmentsRefListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Assignment found in assignments table in Firebase Database
                            LAMMAssignmentObject assignment = dataSnapshot.getValue(LAMMAssignmentObject.class);
                            if (assignment != null) {
                                // Get the user's UID
                                String assignedDriver = "";
                                for (String assigned : assignment.getUsers().keySet()) {
                                    assignedDriver = assigned;
                                }

                                // Get the user's name
                                mUsersDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseUrl).child(users).child(assignedDriver);
                                if (mUsersRefListener == null) {
                                    mUsersRefListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()) {
                                                // User found in users table in Firebase Database
                                                LAMMUserObject user = dataSnapshot.getValue(LAMMUserObject.class);
                                                if (user != null) {
                                                    String driver = driverString + user.getName();
                                                    mArduinoDriverTextView.setText(driver);
                                                } else {
                                                    FirebaseCrash.report(new Exception("mUsersDatabaseRef: onDataChange() User found in database, error parsing into Java."));
                                                    Log.e(ArduinoFragment.class.getSimpleName(), "mUsersDatabaseRef: onDataChange() User found in database, error parsing into Java.");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            FirebaseCrash.report(new Exception("mUsersDatabaseRef: onCancelled() Error reading from users table: " + databaseError.toString()));
                                            Log.e(ArduinoFragment.class.getSimpleName(), "mUsersDatabaseRef: onCancelled() Error reading from users table: " + databaseError.toString());
                                        }
                                    };
                                }
                                mUsersDatabaseRef.addListenerForSingleValueEvent(mUsersRefListener);
                            } else {
                                FirebaseCrash.report(new Exception("mAssignmentsDatabaseRef: onDataChange() Assignment found in database, error parsing into Java."));
                                Log.e(ArduinoFragment.class.getSimpleName(), "mAssignmentsDatabaseRef: onDataChange() Assignment found in database, error parsing into Java.");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        FirebaseCrash.report(new Exception("mAssignmentsDatabaseRef: onCancelled() Error reading from assignments table: " + databaseError.toString()));
                        Log.e(ArduinoFragment.class.getSimpleName(), "mAssignmentsDatabaseRef: onCancelled() Error reading from assignments table: " + databaseError.toString());
                    }
                };
                mAssignmentsDatabaseRef.addListenerForSingleValueEvent(mAssignmentsRefListener);
            }
        }
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onDetach() {
        super.onDetach();

        // Remove listeners
        if (mAssignmentsRefListener != null) {
            mAssignmentsDatabaseRef.removeEventListener(mAssignmentsRefListener);
            mAssignmentsRefListener = null;
        }

        if (mArduinoRefListener != null) {
            mArduinoDatabaseRef.removeEventListener(mArduinoRefListener);
            mArduinoRefListener = null;
        }

        if (mUsersRefListener != null) {
            if (mUsersDatabaseRef != null) {
                mUsersDatabaseRef.removeEventListener(mUsersRefListener);
            }
            mUsersRefListener = null;
        }
    }
}
