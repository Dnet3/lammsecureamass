package org.lammsecure.lammsecureamass.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.models.LAMMJourneyObject;
import org.lammsecure.lammsecureamass.utilities.ApplicationUtilities;

import java.util.ArrayList;

/**
 * Created by Max on 11/3/17.
 *
 * A RecyclerView adapter for Arduino journey items
 */

public class JourneysRecyclerViewAdapter extends RecyclerView.Adapter<JourneysRecyclerViewAdapter.JourneyItemViewHolder> {

    private ArrayList<LAMMJourneyObject> mJourneys;

    /**
     * Constructor
     */
    public JourneysRecyclerViewAdapter() {}

    /**
     * Tell the adapter that journeys have been set
     */
    public void setJourneys(ArrayList<LAMMJourneyObject> journeys) {
        mJourneys = journeys;
        notifyDataSetChanged();
    }

    /**
     * Adapter method to retrieve Viewholders to be put into the RecyclerView
     */
    @Override
    public JourneyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_journey, parent, false);
        return new JourneyItemViewHolder(view);
    }

    /**
     * Adapter method to change the information displayed in the Viewholders
     */
    @Override
    public void onBindViewHolder(JourneyItemViewHolder viewHolder, int position) {

        if (mJourneys != null) {
            if (mJourneys.size() > position) {
                viewHolder.mStartDate.setText(ApplicationUtilities.convertTimeStampToDate(mJourneys.get(position).getStartTime()));
            }
        }
        else {
            viewHolder.mStartDate.setText(null);
        }
    }

    /**
     * Adapter method to retrieve amount of items that will populate the RecyclerView
     */
    @Override
    public int getItemCount() {
        if (mJourneys == null) {
            return 0;
        }
        else {
            return mJourneys.size();
        }
    }

    /**
     * A Viewholder for showing a single RecyclerView item
     */
    static class JourneyItemViewHolder extends RecyclerView.ViewHolder {

        protected TextView mStartDate;

        public JourneyItemViewHolder(View view) {
            super(view);

            mStartDate = (TextView) view.findViewById(R.id.textview_start_date);
        }
    }
}