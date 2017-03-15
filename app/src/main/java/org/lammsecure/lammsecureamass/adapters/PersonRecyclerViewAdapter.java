package org.lammsecure.lammsecureamass.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lammsecure.lammsecureamass.R;

import java.util.ArrayList;

/**
 * Created by Max on 13/3/17.
 *
 * A RecyclerView adapter for showing lists of people
 */

public class PersonRecyclerViewAdapter extends RecyclerView.Adapter<PersonRecyclerViewAdapter.PersonItemViewHolder> {

    private ArrayList<String> mPeople;

    /**
     * Constructor
     */
    public PersonRecyclerViewAdapter() {}

    /**
     * Tell the adapter that people have been set
     */
    public void setPeople(ArrayList<String> people) {
        mPeople = people;
        notifyDataSetChanged();
    }

    /**
     * Adapter method to retrieve Viewholders to be put into the RecyclerView
     */
    @Override
    public PersonItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_person, parent, false);
        return new PersonItemViewHolder(view);
    }

    /**
     * Adapter method to change the information displayed in the Viewholders
     */
    @Override
    public void onBindViewHolder(PersonItemViewHolder viewHolder, int position) {

        if (mPeople != null) {
            if (mPeople.size() > position) {
                viewHolder.mName.setText(mPeople.get(position));
            }
        }
        else {
            viewHolder.mName.setText(null);
        }
    }

    /**
     * Adapter method to retrieve amount of items that will populate the RecyclerView
     */
    @Override
    public int getItemCount() {
        if (mPeople == null) {
            return 0;
        }
        else {
            return mPeople.size();
        }
    }

    /**
     * A Viewholder for showing a single RecyclerView item
     */
    static class PersonItemViewHolder extends RecyclerView.ViewHolder {

        protected TextView mName;

        public PersonItemViewHolder(View view) {
            super(view);

            mName = (TextView) view.findViewById(R.id.textview_name);
        }
    }
}
