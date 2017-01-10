package com.why.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> mDatas = new ArrayList<>();

    public HomeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_home, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void refresh(List<String> list) {
        if (list==null){
            list=new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void loadMore(List<String> list) {
        int startPosition = mDatas.size();
        mDatas.addAll(list);
        notifyItemInserted(startPosition);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.id_num);
        }
    }
}