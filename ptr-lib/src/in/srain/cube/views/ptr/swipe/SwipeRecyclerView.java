package in.srain.cube.views.ptr.swipe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
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

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.R;

/**
 * Created by tengtao on 2017/1/9.
 */

public class SwipeRecyclerView extends FrameLayout {
    private Context mContext;
    private LayoutInflater mInflater;

    private PtrFrameLayout mPtrFrameLayout;
    private FrameLayout mEmptyViewContainer;
    private FrameLayout mRecyclerViewContainer;
    private RecyclerView mRecyclerView;
    private FrameLayout mFooterViewContainer;

    private OnRefreshListener mOnRefreshListener;


    private boolean isRefreshing;
    private boolean isLoading;
    private boolean hasMore = true;

    //是否可以刷新
    private boolean isPushRefreshEnable = true;
    //是否启用加载更多
    private boolean isPullMoreEnable = true;
    private View mEmptyView;
    private boolean mRegisterCheckEmptyView;
    private RecyclerView.Adapter mAdapter;


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


    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews(context);
        findViewById();
        setupLoadView();
        setPtrView();
    }

    private void setPtrView() {
        mPtrFrameLayout.setDurationToCloseHeader(500);
        mPtrFrameLayout.setEnabledNextPtrAtOnce(false);
        setPtrHandler();
    }

    private void setupViews(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mInflater.inflate(R.layout.layout_swipe_recycler_view, this);
    }

    private void findViewById() {
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.ptr_frame_view);
        mEmptyViewContainer = (FrameLayout) findViewById(R.id.empty_view_container);
        mRecyclerViewContainer = (FrameLayout) findViewById(R.id.recycler_view_container);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mFooterViewContainer = (FrameLayout) findViewById(R.id.footer_view);
        mFooterViewContainer.setVisibility(View.GONE);
    }

    private void setupLoadView() {
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }


    public void setHeaderView(View header) {

        if (!(header instanceof PtrUIHandler)) {
            throw new IllegalArgumentException("header view must implements PtrUIHandler");
        }
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler((PtrUIHandler) header);


    }

    public void autoRefresh() {
        autoRefresh(true);
    }

    public void autoRefresh(boolean atOnce) {
        mPtrFrameLayout.autoRefresh(atOnce);
    }


    public void autoRefresh(boolean atOnce, int duration) {
        mPtrFrameLayout.autoRefresh(atOnce, duration);
    }

    public void setPushRefreshEnable(boolean pushRefreshEnable) {
        isPushRefreshEnable = pushRefreshEnable;
    }

    public void setPullMoreEnable(boolean pullMoreEnable) {
        isPullMoreEnable = pullMoreEnable;
    }

    public void addFooterView(@LayoutRes int layoutId) {
        addFooterView(mInflater.inflate(layoutId, null));
    }

    public void addFooterView(View view) {
        if (view == null) {
            throw new NullPointerException("footer view can not null");
        }

        if (checkFooterView()) {
            mFooterViewContainer.removeAllViews();
        }

        mFooterViewContainer.addView(view);
    }


    public void refreshCompleted() {
        isLoading = false;
        isRefreshing = false;
        mPtrFrameLayout.refreshComplete();
        mFooterViewContainer.animate()
                .translationY(mFooterViewContainer.getHeight())
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

    }


    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
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

    public boolean isPullMoreEnable() {
        return isPullMoreEnable && checkFooterView();
    }


    private boolean checkFooterView() {
        return mFooterViewContainer.getChildCount() > 0;
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

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mRegisterCheckEmptyView) {
            unregisterAdapterDataObserver();
        }
        this.mAdapter = adapter;

        mRecyclerView.setAdapter(adapter);
        registerAdapterDataObserver();
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


    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }


    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastItem = 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int totalItemCount = layoutManager.getItemCount();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
                //Position to find the final item of the current LayoutManager
                lastItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastItem == -1) lastItem = gridLayoutManager.findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
                lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastItem == -1) lastItem = linearLayoutManager.findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
                // since may lead to the final item has more than one StaggeredGridLayoutManager the particularity of the so here that is an array
                // this array into an array of position and then take the maximum value that is the last show the position value
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                lastItem = findMax(lastPositions);
            }


            if (isPullMoreEnable()
                    && (lastItem == totalItemCount - 1)
                    && !isRefreshing()
                    && (dx > 0 || dy > 0)
                    && !isLoading) {
                if (isHasMore()) {
                    isLoading = true;
                    loadMore();
                } else {
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onAnyMore();
                    }
                }
            }

        }
    };

    //To find the maximum value in the array
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void loadMore() {
        if (hasMore && !isRefreshing) {
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
            if (mOnRefreshListener != null)
                mOnRefreshListener.onLoadMore();

        }
    }



    private void checkIfEmptyView() {
        if (mEmptyView != null && mAdapter != null) {
            if (mAdapter.getItemCount() == 0) {
                mEmptyViewContainer.setVisibility(VISIBLE);
                mRecyclerViewContainer.setVisibility(GONE);
//                showAnim(mEmptyViewContainer);
//                hideAnim(mLoadMoreRecyclerView);
            } else {
                mRecyclerViewContainer.setVisibility(VISIBLE);
//                showAnim(mLoadMoreRecyclerView);
//                hideAnim(mEmptyViewContainer);
                mEmptyViewContainer.setVisibility(GONE);

            }
        }


    }

    private void registerAdapterDataObserver() {
        if (!mRegisterCheckEmptyView && mEmptyView != null && mAdapter != null) {
            mRegisterCheckEmptyView = true;
            mAdapter.registerAdapterDataObserver(mAdapterObserver);
        }
    }

    private void unregisterAdapterDataObserver() {
        if (mRegisterCheckEmptyView) {
            mAdapter.unregisterAdapterDataObserver(mAdapterObserver);
            mRegisterCheckEmptyView = false;
        }
    }

    private boolean checkRecyclerScroll() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return !(ViewCompat.canScrollVertically(mRecyclerView, -1) || mRecyclerView.getScrollY() > 0);
        } else {
            return !ViewCompat.canScrollVertically(mRecyclerView, -1);
        }
    }

    private void setPtrHandler() {
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return checkRecyclerScroll() && isPushRefreshEnable;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onRefresh();
                }
            }
        });
    }
}
