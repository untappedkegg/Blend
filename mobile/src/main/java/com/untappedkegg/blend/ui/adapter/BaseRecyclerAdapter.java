package com.untappedkegg.blend.ui.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

/**
 * Created by kyle on 1/9/15.
 */
public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /*----- VARIABLES -----*/
    protected Cursor mCursor;
    private int mLayout;
//    private int mSecondLayout = -1;
    private boolean clickable = false;
    private boolean longClickable = false;
    private OnClickListener mClickListener;
    private OnLongClickListener mLongClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public BaseRecyclerAdapter(Cursor cursor, int layoutId) {
        this.mCursor = cursor;
        this.mLayout = layoutId;
    }

    public BaseRecyclerAdapter(Cursor cursor, int layoutId, boolean clickable, OnClickListener clickListener) {
        this.mCursor = cursor;
        this.mLayout = layoutId;
        this.clickable = clickable;
        this.mClickListener = clickListener;
    }

    public BaseRecyclerAdapter(Cursor cursor, int layoutId, boolean clickable, boolean longClickable, OnClickListener clickListener, OnLongClickListener longClickListener) {
        this.mCursor = cursor;
        this.mLayout = layoutId;
        this.clickable = clickable;
        this.longClickable = longClickable;
        this.mClickListener = clickListener;
        this.mLongClickListener = longClickListener;
    }

//    public BaseRecyclerAdapter(Cursor cursor, int layoutId, int secondLayoutId, boolean clickable, boolean longClickable, OnClickListener clickListener, OnLongClickListener longClickListener) {
//        this.mCursor = cursor;
//        this.mLayout = layoutId;
//        this.mSecondLayout = secondLayoutId;
//        this.clickable = clickable;
//        this.longClickable = longClickable;
//        this.mClickListener = clickListener;
//        this.mLongClickListener = longClickListener;
//    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        // set the view's size, margins, padding and layout parameters

        if(clickable) {
            v.setOnClickListener(mClickListener);
        }
        if (longClickable) {
            v.setOnLongClickListener(mLongClickListener);
        }

        RecyclerView.ViewHolder vh = getViewHolder(v);
        return vh;
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if(mCursor != null && !mCursor.isClosed())
            mCursor.close();
    }

    protected abstract RecyclerView.ViewHolder getViewHolder( View v);
}
