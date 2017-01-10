package com.why.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengtao on 2017/1/6.
 */

public class WrapperActivity extends AppCompatActivity {
    private WrapperRecyclerView mWrapperRecyclerView;
    private LoadMoreRecyclerView mLoadMoreRecyclerView;
    private HomeAdapter mAdapter;
    private int index = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper);
        mWrapperRecyclerView = (WrapperRecyclerView) findViewById(R.id.wapper_recycler_view);
        mWrapperRecyclerView.setEmptyView(R.layout.layout_empty);
        mWrapperRecyclerView.setAdapter(mAdapter = new HomeAdapter(this));


        mLoadMoreRecyclerView = mWrapperRecyclerView.getLoadMoreRecyclerView();

        mLoadMoreRecyclerView.setLinearManager();
        mLoadMoreRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLoadMoreRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        initDatas();

        mLoadMoreRecyclerView.setOnPullLoadMoreListener(new OnPullLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (index >= 3) {
                    mLoadMoreRecyclerView.setRefreshing(true);
                }


                mLoadMoreRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            list.add("load more item " + index + " " + (i + 1));
                        }
                        index++;
                        mAdapter.loadMore(list);
                        mLoadMoreRecyclerView.pullLoadMoreCompleted();
                    }
                }, 2000);

            }

            @Override
            public void onAnyMore() {
                Toast.makeText(WrapperActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDatas() {
//        mLoadMoreRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//              mAdapter.refresh(null);
//            }
//        },5000);
        mLoadMoreRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 15; i++) {
                    list.add("first item " + (i + 1));
                }
                mAdapter.refresh(list);
            }
        }, 1000);
//
//        mLoadMoreRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mAdapter.refresh(null);
//            }
//        },15000);
    }

}
