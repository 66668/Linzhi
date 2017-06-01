package com.linzhi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linzhi.R;
import com.linzhi.model.MessageListModel;
import com.linzhi.utils.AppCommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by sjy on 2017/4/13.
 */

public class MessageListAdapter extends BaseAdapter {
    public ArrayList<MessageListModel> entityList = new ArrayList<MessageListModel>();
    public Context context;
    public LayoutInflater inflater;

    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;

    public class WidgetHolder {
        public TextView tv_time;
        public TextView tv_name;
        public TextView tv_level;
        public TextView tv_car;
    }

    public MessageListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public MessageListAdapter(Context context, ArrayList<MessageListModel> list) {
        this.context = context;
        this.entityList = list;
        inflater = LayoutInflater.from(context);
    }

    //listView赋值
    public void setEntityList(ArrayList entityList) {
        this.entityList.clear();
        this.entityList.addAll(entityList);
        notifyDataSetChanged();
    }

    //listView拼接
    public void addEntityList(ArrayList entityList) {
        this.entityList.addAll(entityList);
        notifyDataSetChanged();
    }

    //listView插入
    public void insertEntityList(ArrayList entityList) {
        if (entityList != null) {
            this.entityList.addAll(0, entityList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return entityList.size();
    }

    @Override
    public Object getItem(int position) {
        return entityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final WidgetHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_msg_list, null);
            holder = new WidgetHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_car = (TextView) convertView.findViewById(R.id.tv_car);
            convertView.setTag(holder);
        } else {
            holder = (WidgetHolder) convertView.getTag();
        }
        //item数据显示
        MessageListModel model = (MessageListModel) entityList.get(position);
        holder.tv_name.setText(model.getName());
        holder.tv_time.setText(model.getTime());
        holder.tv_car.setText(model.getRemake());

        String levelName = AppCommonUtils.getTransLevel(model.getClientLevel());
        holder.tv_level.setText(levelName);

        return convertView;
    }
}