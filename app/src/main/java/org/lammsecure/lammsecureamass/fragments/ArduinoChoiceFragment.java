package org.lammsecure.lammsecureamass.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.activities.ArduinoActivity;
import org.lammsecure.lammsecureamass.adapters.ArduinoChoiceRecyclerViewAdapter;
import org.lammsecure.lammsecureamass.listeners.RecyclerViewTouchListener;
import org.lammsecure.lammsecureamass.models.LAMMArduinoObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArduinoChoiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArduinoChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArduinoChoiceFragment extends Fragment {

    private static final String ARG_ACCOUNT_NAME = "account_name",
            RECYCLER_SAVED_STATE = "archive_recycler_saved_state";

    private RecyclerView mRecyclerView;
    private ArduinoChoiceRecyclerViewAdapter mAdapter;
    private ProgressBar mProgressBar;

    private String mAccountName;
    private ArrayList<String> mArduinoIDs;
    private ArrayList<LAMMArduinoObject> mArduinoObjects;

    private GridLayoutManager.SavedState mRecyclerViewLayoutSavedState;
    private OnFragmentInteractionListener mListener;

    private DatabaseReference mAccountArduinosDatabaseRef, mArduinosDatabaseRef;
    private ValueEventListener mAccountArduinosRefListener, mArduinosRefListener;

    /**
     * Required empty public constructor
     */
    public ArduinoChoiceFragment() {}

    /**
     * @param accountName the name of the Account details to display.
     * @return A new instance of fragment ArduinoChoiceFragment.
     */
    public static ArduinoChoiceFragment newInstance(String accountName) {
        ArduinoChoiceFragment fragment = new ArduinoChoiceFragment();
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
        View view = inflater.inflate(R.layout.fragment_arduino_choice, container, false);
        setUpViews(view);
        return view;
    }

    /**
     * Use this method to set up the views used in this Fragment
     */
    public void setUpViews(View rootView) {

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_arduino_choice);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.arduinos_recycler_view);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }

        mAdapter = new ArduinoChoiceRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), new RecyclerViewTouchListener.RecyclerViewClickListener() {

            @Override
            public void onClick(View view, final int position) {
                ArduinoActivity.start(getActivity(), mArduinoIDs.get(position), mAccountName);
            }
        }));

        FloatingActionButton addArduinoButton = (FloatingActionButton) rootView.findViewById(R.id.button_add_arduino);
        addArduinoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddArduinoClicked();
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
                mAccountArduinosDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url)).child(getString(R.string.firebase_branch_accounts)).child(mAccountName).child(getString(R.string.firebase_branch_arduinos));
                final String firebaseUrl = getString(R.string.lamm_firebase_url),
                        arduinosBranch = getString(R.string.firebase_branch_arduinos);

                mAccountArduinosRefListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Account is in accounts table in Firebase Database
                            HashMap<String, Boolean> arduinos = (HashMap<String, Boolean>) dataSnapshot.getValue();

                            if (arduinos != null) {
                                mArduinoIDs = new ArrayList<>();
                                mArduinoObjects  = new ArrayList<>();

                                for (String arduinoID :arduinos.keySet()) {
                                    mArduinoIDs.add(arduinoID);
                                    mArduinosDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseUrl).child(arduinosBranch).child(arduinoID);
                                    mArduinosRefListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // Account is in accounts table in Firebase Database
                                                LAMMArduinoObject arduinoObject = dataSnapshot.getValue(LAMMArduinoObject.class);
                                                if (arduinoObject != null) {
                                                    mArduinoObjects.add(arduinoObject);
                                                    if (mAdapter != null) {
                                                        mProgressBar.setVisibility(View.GONE);
                                                        mAdapter.setArduinos(mArduinoObjects, mArduinoIDs);
                                                    }
                                                } else {
                                                    FirebaseCrash.report(new Exception("mArduinosDatabaseRef: onDataChange() Arduino found in database, error parsing into Java."));
                                                    Log.e(ArduinoChoiceFragment.class.getSimpleName(), "mArduinosDatabaseRef: onDataChange() Arduino found in database, error parsing into Java.");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            FirebaseCrash.report(new Exception("mArduinosDatabaseRef: onCancelled() Error reading from arduinos table." + databaseError.toString()));
                                            Log.e(ArduinoChoiceFragment.class.getSimpleName(), "FirebaseDatabaseReference: Error reading from arduinos table." + databaseError.toString());
                                        }
                                    };
                                    mArduinosDatabaseRef.addListenerForSingleValueEvent(mArduinosRefListener);
                                }
                            }
                            else {
                                FirebaseCrash.report(new Exception("mAccountArduinosDatabaseRef: onDataChange() Arduino HashMap found in database, error parsing into Java."));
                                Log.e(ArduinoChoiceFragment.class.getSimpleName(), "mAccountArduinosDatabaseRef: onDataChange() Arduino HashMap found in database, error parsing into Java.");
                            }
                        }
                        else {
                            mListener.hasNoArduinos();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        FirebaseCrash.report(new Exception("mAccountArduinosDatabaseRef: onCancelled() Error reading from account arduinos branch." + databaseError.toString()));
                        Log.d(ArduinoChoiceFragment.class.getSimpleName(), "mAccountArduinosDatabaseRef: onCancelled() Error reading from account arduinos branch." + databaseError.toString());
                    }
                };
                mAccountArduinosDatabaseRef.addListenerForSingleValueEvent(mAccountArduinosRefListener);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_SAVED_STATE, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onDetach() {
        super.onDetach();
        // Remove listeners
        mListener = null;

        // Remove listeners
        if (mAccountArduinosRefListener != null) {
            if (mAccountArduinosDatabaseRef != null) {
                mAccountArduinosDatabaseRef.removeEventListener(mAccountArduinosRefListener);
            }
            mAccountArduinosRefListener = null;
        }

        if (mArduinosRefListener != null) {
            if (mArduinosDatabaseRef != null) {
                mArduinosDatabaseRef.removeEventListener(mArduinosRefListener);
            }
            mArduinosRefListener = null;
        }
    }

    /**
     * Fragment interaction listener
     */
    public interface OnFragmentInteractionListener {
        void onAddArduinoClicked();
        void hasNoArduinos();
    }
}
