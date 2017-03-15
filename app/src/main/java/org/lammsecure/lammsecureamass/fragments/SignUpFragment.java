package org.lammsecure.lammsecureamass.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.models.LAMMAccountObject;
import org.lammsecure.lammsecureamass.models.LAMMArduinoObject;
import org.lammsecure.lammsecureamass.models.LAMMUserObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  Created by Max on 8/3/17.
 *
 *  For signing up new users and creating accounts
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_ERROR_SIGNING_IN = "error_signing_in",
            ARG_USER_OBJECT = "user_object",
            ARG_UID = "uid",
            ARG_ACC_NAME = "account_name",
            ARG_ACC_TYPE = "account_type",
            ARG_ACC_ADD = "account_address",
            ARG_ACC_PHO = "account_phone_number",
            NAME_IN_USE = "name_in_use";

    private TextInputEditText mAccountNameEditText, mAddressEditText, mPhoneNumberEditText;
    private TextInputLayout mAccountNameLayout, mAddressLayout, mPhoneNumberLayout;
    private Spinner mAccountTypeSpinner;

    private String mError, mUID;
    private boolean mAccountNameInUse;

    private LAMMUserObject mUserObject;

    private DatabaseReference mAccountsDatabaseRef;
    private ValueEventListener mAccountsRefListener;
    private DatabaseReference mDatabaseRef;

    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public SignUpFragment() {}

    /**
     * @param userObject the User object to be signed up.
     * @param userID the UID of the user to sign up.
     * @return A new instance of fragment NothingToSeeHereFragment.
     */
    public static SignUpFragment newInstance(LAMMUserObject userObject, String userID) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER_OBJECT, userObject);
        args.putString(ARG_UID, userID);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param error an error string.
     * @param userObject the User object to be signed up.
     * @param userID the UID of the user to sign up.
     * @return A new instance of fragment NothingToSeeHereFragment.
     */
    public static SignUpFragment newInstance(String error, LAMMUserObject userObject, String userID) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ERROR_SIGNING_IN, error);
        args.putParcelable(ARG_USER_OBJECT, userObject);
        args.putString(ARG_UID, userID);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url));

        Bundle args = getArguments();

        if (args != null) {
            mError = args.getString(ARG_ERROR_SIGNING_IN);
            mUserObject = args.getParcelable(ARG_USER_OBJECT);
            mUID = args.getString(ARG_UID);
        }

        if (savedInstanceState != null) {
            mAccountNameInUse = savedInstanceState.getBoolean(NAME_IN_USE);
        }
        else {
            mAccountNameInUse = false;
        }
    }

    /**
     * Android lifecycle method
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        setUpViews(rootView, savedInstanceState);
        return rootView;
    }

    /**
     * Use this method to set up the views used in this Fragment
     */
    public void setUpViews(View rootView, Bundle savedInstanceState) {

        if (mError != null) {
            TextView errorTextView = (TextView) rootView.findViewById(R.id.error_text_view);
            errorTextView.setText(mError);
            errorTextView.setVisibility(View.VISIBLE);
        }

        TextView googleAccountTextView = (TextView) rootView.findViewById(R.id.google_account_text_view);
        googleAccountTextView.setText(mUserObject.getEmailAddress());

        mAccountTypeSpinner = (Spinner) rootView.findViewById(R.id.account_type_spinner);
        List<String> accountTypes = new ArrayList<>();
        accountTypes.add(getString(R.string.account_enterprise));
        accountTypes.add(getString(R.string.account_private));
        ArrayAdapter<String> accountTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, accountTypes);
        accountTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAccountTypeSpinner.setAdapter(accountTypeSpinnerAdapter);

        mAccountNameEditText = (TextInputEditText) rootView.findViewById(R.id.account_name_edittext);
        mAddressEditText = (TextInputEditText) rootView.findViewById(R.id.address_edittext);
        mPhoneNumberEditText = (TextInputEditText) rootView.findViewById(R.id.phone_number_edittext);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_ACC_NAME)) {
                mAccountNameEditText.setText(savedInstanceState.getString(ARG_ACC_NAME));
            }
            if (savedInstanceState.containsKey(ARG_ACC_TYPE)) {
                mAccountTypeSpinner.setSelection(savedInstanceState.getInt(ARG_ACC_TYPE, 1));
            }
            if (savedInstanceState.containsKey(ARG_ACC_ADD)) {
                mAddressEditText.setText(savedInstanceState.getString(ARG_ACC_ADD));
            }
            if (savedInstanceState.containsKey(ARG_ACC_PHO)) {
                mPhoneNumberEditText.setText(savedInstanceState.getString(ARG_ACC_PHO));
            }
        }
        else {
            mAccountTypeSpinner.setSelection(1);
        }

        mAccountNameLayout = (TextInputLayout) rootView.findViewById(R.id.input_layout_account_name);
        mAddressLayout = (TextInputLayout) rootView.findViewById(R.id.input_layout_address);
        mPhoneNumberLayout = (TextInputLayout) rootView.findViewById(R.id.input_layout_phone_number);

        Button disconnectAccountButton = (Button) rootView.findViewById(R.id.disconnect_account_button);
        Button saveDetailsButton = (Button) rootView.findViewById(R.id.save_account_details_button);

        addListeners(disconnectAccountButton, saveDetailsButton);
    }

    /**
     * Use this method to add listeners to the views used in this Fragment
     */
    private void addListeners(Button disconnect, Button saveDetails) {

        disconnect.setOnClickListener(this);
        saveDetails.setOnClickListener(this);

        mAccountNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Check if the account name already exists:
                String accountName = mAccountNameEditText.getText().toString();
                if (!accountName.isEmpty()) {
                    mAccountsDatabaseRef = mDatabaseRef.child(getString(R.string.firebase_branch_accounts)).child(mAccountNameEditText.getText().toString());
                    mAccountsRefListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                // Account is already in the accounts table in Firebase Database
                                mAccountNameInUse = true;
                                mAccountNameEditText.requestFocus();
                                mAccountNameLayout.setErrorEnabled(true);
                                mAccountNameLayout.setError(getString(R.string.username_taken));
                            }
                            else {
                                mAccountNameInUse = false;
                                mAccountNameLayout.setErrorEnabled(false);
                                mAccountNameLayout.setError(null);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            FirebaseCrash.report(new Exception("mAccountsDatabaseRef: onCancelled() Error reading from accounts table: " + databaseError.toString()));
                            Log.d(SignUpFragment.class.getSimpleName(), "mAccountsDatabaseRef: onCancelled() Error reading accounts table: " + databaseError.toString());
                        }
                    };
                    mAccountsDatabaseRef.addListenerForSingleValueEvent(mAccountsRefListener);
                }
                else {
                    mAccountNameEditText.requestFocus();
                    mAccountNameLayout.setErrorEnabled(true);
                    mAccountNameLayout.setError(getString(R.string.account_name_connot_be_blank));
                }
            }
        });

        mAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String accountAddress = mAddressEditText.getText().toString();
                if (!accountAddress.isEmpty()) {
                    mAddressLayout.setErrorEnabled(false);
                    mAddressLayout.setError(null);
                }
                else {
                    mAddressLayout.setErrorEnabled(true);
                    mAddressLayout.setError(getString(R.string.address_cannot_be_blank));
                }
            }
        });

        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phoneNumber = mPhoneNumberEditText.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    mPhoneNumberLayout.setErrorEnabled(false);
                    mPhoneNumberLayout.setError(null);
                }
                else {
                    mPhoneNumberLayout.setErrorEnabled(true);
                    mPhoneNumberLayout.setError(getString(R.string.phone_number_cannot_be_blank));
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

        String accountName = mAccountNameEditText.getText().toString();
        if (!accountName.isEmpty()) {
            outState.putString(ARG_ACC_NAME, accountName);
        }

        outState.putInt(ARG_ACC_TYPE, mAccountTypeSpinner.getSelectedItemPosition());

        String accountAddress = mAddressEditText.getText().toString();
        if (!accountAddress.isEmpty()) {
            outState.putString(ARG_ACC_ADD, accountAddress);
        }

        String accountPhone = mPhoneNumberEditText.getText().toString();
        if (!accountPhone.isEmpty()) {
            outState.putString(ARG_ACC_PHO, accountPhone);
        }

        outState.putBoolean(NAME_IN_USE, mAccountNameInUse);
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        if (mAccountsRefListener != null) {
            if (mAccountsDatabaseRef != null) {
                mAccountsDatabaseRef.removeEventListener(mAccountsRefListener);
            }
            mAccountsRefListener = null;
        }
    }

    /**
     * View clicked listener method
     */
    @Override
    public void onClick(View view) {
        int iD = view.getId();
        if (iD == R.id.disconnect_account_button) {
            mListener.onDisconnectAccountClicked();
        }
        else if (iD == R.id.save_account_details_button) {

            String accountName = mAccountNameEditText.getText().toString();
            String accountType;
            if (mAccountTypeSpinner.getSelectedItemPosition() == 0) {
                accountType = getString(R.string.account_enterprise);
            }
            else {
                accountType = getString(R.string.account_private);
            }
            String accountAddress = mAddressEditText.getText().toString();
            String accountPhoneNumber = mPhoneNumberEditText.getText().toString();

            if (!accountName.isEmpty()) {
                if (!accountAddress.isEmpty()) {
                    if (!accountPhoneNumber.isEmpty()) {
                        if (!mAccountNameInUse) {
                            // Send a User object to the database
                            HashMap<String, Boolean> accounts = new HashMap<>();
                            accounts.put(accountName, true);
                            mUserObject.setAccounts(accounts);
                            mDatabaseRef.child(getString(R.string.firebase_branch_users)).child(mUID).setValue(mUserObject);

                            // Send an Account object to the database
                            HashMap<String, Boolean> arduinos = new HashMap<>();
                            HashMap<String, Boolean> drivers = new HashMap<>();
                            drivers.put(mUID, true);
                            HashMap<String, Boolean> managers = new HashMap<>();
                            managers.put(mUID, true);
                            LAMMAccountObject accountObject = new LAMMAccountObject(accountType, accountAddress, arduinos, drivers, managers, accountPhoneNumber);
                            mDatabaseRef.child(getString(R.string.firebase_branch_accounts)).child(accountName).setValue(accountObject);

                            mListener.onAccountCreated(accountName);
                        }
                    }
                    else {
                        mPhoneNumberEditText.requestFocus();
                        mPhoneNumberLayout.setErrorEnabled(true);
                        mPhoneNumberLayout.setError(getString(R.string.phone_number_cannot_be_blank));
                    }
                }
                else {
                    mAddressEditText.requestFocus();
                    mAddressLayout.setErrorEnabled(true);
                    mAddressLayout.setError(getString(R.string.address_cannot_be_blank));
                }
            }
            else {
                mAccountNameEditText.requestFocus();
                mAccountNameLayout.setErrorEnabled(true);
                mAccountNameLayout.setError(getString(R.string.account_name_connot_be_blank));
            }
        }
    }

    /**
     * Fragment interaction listener
     */
    public interface OnFragmentInteractionListener {
        void onAccountCreated(String accountName);
        void onDisconnectAccountClicked();
    }
}
