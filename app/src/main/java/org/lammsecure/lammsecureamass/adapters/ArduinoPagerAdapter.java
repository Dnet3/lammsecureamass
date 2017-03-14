package org.lammsecure.lammsecureamass.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.lammsecure.lammsecureamass.fragments.ArduinoFragment;
import org.lammsecure.lammsecureamass.fragments.ImageCaptureHistoryFragment;
import org.lammsecure.lammsecureamass.fragments.JourneyHistoryFragment;
import org.lammsecure.lammsecureamass.fragments.NothingToSeeHereFragment;

/**
 * Created by Max on 11/3/17.
 *
 * A ViewPager adapter for LAMM Secure Arduino fragments
 */

public class ArduinoPagerAdapter extends FragmentStatePagerAdapter {

    private String mArduinoID;
    private boolean mHasImageCaptures, mHasJourneys;

    /**
     * Constructor
     */
    public ArduinoPagerAdapter(FragmentManager fm, String arduinoID) {
        super(fm);
        mArduinoID = arduinoID;
        mHasImageCaptures = true;
        mHasJourneys = true;
    }

    /**
     * Tell the adapter that this Arduino has no image captures
     */
    public void setImageCaptures(boolean hasImageCaptures) {
        mHasImageCaptures = hasImageCaptures;
        notifyDataSetChanged();
    }

    /**
     * Tell the adapter that this Arduino has no journeys
     */
    public void setJourneys(boolean hasJourneys) {
        mHasJourneys = hasJourneys;
        notifyDataSetChanged();
    }

    /**
     * Adapter method to retrieve Fragments to be put into the Viewpager
     */
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (mArduinoID != null) {
                    return ArduinoFragment.newInstance(mArduinoID);
                }
                else {
                    return new NothingToSeeHereFragment();
                }
            case 1:
                if (mArduinoID != null && mHasImageCaptures) {
                    return ImageCaptureHistoryFragment.newInstance(mArduinoID);
                }
                else {
                    return new NothingToSeeHereFragment();
                }
            case 2:
                if (mArduinoID != null && mHasJourneys) {
                    return JourneyHistoryFragment.newInstance(mArduinoID);
                }
                else {
                    return new NothingToSeeHereFragment();
                }
            default:
                return null;
        }
    }

    /**
     * Adapter method which determines whether or not to reload the existing Fragments
     */
    @Override
    public int getItemPosition(Object object) {

        if (mArduinoID != null || !mHasImageCaptures || !mHasJourneys) {
            return POSITION_NONE;
        }
        else {
            return super.getItemPosition(object);
        }
    }

    /**
     * Adapter method which returns a count of how many Fragments are in the Viewpager
     */
    @Override
    public int getCount() {
        return 3;
    }
}
