package org.lammsecure.lammsecureamass.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.lammsecure.lammsecureamass.fragments.ArduinoChoiceFragment;
import org.lammsecure.lammsecureamass.fragments.DriversFragment;
import org.lammsecure.lammsecureamass.fragments.ManagersFragment;
import org.lammsecure.lammsecureamass.fragments.NothingToSeeHereFragment;

/**
 * Created by Max on 10/3/17.
 *
 * A ViewPager adapter for LAMM Secure account fragments
 */
public class AccountPagerAdapter extends FragmentStatePagerAdapter {

    private String mAccountName;
    private boolean mHasArduinos;

    /**
     * Constructor
     */
    public AccountPagerAdapter(FragmentManager fm, String accountName) {
        super(fm);
        mAccountName = accountName;
        mHasArduinos = true;
    }

    /**
     * Tell the adapter that this account has no Arduinos
     */
    public void setArduinos(boolean hasArduinos) {
        mHasArduinos = hasArduinos;
        notifyDataSetChanged();
    }

    /**
     * Adapter method to retrieve Fragments to be put into the Viewpager
     */
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (mAccountName != null && mHasArduinos) {
                    return ArduinoChoiceFragment.newInstance(mAccountName);
                }
                else {
                    return NothingToSeeHereFragment.newInstance(true);
                }

            case 1:
                if (mAccountName != null) {
                    return DriversFragment.newInstance(mAccountName);
                }
                else {
                    return new NothingToSeeHereFragment();
                }
            case 2:
                if (mAccountName != null) {
                    return ManagersFragment.newInstance(mAccountName);
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

        if (mAccountName != null|| !mHasArduinos) {
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
