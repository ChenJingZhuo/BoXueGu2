package com.boxuegu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boxuegu.R;
import com.boxuegu.bean.VideoBean;

import java.util.List;

public class VideoListAdapter extends BaseAdapter {
    private Context mContext;
    private List<VideoBean> vbl;
    private int selectedPosition=-1;
    private OnSelectListener onSelectListener;
    public VideoListAdapter(Context context, OnSelectListener onSelectListener){
        this.mContext=context;
        this.onSelectListener=onSelectListener;
    }
    public void setSelectedPosition(int position){
        selectedPosition=position;
    }
    public void setData(List<VideoBean> vbl){
        this.vbl=vbl;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return vbl==null ? 0 : vbl.size();
    }

    public VideoBean getItem(int position){
        return vbl==null ? null : vbl.get(position);
    }

    public long getItemId(int position){
        return position;
    }
    public View getView(final int position,View convertView,ViewGroup parent){
        final ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(
                    R.layout.video_list_item,null );
                    vh.tv_title=(TextView) convertView
                            .findViewById(R.id.tv_video_title);
                    vh.iv_icon=(ImageView) convertView
                            .findViewById(R.id.iv_left_icon);
                    convertView.setTag(vh);

        }else{
            vh=(ViewHolder) convertView.getTag();
        }
        final VideoBean bean =getItem(position);
        vh.iv_icon.setImageResource(R.drawable.course_bar_icon);
        vh.tv_title.setTextColor(Color.parseColor("#333333"));
        if (bean!=null){
            vh.tv_title.setText(bean.secondTitle);
            if(selectedPosition==position){
                vh.iv_icon.setImageResource(R.drawable.course_intro_icon);
                vh.tv_title.setTextColor(Color.parseColor("#009958"));
            }else{
                vh.iv_icon.setImageResource(R.drawable.course_bar_icon);
                vh.tv_title.setTextColor(Color.parseColor("#333333"));
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bean==null){
                    return;
                }
                //播放视频
                onSelectListener.onSelect(position,vh.iv_icon);
            }
        });
        return convertView;
    }
    class ViewHolder{
        public TextView tv_title;
        public ImageView iv_icon;
    }
    public interface OnSelectListener{
        void onSelect(int position, ImageView iv);
    }
}
