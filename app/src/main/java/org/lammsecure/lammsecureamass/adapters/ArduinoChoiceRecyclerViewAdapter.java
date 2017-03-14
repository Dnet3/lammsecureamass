package org.lammsecure.lammsecureamass.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.models.LAMMArduinoObject;

import java.util.ArrayList;

/**
 * Created by Max on 10/3/17.
 *
 * A RecyclerView adapter for Arduino items
 */
public class ArduinoChoiceRecyclerViewAdapter extends RecyclerView.Adapter<ArduinoChoiceRecyclerViewAdapter.ArduinoItemViewHolder> {

    private ArrayList<String> mArduinoIDs;
    private ArrayList<LAMMArduinoObject> mArduinos;

    /**
     * Constructor
     */
    public ArduinoChoiceRecyclerViewAdapter() {}

    /**
     * Tell the adapter that Arduino details have been set
     */
    public void setArduinos(ArrayList<LAMMArduinoObject> arduinos, ArrayList<String> arduinoIDs) {
        mArduinos = arduinos;
        mArduinoIDs = arduinoIDs;
        notifyDataSetChanged();
    }

    /**
     * Adapter method to retrieve Viewholders to be put into the RecyclerView
     */
    @Override
    public ArduinoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_arduino, parent, false);
        return new ArduinoItemViewHolder(view);
    }

    /**
     * Adapter method to change the information displayed in the Viewholders
     */
    @Override
    public void onBindViewHolder(ArduinoItemViewHolder viewHolder, int position) {

        if (mArduinoIDs!= null && mArduinos != null) {
            if (mArduinos.size() > position) {
                viewHolder.mID.setText(mArduinoIDs.get(position));
                viewHolder.mName.setText(mArduinos.get(position).getName());
            }
        }
    }

    /**
     * Adapter method to retrieve amount of items that will populate the RecyclerView
     */
    @Override
    public int getItemCount() {

        if (mArduinoIDs == null) {
            return 0;
        }
        else {
            return mArduinoIDs.size();
        }
    }

    /**
     * A Viewholder for showing a single RecyclerView item
     */
    static class ArduinoItemViewHolder extends RecyclerView.ViewHolder {

        protected TextView mID, mName;

        public ArduinoItemViewHolder(View view) {
            super(view);

            mID = (TextView) view.findViewById(R.id.arduino_id_textview);
            mName = (TextView) view.findViewById(R.id.arduino_name_textview);
        }
    }
}