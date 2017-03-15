package org.lammsecure.lammsecureamass.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.adapters.JourneysRecyclerViewAdapter;
import org.lammsecure.lammsecureamass.listeners.RecyclerViewTouchListener;
import org.lammsecure.lammsecureamass.models.LAMMJourneyObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JourneyHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JourneyHistoryFragment#newInstance} method to
 * create an instance of this fragment.
 */
public class JourneyHistoryFragment extends Fragment {

    private static final String ARG_ARDUINO_ID = "arduino_id",
            RECYCLER_SAVED_STATE = "archive_recycler_saved_state";

    private RecyclerView mRecyclerView;
    private GridLayoutManager.SavedState mRecyclerViewLayoutSavedState;
    private JourneysRecyclerViewAdapter mAdapter;
    private ProgressBar mProgressBar;

    private DatabaseReference mJourneysDatabaseRef;
    private ValueEventListener mJourneysRefListener;

    private String mArduinoID;
    private ArrayList<LAMMJourneyObject> mJourneys;

    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public JourneyHistoryFragment() {}

    /**
     * @param arduinoID the ID of the Arduino details to display.
     * @return A new instance of fragment JourneyHistoryFragment.
     */
    public static JourneyHistoryFragment newInstance(String arduinoID) {
        JourneyHistoryFragment fragment = new JourneyHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARDUINO_ID, arduinoID);
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

        Bundle extras = getArguments();
        if (extras != null) {
            mArduinoID = extras.getString(ARG_ARDUINO_ID);
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
        View view = inflater.inflate(R.layout.fragment_journey_history, container, false);
        setUpViews(view);
        return view;
    }

    /**
     * Use this method to set up the views used in this Fragment
     */
    private void setUpViews(View rootView) {
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_journeys);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_journeys);
        mRecyclerView.setHasFixedSize(true);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }

        mAdapter = new JourneysRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), new RecyclerViewTouchListener.RecyclerViewClickListener() {

            @Override
            public void onClick(View view, final int position) {
                Toast.makeText(getActivity(), "This item will open a more detailed journey info activity.", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onResume() {
        super.onResume();

        if (mArduinoID != null) {
            if (!mArduinoID.isEmpty()) {

                // Get the image captures for this Arduino
                mJourneys = new ArrayList<>();

                mJourneysDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.lamm_firebase_url)).child(getString(R.string.firebase_branch_journeys)).child(mArduinoID);
                mJourneysRefListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Journeys found in journeys table in Firebase Database
                            for (DataSnapshot journeySnapshot: dataSnapshot.getChildren()) {
                                LAMMJourneyObject journey = journeySnapshot.getValue(LAMMJourneyObject.class);
                                if (journey != null) {
                                    mJourneys.add(journey);
                                }
                                else {
                                    FirebaseCrash.report(new Exception("mJourneysDatabaseRef: onDataChange() Journey found in database, error parsing into Java."));
                                    Log.e(JourneyHistoryFragment.class.getSimpleName(), "mJourneysDatabaseRef: onDataChange() Journey found in database, error parsing into Java.");
                                }
                            }
                            mProgressBar.setVisibility(View.GONE);
                            mAdapter.setJourneys(mJourneys);
                        }
                        else {
                            mListener.noJourneyFound();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        FirebaseCrash.report(new Exception("mJourneysDatabaseRef: onCancelled() Error reading from journeys table: " + databaseError.toString()));
                        Log.e(JourneyHistoryFragment.class.getSimpleName(), "mJourneysDatabaseRef: onCancelled() Error reading from journeys table: " + databaseError.toString());
                        mListener.noJourneyFound();
                    }
                };
                mJourneysDatabaseRef.addListenerForSingleValueEvent(mJourneysRefListener);
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
        mListener = null;

        // Remove listeners
        if (mJourneysRefListener != null) {
            mJourneysDatabaseRef.removeEventListener(mJourneysRefListener);
            mJourneysRefListener = null;
        }
    }

    /**
     * Fragment interaction listener
     */
    public interface OnFragmentInteractionListener {
        void noJourneyFound();
    }
}
