package com.linzhi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linzhi.R;
import com.linzhi.model.SiteIDModel;

import java.util.ArrayList;

/**
 * Created by sjy on 2017/4/12.
 */

public class SiteIDSpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SiteIDModel> provinceList = new ArrayList<SiteIDModel>();

    public SiteIDSpinnerAdapter(Context context,ArrayList<SiteIDModel> provinceList) {
        this.context = context;
        this.provinceList.addAll(provinceList);
    }

    @Override
    public int getCount() {
        return provinceList.size();
    }

    @Override
    public SiteIDModel getItem(int position) {
        return provinceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = View.inflate(context, R.layout.act_spinner_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.item_txt);
        textView.setText(provinceList.get(position).getSiteName());
        return convertView;
    }
}
