package com.why.recyclerviewdemo;

/**
 * 加载更多回调
 */
public interface OnPullLoadMoreListener {
    void onLoadMore();

    /**
     * 没有更多了
     */
    void onAnyMore();
}