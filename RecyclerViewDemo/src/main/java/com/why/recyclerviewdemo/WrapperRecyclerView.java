package com.why.recyclerviewdemo;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by tengtao on 2017/1/6.
 * 包含加载更多，emptyview
 */

public class WrapperRecyclerView extends LinearLayout {

    private Context mContext;
    private FrameLayout mEmptyViewContainer;
    private LoadMoreRecyclerView mLoadMoreRecyclerView;

    private boolean mRegisterCheckEmptyView = false;
    private RecyclerView.Adapter mAdapter;
    private View mEmptyView;
    private boolean isUseAnin = true;
    private LayoutInflater mInflater;
    private FrameLayout mProgressContainer;
    private View mProgressView;

    final private RecyclerView.AdapterDataObserver mAdapterObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmptyView();
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            checkIfEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmptyView();
        }
    };



    public WrapperRecyclerView(Context context) {
        this(context, null);
    }

    public WrapperRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapperRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOrientation(VERTICAL);
        setupViews();
    }

    private void setupViews() {
        this.mContext = getContext();
        mInflater = LayoutInflater.from(mContext);

        mProgressContainer = new FrameLayout(mContext);
        addView(mProgressContainer, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mProgressView = mInflater.inflate(R.layout.layout_progressbar, null);
        addProgressView(mProgressView);

        mEmptyViewContainer = new FrameLayout(mContext);
        addView(mEmptyViewContainer, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mEmptyViewContainer.setVisibility(View.GONE);


        mLoadMoreRecyclerView = new LoadMoreRecyclerView(mContext);
        addView(mLoadMoreRecyclerView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

    }

    public void addProgressView(View progressView) {
        if (progressView == null) {
            throw new NullPointerException("progress can not null");
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        if (mProgressContainer.getChildCount() > 0) {
            mProgressContainer.removeAllViews();
        }
        mProgressContainer.addView(progressView, params);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mRegisterCheckEmptyView) {
            unregisterAdapterDataObserver();
        }
        this.mAdapter = adapter;
        mLoadMoreRecyclerView.setAdapter(adapter);

        // check RegisterCheckEmptyView
        if (mEmptyView != null) {
            registerAdapterDataObserver();
        }
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    public View getEmptyView() {
        return mEmptyView;
    }
    public View getProgressView() {
        return mProgressContainer;
    }



    public void setEmptyView(@LayoutRes int resId) {
        setEmptyView(mInflater.inflate(resId, null));
    }

    public void setEmptyView(View emptyView) {
        if (emptyView == null) {
            throw new NullPointerException("emptyView can not null");
        }
        mEmptyView = emptyView;

        if (mEmptyViewContainer.getChildCount() > 0) {
            mEmptyViewContainer.removeAllViews();
        }

        mEmptyViewContainer.addView(emptyView);

        registerAdapterDataObserver();
    }

    private void registerAdapterDataObserver() {
        if (!mRegisterCheckEmptyView && mEmptyView != null && mAdapter != null) {
            mRegisterCheckEmptyView = true;
            mAdapter.registerAdapterDataObserver(mAdapterObserver);
        }
    }

    private void unregisterAdapterDataObserver() {
        if (mRegisterCheckEmptyView) {
            mAdapter.registerAdapterDataObserver(mAdapterObserver);
            mRegisterCheckEmptyView = false;
        }
    }

    private void checkIfEmptyView() {
        if (mProgressContainer.getVisibility() == View.VISIBLE) {
//            mProgressContainer.setVisibility(View.GONE);
            hideAnim(mProgressContainer);

        }

        if (mEmptyView != null && mAdapter != null) {
            if (mAdapter.getItemCount() == 0 || (mAdapter.getItemCount() == 1 && mLoadMoreRecyclerView.isLoadMore())) {
//                mEmptyViewContainer.setVisibility(VISIBLE);
//                mLoadMoreRecyclerView.setVisibility(GONE);
                showAnim(mEmptyViewContainer);
                hideAnim(mLoadMoreRecyclerView);
            } else {
//                mLoadMoreRecyclerView.setVisibility(VISIBLE);
                showAnim(mLoadMoreRecyclerView);
                hideAnim(mEmptyViewContainer);
//                mEmptyViewContainer.setVisibility(GONE);

            }
        }


    }


    public LoadMoreRecyclerView getLoadMoreRecyclerView() {
        return mLoadMoreRecyclerView;
    }


    private void showAnim(final View view) {
        if (!isUseAnin) {
            view.setVisibility(View.VISIBLE);
            return;
        }
        view.animate().alpha(1.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setAlpha(0);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).setInterpolator(new AccelerateDecelerateInterpolator()).start();
    }

    private void hideAnim(final View view) {
        if (!isUseAnin) {
            view.setVisibility(View.GONE);
            return;
        }
        view.animate().alpha(0.0f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).setInterpolator(new AccelerateDecelerateInterpolator()).start();

    }



    public boolean checkCanDoRefresh() {
        return !mLoadMoreRecyclerView.isLoadMore()
                && mLoadMoreRecyclerView.getVisibility() == View.VISIBLE;
    }
}
