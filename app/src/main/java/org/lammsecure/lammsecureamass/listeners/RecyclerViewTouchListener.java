package org.lammsecure.lammsecureamass.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by Max on 11/3/17.
 *
 * A touch listener for RecyclerView items
 */

public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener{

    private RecyclerViewTouchListener.RecyclerViewClickListener mClickListener;
    private GestureDetector mGestureDetector;

    /**
     * Constructor
     */
    public RecyclerViewTouchListener(Context context, final RecyclerViewTouchListener.RecyclerViewClickListener clickListener){

        this.mClickListener = clickListener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
        if(child != null && mClickListener != null && mGestureDetector.onTouchEvent(event)){
            mClickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * RecyclerView item interaction listener
     */
    public interface RecyclerViewClickListener {
        void onClick(View view,int position);
    }
}

