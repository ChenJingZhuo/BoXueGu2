package com.boxuegu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boxuegu.activity.ExercisesDetailActivity;
import com.boxuegu.R;
import com.boxuegu.bean.ExercisesBean;

import java.util.List;

public class ExercisesAdapter extends BaseAdapter {
    private Context mContext;
    private List<ExercisesBean> ebl;
    public ExercisesAdapter(Context context){
        this.mContext=context;
    }

    /**
     * 设置数据更新界面
     * @param ebl
     */
    public void setData(List<ExercisesBean> ebl){
        this.ebl=ebl;
        notifyDataSetChanged();
    }

    /**
     * 获取Item的总数
     * @return
     */
    @Override
    public int getCount() {
        return ebl==null?0:ebl.size();
    }

    /**
     * 根据position得到对应Item对象
     * @param position
     * @return
     */
    @Override
    public ExercisesBean getItem(int position) {
        return ebl==null?null:ebl.get(position);
    }

    /**
     * 根据position得到对应的Item的id
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 得到相应position对应的Item视图，position是当前Item的位置
     * @param position
     * @param convertView   滑出屏幕的Item的View
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        //复用convertView
        if(convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.exercises_list_item,null);
            vh.title=(TextView)convertView.findViewById(R.id.tv_title);
            vh.content=(TextView)convertView.findViewById(R.id.tv_content);
            vh.order=(TextView) convertView.findViewById(R.id.tv_order);
            convertView.setTag(vh);
        }else {
            vh=(ViewHolder) convertView.getTag();
        }
        //获取position对应的Item的数据对象
        final ExercisesBean bean=getItem(position);
        if(bean!=null){
            vh.order.setText(position+1+"");
            vh.title.setText(bean.title);
            vh.content.setText(bean.content);
            vh.order.setBackgroundResource(bean.background);
        }

        //每个Item的点击事件
        convertView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(bean==null)
                    return;
                Intent intent=new Intent(mContext, ExercisesDetailActivity.class);
                intent.putExtra("id",bean.id);
                intent.putExtra("title",bean.title);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public TextView title,content;
        public TextView order;
    }
}
