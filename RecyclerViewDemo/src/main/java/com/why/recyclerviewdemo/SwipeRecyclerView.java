package com.why.recyclerviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * Created by tengtao on 2017/1/9.
 */

public class SwipeRecyclerView extends FrameLayout {
    private Context mContext;
    private PtrFrameLayout mPtrFrameLayout;
    private WrapperRecyclerView mWrapperRecyclerView;
    private LoadMoreRecyclerView mLoadMoreRecyclerView;

    private OnRefreshListener mOnRefreshListener;

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews(context);
    }

    public void setupViews(Context context) {
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.refresh_recycler_view, this);
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.ptr_frame_layout);
        mWrapperRecyclerView = (WrapperRecyclerView) findViewById(R.id.wapper_recycler_view);
        setLoadMore();


        // header
        final MaterialHeader header = new MaterialHeader(context);
        //header.setColorSchemeColors(new int[]{R.color.line_color_run_speed_13});
//        int[] colors = getResources().getIntArray(R.array.refresh_progress_bar_colors);
//        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrameLayout);
        mPtrFrameLayout.setDurationToCloseHeader(500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.setEnabledNextPtrAtOnce(false);

        setPtrHandler();

    }

    private void setLoadMore() {
        mLoadMoreRecyclerView = mWrapperRecyclerView.getLoadMoreRecyclerView();
        mLoadMoreRecyclerView.setOnPullLoadMoreListener(new OnPullLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onLoadMore();
                }
            }

            @Override
            public void onAnyMore() {
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onAnyMore();
                }
            }
        });
    }

    private void setPtrHandler() {
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mWrapperRecyclerView.checkCanDoRefresh();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mOnRefreshListener != null) {
                    mLoadMoreRecyclerView.setRefreshing(true);
                    mOnRefreshListener.onRefresh();
                }
            }
        });

    }

    public void autoRefresh() {
        mPtrFrameLayout.autoRefresh();
        mWrapperRecyclerView.getProgressView().setVisibility(View.GONE);
    }

    public void autoRefresh(boolean atOnce) {
        mPtrFrameLayout.autoRefresh(atOnce);
        mWrapperRecyclerView.getProgressView().setVisibility(View.GONE);
    }


    public void autoRefresh(boolean atOnce, int duration) {
        mPtrFrameLayout.autoRefresh(atOnce, duration);
        mWrapperRecyclerView.getProgressView().setVisibility(View.GONE);
    }


    public void refreshCompleted() {
        mLoadMoreRecyclerView.pullLoadMoreCompleted();
        mPtrFrameLayout.refreshComplete();
    }


    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    public LoadMoreRecyclerView getLoadMoreRecyclerView() {
        return mLoadMoreRecyclerView;
    }
}
