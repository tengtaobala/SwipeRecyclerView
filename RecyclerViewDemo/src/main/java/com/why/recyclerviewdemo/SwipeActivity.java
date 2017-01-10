package com.why.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.Toast;

import com.why.recyclerviewdemo.new1.PtrClassicHeader;
import com.why.recyclerviewdemo.new1.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {

    private SwipeRecyclerView mSwipeRecyclerView;

    private HomeAdapter mAdapter;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_main);
        mSwipeRecyclerView = (SwipeRecyclerView) findViewById(R.id.swpie_view);
        mSwipeRecyclerView.setLinearManager();
        mSwipeRecyclerView.setAdapter(mAdapter = new HomeAdapter(this));
        mSwipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mSwipeRecyclerView.addFooterView(R.layout.footer_layout_default);
        mSwipeRecyclerView.setEmptyView(R.layout.layout_empty);
        mSwipeRecyclerView.setHeaderView(new PtrClassicHeader(this));
        initDatas();
        mSwipeRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 15; i++) {
                            list.add("refresh item " + (i + 1));
                        }
                        mAdapter.refresh(list);
                        mSwipeRecyclerView.refreshCompleted();

                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                mSwipeRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            list.add("load more item " + index + " " + (i + 1));
                        }
                        index++;
                        mAdapter.loadMore(list);
                        mSwipeRecyclerView.refreshCompleted();
                    }
                }, 2000);
            }

            @Override
            public void onAnyMore() {
                Toast.makeText(SwipeActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void initDatas() {
        mSwipeRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRecyclerView.autoRefresh(true);
            }
        },500);
    }

    public void clearClick(View view) {
        if (mAdapter.getItemCount() == 0) {
            initDatas();
        } else
            mAdapter.refresh(null);
    }


}
