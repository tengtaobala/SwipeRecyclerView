package com.why.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainLoadMoreActivity extends AppCompatActivity {

    private LoadMoreRecyclerView mLoadMoreRecyclerView;

    private HomeAdapter mAdapter;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadMoreRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.load_more_recycler_view);
        //设置布局管理器
        mLoadMoreRecyclerView.setLinearManager();
//设置adapter
        mLoadMoreRecyclerView.setAdapter(mAdapter = new HomeAdapter(this));
//设置Item增加、移除动画
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
                Toast.makeText(MainLoadMoreActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDatas() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("first item " + (i + 1));
        }
        mAdapter.refresh(list);
    }


}
