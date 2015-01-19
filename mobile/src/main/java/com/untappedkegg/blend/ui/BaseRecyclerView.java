package com.untappedkegg.blend.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.untappedkegg.blend.R;
import com.untappedkegg.blend.ui.recyclerviewextentions.ItemTouchListenerAdapter;
import com.untappedkegg.blend.ui.recyclerviewextentions.SwipeToDismissTouchListener;

import java.util.List;

/**
 * Created by kyle on 1/7/15.
 */
public abstract class BaseRecyclerView extends Fragment implements ItemTouchListenerAdapter.RecyclerViewOnItemClickListener, ActionMode.Callback {
    protected RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapter;
    protected boolean isClickable = false;
    protected boolean swipeToDismiss = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(getLayout(), container, false);

        mRecyclerView = (RecyclerView) view.findViewById(getRecyclerId());

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = getLayoutManager();
        if (mLayoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager)mLayoutManager).setReverseLayout(shouldUseReverseLayout());
        }
        if (mLayoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager)mLayoutManager).setReverseLayout(shouldUseReverseLayout());
        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter = getAdapter();
        mRecyclerView.setAdapter(adapter);
        setRecyclerViewOptions();
        return view;
    }

    protected int getRecyclerId() {
        return android.R.id.list;
    }

    protected int getLayout() {
        return R.layout.generic_recycler;
    }

    /**
     * <p>Default implementation returns a {@link android.support.v7.widget.LinearLayoutManager}</p>
     * <p>Subclasses should override this to return a grid type, if necessary</p>
     *
     * @see android.support.v7.widget.GridLayoutManager
     * @see android.support.v7.widget.StaggeredGridLayoutManager
     *
     * */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract boolean shouldUseReverseLayout();

    /**
     * {@see https://github.com/ismoli/DynamicRecyclerView}
     */
    protected void setRecyclerViewOptions() {

        if (swipeToDismiss) {
            mRecyclerView.addOnItemTouchListener(new SwipeToDismissTouchListener(mRecyclerView, new SwipeToDismissTouchListener.DismissCallbacks() {
                @Override
                public SwipeToDismissTouchListener.SwipeDirection canDismiss(int position) {
                    return SwipeToDismissTouchListener.SwipeDirection.BOTH;
                }

                @Override
                public void onDismiss(RecyclerView view, List<SwipeToDismissTouchListener.PendingDismissData> dismissData) {
                    for (SwipeToDismissTouchListener.PendingDismissData data : dismissData) {
//                        adapter.removeItem(data.position);
                        adapter.notifyItemRemoved(data.position);
                    }
                }
            }));
        }

        if (isClickable) {
            mRecyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(mRecyclerView, this));

        }


    }


}
