package com.untappedkegg.blend.thread;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.untappedkegg.blend.AppState;
import com.untappedkegg.blend.R;
import com.untappedkegg.blend.data.MessagesAdapter;
import com.untappedkegg.blend.ui.BaseRecyclerView;
import com.untappedkegg.blend.ui.adapter.BaseRecyclerAdapter;
import com.untappedkegg.blend.ui.recyclerviewextentions.RecyclerViewAdapter;
import com.untappedkegg.blend.utils.MessageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private String threadId, contactName;

    public ThreadFragment() {
        // Required empty public constructor
        this.isClickable = true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ThreadRecyclerAdapter(threadId);
        // return new ThreadRecyclerAdapter(getActivity(), MessagesAdapter.readThreadMessages(contactId), R.layout.message_row_sent);
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

        activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getActionBar().setHomeButtonEnabled(true);

        try {
            mListener = (OnThreadInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " +  OnThreadInteractionListener.class.getSimpleName());
        }

        final Bundle bundle = getArguments();
        threadId = bundle.getString(AppState.KEY_MSG_ID);
        contactName = bundle.getString(AppState.KEY_MSG_NAME);

        final ActionBar actionBar = activity.getActionBar();
        actionBar.setTitle(contactName);
//        actionBar.setIcon(ImageUtils.bitmapToDrawable((Bitmap) bundle.getParcelable(AppState.KEY_MSG_PHOTO)));
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

    /*----- NESTED CLASSES -----*/
    public static class ThreadRecyclerAdapter extends RecyclerViewAdapter {

        ArrayList<MessagesAdapter.TextMessage> items;
        ThreadRecyclerAdapter(String threadId) {
            items = new ArrayList<>(MessagesAdapter.threadToArrayList(threadId)); //MessagesAdapter.conversationsToArrayList();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_sent, parent, false);
            // set the view's size, margins, padding and layout parameters

            RecyclerView.ViewHolder vh = new ThreadViewHolder(v);
            return vh;
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
        public void onBindViewData(RecyclerView.ViewHolder viewHolder, int position) {
            //Must cast ViewHolder in order to access variabes
            ThreadViewHolder mHolder = (ThreadViewHolder) viewHolder;
            MessagesAdapter.TextMessage mText = items.get(position);

            try {
                mHolder.contact.setImageDrawable(mText.photo);
            } catch (Exception e) {
                mHolder.contact.setImageResource(R.drawable.ic_launcher);
            }

            //mHolder.contactName.setText(MessageUtils.getContactName(phoneNum));
            mHolder.date.setText(mText.date);
            mHolder.message.setText(mText.snippet);
            //mHolder.contactId.setText(id);
        }

        @Override
        public void removeItem(int position) {
            items.remove(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}
