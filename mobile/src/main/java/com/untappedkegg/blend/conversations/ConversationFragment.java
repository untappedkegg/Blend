package com.untappedkegg.blend.conversations;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.untappedkegg.blend.R;
import com.untappedkegg.blend.data.MessagesAdapter;
import com.untappedkegg.blend.ui.BaseRecyclerView;
import com.untappedkegg.blend.ui.recyclerviewextentions.RecyclerViewAdapter;
import com.untappedkegg.blend.utils.Boast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.untappedkegg.blend.conversations.ConversationFragment.OnConversationInteractionListener} interface
 * to handle interaction events.
 */
public class ConversationFragment extends BaseRecyclerView {

    /*----- VARIABLES -----*/
    private OnConversationInteractionListener mListener;

    public ConversationFragment() {
        this.isClickable = true;
        this.swipeToDismiss = true;
        // Required empty public constructor
    }

    /*----- LIFECYCLE METHODS -----*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnConversationInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +  OnConversationInteractionListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
//        return new ConversationRecyclerAdapter(getActivity(), MessagesAdapter.readAllMessages(), R.layout.generic_card);
        return new ConversationRecyclerAdapter();
    }

//    @Override
//    public void onClick(View v) {
//        // TODO get name and id
//
//        Boast.makeText(getActivity(), "Card Touched; Name = " + ((TextView) v.findViewById(R.id.contact_name)).getText().toString() + " ID = " + ((TextView) v.findViewById(R.id.contact_id)).getText().toString(), Boast.LENGTH_LONG).show();
////        mListener.onConversationSelected(((TextView)v.findViewById(R.id.contact_name)).getText().toString(), ((TextView)v.findViewById(R.id.contact_id)).getText().toString());
//
//    }

//    @Override
//    public boolean onLongClick(View v) {
//        Boast.makeText(getActivity(), "Long Clicked").show();
//        return true;
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
    public void onItemClick(RecyclerView parent, View v, int position) {
//        v.setElevation(500);
        Boast.makeText(getActivity(), "Card Touched; Name = " + ((TextView) v.findViewById(R.id.contact_name)).getText().toString() + " ID = " + ((TextView) v.findViewById(R.id.contact_id)).getText().toString(), Boast.LENGTH_LONG).show();
        mListener.onConversationSelected(((TextView) v.findViewById(R.id.contact_name)).getText().toString(), ((TextView) v.findViewById(R.id.contact_id)).getText().toString());
    }

    @Override
    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
        Boast.makeText(getActivity(), "Long Clicked").show();
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
    public interface OnConversationInteractionListener {
        public void onConversationSelected(String name, String id);
    }

    /*----- NESTED CLASSES -----*/

    public static class ConversationRecyclerAdapter extends RecyclerViewAdapter {

        ArrayList<MessagesAdapter.Conversation> items;
        ConversationRecyclerAdapter() {
                items = new ArrayList<>(MessagesAdapter.conversationsToArrayList()); //MessagesAdapter.conversationsToArrayList();

        }

        public static class ConvoViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public View mView;
            public TextView contactName;
            public TextView msgPreview;
            public TextView msgDate;
            public TextView contactId;
            public ImageView contactPhoto;


            public ConvoViewHolder(View v) {
                super(v);
                mView = v;

                contactName = (TextView) v.findViewById(R.id.contact_name);
                msgPreview = (TextView) v.findViewById(R.id.contact_msg_preview);
                msgDate = (TextView) v.findViewById(R.id.contact_msg_date);
                contactPhoto = (ImageView) v.findViewById(R.id.contact_photo);
                contactId = (TextView) v.findViewById(R.id.contact_id);

            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_card, parent, false);
            // set the view's size, margins, padding and layout parameters

            RecyclerView.ViewHolder vh = new ConvoViewHolder(v);
            return vh;
        }

        @Override
        protected void onBindViewData(RecyclerView.ViewHolder viewHolder, int position) {
            //Must cast ViewHolder in order to access variabes
            ConvoViewHolder mHolder = (ConvoViewHolder) viewHolder;
            MessagesAdapter.Conversation mConvo = items.get(position);


                try {
                    mHolder.contactPhoto.setImageDrawable(mConvo.photo);
                } catch (Exception e) {
                    mHolder.contactPhoto.setImageResource(R.drawable.ic_launcher);
                }
//            Log.e("Conversation", items.get(position).name + "");
            mHolder.contactName.setText(mConvo.name);
            mHolder.msgDate.setText(mConvo.date);
            mHolder.msgPreview.setText(mConvo.snippet);
            mHolder.contactId.setText(mConvo.threadId);
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

/*
    public static class ConversationRecyclerAdapter extends BaseRecyclerAdapter {

        public ConversationRecyclerAdapter(Context ctx, Cursor cursor, int layoutId) {
            super(ctx, cursor, layoutId);
        }

        public ConversationRecyclerAdapter(Context ctx, Cursor cursor, int layoutId, boolean clickable, View.OnClickListener clickListener) {
            super(ctx, cursor, layoutId, clickable, clickListener);
        }

        public ConversationRecyclerAdapter(Context ctx, Cursor cursor, int layoutId, boolean clickable, boolean longClickable, View.OnClickListener clickListener) {
            super(ctx, cursor, layoutId, clickable, longClickable, clickListener);
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ConvoViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public View mView;
            public TextView contactName;
            public TextView msgPreview;
            public TextView msgDate;
            public TextView contactId;
            public ImageView contactPhoto;


            public ConvoViewHolder(View v) {
                super(v);
                mView = v;

                contactName = (TextView) v.findViewById(R.id.contact_name);
                msgPreview = (TextView) v.findViewById(R.id.contact_msg_preview);
                msgDate = (TextView) v.findViewById(R.id.contact_msg_date);
                contactPhoto = (ImageView) v.findViewById(R.id.contact_photo);
                contactId = (TextView) v.findViewById(R.id.contact_id);

            }
        }

        @Override
        protected RecyclerView.ViewHolder getViewHolder(View v) {
            return new ConvoViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
            //Must cast ViewHolder in order to access variabes
            ConvoViewHolder mHolder = (ConvoViewHolder) holder;
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
                        mHolder.contactPhoto.setImageDrawable(MessageUtils.getDrawableFromNumber(phoneNum));
                    } catch (Exception e) {
                        mHolder.contactPhoto.setImageResource(R.drawable.ic_launcher);
                    }
                } else {
                    // TODO: Set alternative image
                }
            mHolder.contactName.setText(MessageUtils.getContactName(phoneNum));
            mHolder.msgDate.setText(formatter.format(calendar.getTime()));
            mHolder.msgPreview.setText(mCursor.getString(26));
            mHolder.contactId.setText(id);

        }

        @Override
        protected void onContentChanged() {
            // Nothing to do?
        }

    }
*/
}
