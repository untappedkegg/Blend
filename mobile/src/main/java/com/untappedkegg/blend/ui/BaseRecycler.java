package com.untappedkegg.blend.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.untappedkegg.blend.R;

/**
 * Created by kyle on 1/7/15.
 */
public abstract class BaseRecycler extends Fragment{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(getLayout(), container, true);

        mRecyclerView = (RecyclerView) view.findViewById(getRecyclerId());

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = getLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(getAdapter());
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


}
