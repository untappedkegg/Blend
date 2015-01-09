package com.untappedkegg.blend.ui.adapter;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.untappedkegg.blend.R;
import com.untappedkegg.blend.utils.MessageUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kyle on 1/7/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Cursor c;
    private String[] mFrom;
    private int[] mTo;
    private int mLayout;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public TextView contactName;
        public TextView msgPreview;
        public TextView msgDate;
        public ImageView contactPhoto;


        public ViewHolder(View v) {
            super(v);
            mView = v;

            contactName = (TextView) v.findViewById(R.id.contact_name);
            msgPreview = (TextView) v.findViewById(R.id.contact_msg_preview);
            msgDate = (TextView) v.findViewById(R.id.contact_msg_date);
            contactPhoto = (ImageView) v.findViewById(R.id.contact_photo);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(Cursor cursor, String[] from, int[] to, int layout) {
        c = cursor;
        mFrom = from;
        mTo = to;
        mLayout = layout;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(Cursor cursor, int layout) {
        c = cursor;
        mLayout = layout;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);
            if (c.moveToPosition(position)) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(c.getLong(25));
                final String phoneNum = c.getString(18);

                //27 m_id
                final Uri photoUri = MessageUtils.getPhotoFromNumber(phoneNum);
                if (photoUri != null) {
                    try {
//                        holder.contactPhoto.setImageURI(photoUri);
//                        holder.contactPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        holder.contactPhoto.setImageDrawable(MessageUtils.getDrawableFromNumber(phoneNum));
                    } catch (Exception e) {
                        holder.contactPhoto.setImageResource(R.drawable.ic_launcher);
                    }
                } else {
//                    holder.contactPhoto.setVisibility(View.INVISIBLE);
                }
                holder.contactName.setText(MessageUtils.getContactName(phoneNum));
                holder.msgDate.setText(formatter.format(calendar.getTime()));
                holder.msgPreview.setText(c.getString(26));
            }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return c.getCount();
    }

}
