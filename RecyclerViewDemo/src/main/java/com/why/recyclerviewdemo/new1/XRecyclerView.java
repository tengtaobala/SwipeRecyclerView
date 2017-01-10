package com.why.recyclerviewdemo.new1;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by tengtao on 2017/1/9.
 */

public abstract class XRecyclerView extends FrameLayout {
    private Context mContext;

    public XRecyclerView(Context context) {
        super(context);
        this.mContext = context;
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    protected  abstract RecyclerView getRecyclerView();

    /**
     * LinearLayoutManager
     */
    public void setLinearManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        getRecyclerView().setLayoutManager(linearLayoutManager);
    }

    /**
     * GridLayoutManager
     */

    public void setGridManager(int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        getRecyclerView().setLayoutManager(gridLayoutManager);
    }


    /**
     * StaggeredGridLayoutManager
     */

    public void setStaggeredManager(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL);
        getRecyclerView().setLayoutManager(staggeredGridLayoutManager);
    }


    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        getRecyclerView().setLayoutManager(layout);
    }


    public RecyclerView.LayoutManager getLayoutManager() {
        return getRecyclerView().getLayoutManager();
    }


    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        getRecyclerView().addItemDecoration(itemDecoration);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        getRecyclerView().setAdapter(adapter);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        getRecyclerView().setItemAnimator(animator);
    }


}
