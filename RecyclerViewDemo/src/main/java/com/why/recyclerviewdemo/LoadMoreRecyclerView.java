package com.why.recyclerviewdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by tengtao on 2017/1/6.
 */

public class LoadMoreRecyclerView extends FrameLayout {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private FrameLayout mFooterViewContainer;
    private OnPullLoadMoreListener mOnPullLoadMoreListener;
    /**
     * 是否还有更多数据
     */
    private boolean hasMore = true;
    /**
     * 是否可以加载更多,当使用刷新的时候条用
     */
    private boolean isRefreshing;

    /**
     * 是否启用加载更多
     */
    private boolean mLoadMoreEnable = true;
    /**
     * 是否正在加载更多
     */
    private boolean isLoadMore;
    private LayoutInflater mInflater;
    private View mFooterView;


    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews();
    }

    private void setupViews() {
        this.mContext = getContext();
        mInflater = LayoutInflater.from(mContext);
        mInflater.inflate(R.layout.load_more_recycler_view, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScroller(this));


        mFooterViewContainer = (FrameLayout) findViewById(R.id.footer_view);
        mFooterViewContainer.setVisibility(View.GONE);

        View footerContentView = mInflater.inflate(R.layout.footer_layout_default, null);
        mFooterViewContainer.addView(footerContentView);

    }

    public void setOnPullLoadMoreListener(OnPullLoadMoreListener onPullLoadMoreListener) {
        mOnPullLoadMoreListener = onPullLoadMoreListener;
    }

    public void loadMore() {
        if (mOnPullLoadMoreListener != null && hasMore && !isRefreshing) {
            mFooterViewContainer.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mFooterViewContainer.setVisibility(View.VISIBLE);
                        }
                    })
                    .start();
            invalidate();
            mOnPullLoadMoreListener.onLoadMore();

        }
    }


    public void pullLoadMoreCompleted() {
        isLoadMore = false;
        isRefreshing=false;
        mFooterViewContainer.animate()
                .translationY(mFooterViewContainer.getHeight())
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

    }


    public boolean isPullLoadMoreEnable() {
        return mLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        mLoadMoreEnable = loadMoreEnable;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }



    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setIsLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    /**
     * LinearLayoutManager
     */
    public void setLinearManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * GridLayoutManager
     */

    public void setGridManager(int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }


    /**
     * StaggeredGridLayoutManager
     */

    public void setStaggeredManager(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }


    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }


    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }


    public void setFooterView(View footerView) {
        if (footerView == null) {
            throw new NullPointerException("footer view can not null");
        }
        if (mFooterViewContainer.getChildCount() > 0) {
            mFooterViewContainer.removeAllViews();
        }
        this.mFooterView = footerView;
        mFooterViewContainer.addView(footerView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
    }

    public void setFooterView(@LayoutRes int resId) {
        View view = mInflater.inflate(resId, null);
        setFooterView(view);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void noMore() {
        if (mOnPullLoadMoreListener != null) {
            mOnPullLoadMoreListener.onAnyMore();
        }
    }


}
