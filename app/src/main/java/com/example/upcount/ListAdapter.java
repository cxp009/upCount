package com.example.upcount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<UpList> lists = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public ListAdapter(Context context, List<UpList> lists){
        this.lists = lists;
        this.mContext=context;
        mLayoutInflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public TextView mTvCost, mTvTime, mTvCount;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        UpList entity = lists.get(position);
        if (convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.layout_list,null);
            holder=new ViewHolder();
            holder.mTvCost = convertView.findViewById(R.id.list_username);
            holder.mTvCount = convertView.findViewById(R.id.list_count);
            holder.mTvTime = convertView.findViewById(R.id.list_time);
            //设置记录界面
            holder.mTvCost.setText(entity.getUsername());
            holder.mTvTime.setText(entity.getTime());
            holder.mTvCount.setText("￥" + entity.getCount());
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
