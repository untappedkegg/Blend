package com.untappedkegg.blend.conversations;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.untappedkegg.blend.R;
import com.untappedkegg.blend.data.MessagesAdapter;
import com.untappedkegg.blend.ui.BaseRecyclerView;
import com.untappedkegg.blend.utils.MessageUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        return new ConversationRecyclerAdapter(MessagesAdapter.readAllMessages(), R.layout.generic_card);
    }

    @Override
    public void onClick(View v) {
        // TODO get name and id
        Toast.makeText(getActivity(), "Card Touched", Toast.LENGTH_LONG).show();
        mListener.onConversationSelected(((TextView)v.findViewById(R.id.contact_name)).getText().toString(), ((TextView)v.findViewById(R.id.contact_id)).getText().toString());

    }

//    @Override
//    public boolean onLongClick(View v) {
//        return false;
//    }

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
    public static class ConversationRecyclerAdapter extends RecyclerView.Adapter<ConversationRecyclerAdapter.ViewHolder> {
        private Cursor c;
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
            public TextView contactId;
            public ImageView contactPhoto;


            public ViewHolder(View v) {
                super(v);
                mView = v;

                contactName = (TextView) v.findViewById(R.id.contact_name);
                msgPreview = (TextView) v.findViewById(R.id.contact_msg_preview);
                msgDate = (TextView) v.findViewById(R.id.contact_msg_date);
                contactPhoto = (ImageView) v.findViewById(R.id.contact_photo);
                contactId = (TextView) v.findViewById(R.id.contact_id);


            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ConversationRecyclerAdapter(Cursor cursor, int layout) {
            c = cursor;
            mLayout = layout;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ConversationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
            // set the view's size, margins, padding and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your data set at this position
            // - replace the contents of the view with that element
            if (c.moveToPosition(position)) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(c.getLong(25));
                final String phoneNum = c.getString(18);
                final String id = MessageUtils.fetchContactIdFromPhoneNumber(phoneNum);

                //27 m_id
                final Uri photoUri = MessageUtils.getPhotoUri(id);
                if (photoUri != null) {
                    try {
                        holder.contactPhoto.setImageDrawable(MessageUtils.getDrawableFromNumber(phoneNum));
                    } catch (Exception e) {
                        holder.contactPhoto.setImageResource(R.drawable.ic_launcher);
                    }
                } else {
                    // TODO: Set alternative image
                }
                holder.contactName.setText(MessageUtils.getContactName(phoneNum));
                holder.msgDate.setText(formatter.format(calendar.getTime()));
                holder.msgPreview.setText(c.getString(26));
                holder.contactId.setText(id);
            }

        }

        // Return the size of your data set (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return c.getCount();
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            if(c != null && !c.isClosed())
                c.close();
        }
    }

}
