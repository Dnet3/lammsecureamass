package org.lammsecure.lammsecureamass.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lammsecure.lammsecureamass.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArduinoChoiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NothingToSeeHereFragment#newInstance} method to
 * create an instance of this fragment.
 */
public class NothingToSeeHereFragment extends Fragment {

    private static final String ARG_ARDUINO_BUTTON = "arduino_button";

    private boolean mShowArduinoButton;

    private ArduinoChoiceFragment.OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public NothingToSeeHereFragment() {}

    /**
     * @param addArduinoButton boolean to control showing the AddArduino button.
     * @return A new instance of fragment NothingToSeeHereFragment.
     */
    public static NothingToSeeHereFragment newInstance(boolean addArduinoButton) {
        NothingToSeeHereFragment fragment = new NothingToSeeHereFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_ARDUINO_BUTTON, addArduinoButton);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ArduinoChoiceFragment.OnFragmentInteractionListener) {
            mListener = (ArduinoChoiceFragment.OnFragmentInteractionListener) context;
        }
    }

    /**
     * Android lifecycle method
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mShowArduinoButton = false;

        Bundle args = getArguments();
        if (args != null) {
            mShowArduinoButton = args.getBoolean(ARG_ARDUINO_BUTTON, false);
        }
    }

    /**
     * Android lifecycle method
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nothing_to_see_here, container, false);
        setUpViews(view);
        return view;
    }

    /**
     * Use this method to set up the views used in this Fragment
     */
    public void setUpViews(View rootView) {

        if (mShowArduinoButton) {
            FloatingActionButton addArduinoButton = (FloatingActionButton) rootView.findViewById(R.id.button_add_arduino);
            addArduinoButton.setVisibility(View.VISIBLE);
            addArduinoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onAddArduinoClicked();
                    }
                }
            });
        }

    }
}
