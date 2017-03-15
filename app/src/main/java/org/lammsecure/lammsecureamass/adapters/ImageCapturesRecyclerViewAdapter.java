package org.lammsecure.lammsecureamass.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.lammsecure.lammsecureamass.R;
import org.lammsecure.lammsecureamass.models.LAMMImageCaptureObject;
import org.lammsecure.lammsecureamass.utilities.ApplicationUtilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Max on 11/3/17.
 *
 * A RecyclerView adapter for Arduino ImageCapture objects
 */

public class ImageCapturesRecyclerViewAdapter extends RecyclerView.Adapter<ImageCapturesRecyclerViewAdapter.ImageCaptureItemViewHolder> {

    private Context mContext;
    private ArrayList<LAMMImageCaptureObject> mImageCaptures;

    /**
     * Constructor
     */
    public ImageCapturesRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    /**
     * Tell the adapter that Image Captures have been set
     */
    public void setImageCaptures(ArrayList<LAMMImageCaptureObject> imageCaptures) {
        mImageCaptures = imageCaptures;
        notifyDataSetChanged();
    }

    /**
     * Adapter method to retrieve Viewholders to be put into the RecyclerView
     */
    @Override
    public ImageCaptureItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_image_capture, parent, false);
        return new ImageCaptureItemViewHolder(view);
    }

    /**
     * Adapter method to change the information displayed in the Viewholders
     */
    @Override
    public void onBindViewHolder(ImageCaptureItemViewHolder viewHolder, int position) {

        if (mImageCaptures != null) {
            if (mImageCaptures.size() > 0) {
                Glide.with(mContext)
                        .load(mImageCaptures.get(position).getImageStorageAddress())
                        .into(viewHolder.mImage);

                viewHolder.mDate.setText(ApplicationUtilities.convertTimeStampToDate(mImageCaptures.get(position).getTimestamp()));
            }
        }
        else {
            Glide.clear(viewHolder.mImage);
            viewHolder.mImage.setImageDrawable(null);
            viewHolder.mDate.setText(null);
        }
    }

    /**
     * Adapter method to retrieve amount of items that will populate the RecyclerView
     */
    @Override
    public int getItemCount() {

        if (mImageCaptures == null) {
            return 0;
        }
        else {
            return mImageCaptures.size();
        }
    }

    /**
     * A Viewholder for showing a single RecyclerView item
     */
    static class ImageCaptureItemViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mImage;
        protected TextView mDate;

        public ImageCaptureItemViewHolder(View view) {
            super(view);

            mImage = (ImageView) view.findViewById(R.id.imageview_image_capture);
            mDate = (TextView) view.findViewById(R.id.textview_capture_date);

        }
    }
}