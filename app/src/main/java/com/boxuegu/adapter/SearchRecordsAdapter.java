package com.boxuegu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boxuegu.R;

import java.util.List;

public class SearchRecordsAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mSearchList;
    private LayoutInflater mInflater;
    private int SEARCH_RECORD_COUNT;
    private int itemCount = SEARCH_RECORD_COUNT;

    public SearchRecordsAdapter(Context context, List<String> searchRecordsList) {
        this.mContext = context;
        this.mSearchList = searchRecordsList;
        mInflater = LayoutInflater.from(context);
        SEARCH_RECORD_COUNT=searchRecordsList.size();
    }


    @Override
    public int getCount() {
        if (mSearchList.size() > SEARCH_RECORD_COUNT) {
            return itemCount;
        } else {
            return mSearchList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mSearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.record_item, null);
            viewHolder.recordTv = convertView.findViewById(R.id.search_content_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String count = mSearchList.get(position);

        viewHolder.recordTv.setText(count);
        return convertView;
    }

    private class ViewHolder {
        TextView recordTv;
    }

    /**
     * 点击后设置Item的数量
     *
     * @param number Item的数量
     */
    public void addItemNum(int number) {
        itemCount = number;
    }

}
