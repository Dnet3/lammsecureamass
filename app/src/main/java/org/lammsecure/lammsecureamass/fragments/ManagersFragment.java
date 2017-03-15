package org.lammsecure.lammsecureamass.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.adapters.PersonRecyclerViewAdapter;
import org.lammsecure.lammsecureamass.listeners.RecyclerViewTouchListener;
import org.lammsecure.lammsecureamass.models.LAMMUserObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManagersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManagersFragment#newInstance} method to
 * create an instance of this fragment.
 */
public class ManagersFragment extends Fragment {

    private static final String ARG_ACCOUNT_NAME = "account_name",
            RECYCLER_SAVED_STATE = "archive_recycler_saved_state";

    private String mAccountName;
    private ArrayList<String> mManagerIds;
    private ArrayList<String> mManagerNames;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager.SavedState mRecyclerViewLayoutSavedState;
    private PersonRecyclerViewAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mUserTextView;

    private DatabaseReference mManagersDatabaseRef, mUserDatabaseRef;
    private ValueEventListener mManagersRefListener, mUserRefListener;

    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public ManagersFragment() {}

    /**
     * @param accountName the name of the Account details to display.
     * @return A new instance of fragment ManagersFragment.
     */
    public static ManagersFragment newInstance(String accountName) {
        ManagersFragment fragment = new ManagersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT_NAME, accountName);
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
        Bundle args = getArguments();
        if (args != null) {
            mAccountName = args.getString(ARG_ACCOUNT_NAME);
        }

        if (savedInstanceState != null) {
            mRecyclerViewLayoutSavedState = savedInstanceState.getParcelable(RECYCLER_SAVED_STATE);
        }
    }

    /**
     * Android lifecycle method
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_managers, container, false);
        setUpViews(view);
        return view;
    }

    /**
     * Use this method to set up the views used in this Fragment
     */
    private void setUpViews(View rootView) {
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_managers);

        mUserTextView = (TextView) rootView.findViewById(R.id.textview_username);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_managers);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new PersonRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), new RecyclerViewTouchListener.RecyclerViewClickListener() {

            @Override
            public void onClick(View view, final int position) {
                Toast.makeText(getActivity(), "Delete manager: " + mManagerNames.get(position) + ", UID=" + mManagerIds.get(position), Toast.LENGTH_SHORT).show();
            }
        }));

        FloatingActionButton addDriverButton = (FloatingActionButton) rootView.findViewById(R.id.button_add_manager);

        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddManagerClicked();
            }
        });
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onResume() {
        super.onResume();

        if (mAccountName != null) {
            if (!mAccountName.isEmpty()) {
                mManagersDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url)).child(getString(R.string.firebase_branch_accounts)).child(mAccountName).child(getString(R.string.firebase_branch_managers));
                final String noManagers = getString(R.string.error_no_managers),
                        firebaseUrl = getString(R.string.lamm_firebase_url),
                        usersBranch = getString(R.string.firebase_branch_users);

                mManagersRefListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            // Account has Managers/s
                            final HashMap<String, Boolean> managers = (HashMap<String, Boolean>) snapshot.getValue();

                            if (managers != null) {
                                mManagerIds = new ArrayList<>();
                                mManagerNames = new ArrayList<>();

                                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                if (firebaseUser != null) {
                                    for (final String managerID : managers.keySet()) {
                                        mUserDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseUrl).child(usersBranch).child(managerID);
                                        mUserRefListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    LAMMUserObject user = snapshot.getValue(LAMMUserObject.class);
                                                    if (user != null) {
                                                        if (!managerID.equals(firebaseUser.getUid())) {
                                                            mManagerIds.add(managerID);
                                                            mManagerNames.add(user.getName());
                                                            mAdapter.setPeople(mManagerNames);
                                                        } else {
                                                            mUserTextView.setText(user.getName());
                                                        }
                                                        mProgressBar.setVisibility(View.GONE);
                                                    } else {
                                                        FirebaseCrash.report(new Exception("mUserDatabaseRef: onDataChange() Error parsing user into Java."));
                                                        Log.e(ManagersFragment.class.getSimpleName(), "mUserDatabaseRef: onDataChange() Error parsing user into Java.");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                FirebaseCrash.report(new Exception("mUserDatabaseRef: onCancelled() Error reading from journeys table: " + databaseError.toString()));
                                                Log.e(ManagersFragment.class.getSimpleName(), "mUserDatabaseRef: Error reading users table: " + databaseError.toString());
                                            }
                                        };
                                        mUserDatabaseRef.addListenerForSingleValueEvent(mUserRefListener);
                                    }
                                }
                            } else {
                                FirebaseCrash.report(new Exception("mManagersDatabaseRef: onDataChange() Error parsing account managers into Java."));
                                Log.e(ManagersFragment.class.getSimpleName(), "mManagersDatabaseRef: onDataChange() Error parsing account managers into Java.");
                            }
                        } else {
                            // Account has no Managers
                            mUserTextView.setText(noManagers);
                            Log.e(ManagersFragment.class.getSimpleName(), "mManagersDatabaseRef: onDataChange() Account has no managers.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        FirebaseCrash.report(new Exception("mManagersDatabaseRef: onCancelled() Error reading from account drivers branch: " + databaseError.toString()));
                        Log.e(DriversFragment.class.getSimpleName(), "mManagersDatabaseRef: onCancelled() Error reading from account drivers branch: " + databaseError.toString());
                    }
                };
                mManagersDatabaseRef.addListenerForSingleValueEvent(mManagersRefListener);
            }
        }

        if (mRecyclerViewLayoutSavedState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerViewLayoutSavedState);
        }
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        if (mManagersRefListener != null) {
            if (mManagersDatabaseRef != null) {
                mManagersDatabaseRef.removeEventListener(mManagersRefListener);
                mManagersRefListener = null;
            }
        }

        if (mUserRefListener != null) {
            if (mUserDatabaseRef != null) {
                mUserDatabaseRef.removeEventListener(mUserRefListener);
                mUserRefListener = null;
            }
        }
    }

    /**
     * Fragment interaction listener
     */
    public interface OnFragmentInteractionListener {
        void onAddManagerClicked();
    }
}
