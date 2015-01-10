package com.untappedkegg.blend.thread;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.untappedkegg.blend.ui.BaseRecyclerView;
import com.untappedkegg.blend.ui.recyclerviewextentions.RecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThreadFragment.OnThreadInteractionListener} interface
 * to handle interaction events.
 */
public class ThreadFragment extends BaseRecyclerView {

    private OnThreadInteractionListener mListener;

    public ThreadFragment() {
        // Required empty public constructor
        this.isClickable = true;
    }


    @Override
    protected RecyclerViewAdapter getAdapter() {
        return null;
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
                    + " must implement" +  OnThreadInteractionListener.class.getSimpleName());
        }
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

}
