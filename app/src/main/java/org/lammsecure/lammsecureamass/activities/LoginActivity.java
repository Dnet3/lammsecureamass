package org.lammsecure.lammsecureamass.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.lammsecure.lammsecureamass.LAMMSecureApplication;
import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.base.BaseLoginActivity;
import org.lammsecure.lammsecureamass.fragments.SignUpFragment;
import org.lammsecure.lammsecureamass.models.LAMMAccountObject;
import org.lammsecure.lammsecureamass.models.LAMMUserObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Max on 21/2/17.
 *
 * An Activity for Authenticating the users Google account with the Firebase Database
 *
 * A simple {@link BaseLoginActivity} subclass.
 * Use the {@link LoginActivity#start}  method to
 * start this Activity.
 */

public class LoginActivity extends BaseLoginActivity implements View.OnClickListener,
        SignUpFragment.OnFragmentInteractionListener {

    private static final String SIGN_UP_CLICKED = "sign_up_clicked",
            SIGN_UP_FRAGMENT_TAG = "sign_up_fragment",
            AUTH_REGISTERED = "authentication_listener_registered";

    private static final int RC_SIGN_IN = 9001;

    private boolean mSignUpClicked, mAuthRegistered;

    private TextView mStatusTextView;

    private DatabaseReference mAccountDatabaseRef, mUsersDatabaseRef;
    private ValueEventListener mAccountRefListener, mUsersRefListener;

    /**
     * @return A new instance of activity LoginActivity that clears all other Activities in the stack.
     */
    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setup views and buttons
        mStatusTextView = (TextView) findViewById(R.id.status);

        Button mAlreadyRegistered = (Button) findViewById(R.id.already_registered_button);
        Button googleSignInButton = (Button) findViewById(R.id.sign_up_button);

        mAlreadyRegistered.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);

        // If this fragment has been created before
        if (savedInstanceState != null) {
            mSignUpClicked = savedInstanceState.getBoolean(SIGN_UP_CLICKED, false);
            mAuthRegistered = savedInstanceState.getBoolean(AUTH_REGISTERED, false);
        }
        else {
            mSignUpClicked = false;
            mAuthRegistered = false;
        }
    }

    /**
     * Google sign-in method
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(LAMMSecureApplication.getGoogleApiClientSingleton().getGoogleApiClient());
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Android lifecycle method
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SIGN_UP_CLICKED, mSignUpClicked);
        outState.putBoolean(AUTH_REGISTERED, mAuthRegistered);
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount googleAccount = result.getSignInAccount();
                firebaseAuthWithGoogle(googleAccount);
            } else {
                // Google Sign In failed, update UI appropriately
                updateUI(null);
            }
        }
    }

    /**
     * Firebase sign-in method
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.i(LoginActivity.class.getSimpleName(), "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        Log.i(LoginActivity.class.getSimpleName(), "FireBaseSignInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(LoginActivity.class.getSimpleName(), "FireBaseSignInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    /**
     * Firebase Authentication listener method
     */
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // User is authorized to use Firebase
            checkUserExistsInDatabase(firebaseUser);

            if(!mAuthRegistered) {
                mAuthRegistered = true;
            }
            else {
                if (mSignUpClicked) {
                    SignUpFragment signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag(SIGN_UP_FRAGMENT_TAG);
                    if (signUpFragment == null) {
                        LAMMUserObject userObject = new LAMMUserObject(new HashMap<String, Boolean>(), firebaseUser.getEmail(), firebaseUser.getDisplayName());
                        signUpFragment = SignUpFragment.newInstance(userObject, firebaseUser.getUid());
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.activity_login_fragment_container, signUpFragment, SIGN_UP_FRAGMENT_TAG);
                        fragmentTransaction.addToBackStack(SIGN_UP_FRAGMENT_TAG);
                        fragmentTransaction.commit();
                    }
                }
            }
        }

        updateUI(firebaseUser);
    }

    /**
     * Firebase and Google sign-out method
     */
    private void signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(LAMMSecureApplication.getGoogleApiClientSingleton().getGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        onBackPressed();
                        updateUI(null);
                    }
                });
    }

    /**
     * Change UI elements to reflect signed in status
     */
    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            // Signed in
            findViewById(R.id.activity_user_progress_bar).setVisibility(View.VISIBLE);
            mStatusTextView.setText(null);
        } else {
            // Signed out
            findViewById(R.id.activity_user_progress_bar).setVisibility(View.GONE);
            mStatusTextView.setText(R.string.signed_out);
        }
    }

    /**
     * View clicked listener method
     */
    @Override
    public void onClick(View view) {
        int iD = view.getId();
        if (iD == R.id.sign_up_button) {
            mSignUpClicked = true;
            signIn();
        }
        else if (iD == R.id.already_registered_button) {
            mSignUpClicked = false;
            signIn();
        }
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

        if (mUsersRefListener != null) {
            if (mUsersDatabaseRef != null) {
                mUsersDatabaseRef.removeEventListener(mUsersRefListener);
            }
            mUsersRefListener = null;
        }
    }

    /**
     * Check Firebase Database to see if a user already exists
     */
    public void checkUserExistsInDatabase(final FirebaseUser firebaseUser) {

        // Check if the user already exists:
        mUsersDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url)).child(getString(R.string.firebase_branch_users)).child(firebaseUser.getUid());
        mUsersRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // User is in users table in Firebase Database
                    LAMMUserObject userObject = snapshot.getValue(LAMMUserObject.class);

                    if (userObject != null) {
                        if (userObject.getAccounts() != null) {
                            if (userObject.getAccounts().keySet().size() < 1) {
                                // If user is has no account
                                SignUpFragment signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag(SIGN_UP_FRAGMENT_TAG);
                                if (signUpFragment == null) {
                                    signUpFragment = SignUpFragment.newInstance(getString(R.string.user_does_not_have_account), userObject, firebaseUser.getUid());
                                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.add(R.id.activity_login_fragment_container, signUpFragment, SIGN_UP_FRAGMENT_TAG);
                                    fragmentTransaction.addToBackStack(SIGN_UP_FRAGMENT_TAG);
                                    fragmentTransaction.commit();
                                }
                            }
                            else if (userObject.getAccounts().keySet().size() == 1) {
                                // If user is is associated with only one account
                                String name = "";
                                for (String account : userObject.getAccounts().keySet()) {
                                    name = account;
                                }
                                final String accountName = name;
                                mAccountDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url)).child(getString(R.string.firebase_branch_accounts)).child(accountName);

                                mAccountRefListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Account is in accounts table in Firebase Database
                                            LAMMAccountObject accountObject = dataSnapshot.getValue(LAMMAccountObject.class);
                                            if (accountObject != null) {
                                                MainActivity.start(LoginActivity.this, accountName);
                                                finish();
                                            }
                                            else {
                                                FirebaseCrash.report(new Exception("mAccountDatabaseRef: User found, error parsing account into Java."));
                                                Log.e(LoginActivity.class.getSimpleName(), "mAccountDatabaseRef: User found, error parsing account into Java.");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        FirebaseCrash.report(new Exception("mAccountDatabaseRef: onCancelled() User found, error reading from accounts table. " + databaseError.toString()));
                                        Log.e(LoginActivity.class.getSimpleName(), "mAccountDatabaseRef: onCancelled() User found, error reading from accounts table." + databaseError.toString());
                                    }
                                };

                                mAccountDatabaseRef.addListenerForSingleValueEvent(mAccountRefListener);
                            }
                            else {
                                // If user is associated with more than one account, show a dialog asking to choose one.
                                List<String> accounts = new ArrayList<>();
                                for (String account : userObject.getAccounts().keySet()) {
                                    accounts.add(account);
                                }
                                // To be completed..............
                            }
                        }
                    }
                    else {
                        FirebaseCrash.report(new Exception("mUsersDatabaseRef: onDataChange() UserObject was null."));
                        Log.e(LoginActivity.class.getSimpleName(), "mUsersDatabaseRef: onDataChange() UserObject was null.");
                    }
                }
                else {
                    // User does not exist in users table in Firebase Database
                    Log.d(LoginActivity.class.getSimpleName(), "mUsersDatabaseRef: User does not exist.");
                    SignUpFragment signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag(SIGN_UP_FRAGMENT_TAG);
                    if (signUpFragment == null) {
                        LAMMUserObject userObject = new LAMMUserObject(new HashMap<String, Boolean>(), firebaseUser.getEmail(), firebaseUser.getDisplayName());
                        signUpFragment = SignUpFragment.newInstance(getString(R.string.user_does_not_exist_in_db), userObject, firebaseUser.getUid());
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.activity_login_fragment_container, signUpFragment, SIGN_UP_FRAGMENT_TAG);
                        fragmentTransaction.addToBackStack(SIGN_UP_FRAGMENT_TAG);
                        fragmentTransaction.commit();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseCrash.report(new Exception("mUsersDatabaseRef: onCancelled() Error reading users table: " + databaseError.toString()));
                Log.e(LoginActivity.class.getSimpleName(), "mUsersDatabaseRef: onCancelled() Error reading users table: " + databaseError.toString());
            }
        };
        mUsersDatabaseRef.addListenerForSingleValueEvent(mUsersRefListener);
    }

    /**
     * SignUpFragment listener method
     */
    @Override
    public void onAccountCreated(String accountName) {
        MainActivity.start(LoginActivity.this, accountName);
        finish();
    }

    /**
     * SignUpFragment listener method
     */
    @Override
    public void onDisconnectAccountClicked() {
        signOut();
    }
}
