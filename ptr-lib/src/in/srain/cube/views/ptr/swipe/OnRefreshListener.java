package in.srain.cube.views.ptr.swipe;

public interface OnRefreshListener {
    void onRefresh();

    void onLoadMore();

    /**
     * 没有更多了
     */
    void onAnyMore();
}