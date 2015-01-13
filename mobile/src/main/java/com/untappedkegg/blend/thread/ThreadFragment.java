package com.untappedkegg.blend.thread;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.untappedkegg.blend.AppState;
import com.untappedkegg.blend.R;
import com.untappedkegg.blend.data.MessagesAdapter;
import com.untappedkegg.blend.ui.BaseRecyclerView;
import com.untappedkegg.blend.ui.adapter.BaseRecyclerAdapter;
import com.untappedkegg.blend.utils.MessageUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThreadFragment.OnThreadInteractionListener} interface
 * to handle interaction events.
 */
public class ThreadFragment extends BaseRecyclerView {

    /*----- VARIABLES -----*/
    private OnThreadInteractionListener mListener;
    private String contactId, contactName;

    public ThreadFragment() {
        // Required empty public constructor
        this.isClickable = true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ThreadRecyclerAdapter(getActivity(), MessagesAdapter.readThreadMessages(contactId), R.layout.message_row_sent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnThreadInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " +  OnThreadInteractionListener.class.getSimpleName());
        }

        final Bundle bundle = getArguments();
        contactId = bundle.getString(AppState.KEY_MSG_ID);
        contactName = bundle.getString(AppState.KEY_MSG_NAME);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    public boolean onLongClick(View v) {
//        return false;
//    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Override
    public void onItemClick(RecyclerView parent, View clickedView, int position) {

    }

    @Override
    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnThreadInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

    }

    public static class ThreadRecyclerAdapter extends BaseRecyclerAdapter {

        public ThreadRecyclerAdapter(Context ctx, Cursor cursor, int layoutId) {
            super(ctx, cursor, layoutId);
        }

        public ThreadRecyclerAdapter(Context ctx, Cursor cursor, int layoutId, boolean clickable, View.OnClickListener clickListener) {
            super(ctx, cursor, layoutId, clickable, clickListener);
        }

        public ThreadRecyclerAdapter(Context ctx, Cursor cursor, int layoutId, boolean clickable, boolean longClickable, View.OnClickListener clickListener) {
            super(ctx, cursor, layoutId, clickable, longClickable, clickListener);
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ThreadViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public ImageView contact;
            public TextView message;
            public TextView date;

            public ThreadViewHolder(View v) {
                super(v);
                mView = v;

                contact = (ImageView) v.findViewById(R.id.contact);
                message = (TextView) v.findViewById(R.id.message);
                date = (TextView) v.findViewById(R.id.date);
            }
        }

        @Override
        protected RecyclerView.ViewHolder getViewHolder(View v) {
            return new ThreadViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
            //Must cast ViewHolder in order to access variabes
            ThreadViewHolder mHolder = (ThreadViewHolder) holder;
            // - get element from your data set at this position
            // - replace the contents of the view with that element

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mCursor.getLong(25));
            final String phoneNum = mCursor.getString(18);
            final String id = MessageUtils.fetchContactIdFromPhoneNumber(phoneNum);

            //27 m_id
            final Uri photoUri = MessageUtils.getPhotoUri(id);
            if (photoUri != null) {
                try {
                    mHolder.contact.setImageDrawable(MessageUtils.getDrawableFromNumber(phoneNum));
                } catch (Exception e) {
                    mHolder.contact.setImageResource(R.drawable.ic_launcher);
                }
            } else {
                // TODO: Set alternative image
            }
            //mHolder.contactName.setText(MessageUtils.getContactName(phoneNum));
            mHolder.date.setText(formatter.format(calendar.getTime()));
            mHolder.message.setText(mCursor.getString(26));
            //mHolder.contactId.setText(id);
        }

        @Override
        protected void onContentChanged() {
            //not sure what to do here yet
        }
    }

}
