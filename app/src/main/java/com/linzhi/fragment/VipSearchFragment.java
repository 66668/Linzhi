package com.linzhi.fragment;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.linzhi.R;
import com.linzhi.adapter.MessageListAdapter;
import com.linzhi.base.BaseFragment;
import com.linzhi.model.MessageListModel;
import com.linzhi.utils.PageUtil;
import com.linzhi.widget.RefreshAndLoadListView;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * BottomBar 应用
 */

public class VipSearchFragment extends BaseFragment {

    TextView tv_level;

    //常量
    private static final String TAG = "RegSearchFragment";
    private static final int GET_DATA_SUCCESS = 10;
    private static final int GET_DATA_FAILED = 11;
    // 列表数据
    private String[] popContents = new String[]{"钻石会员", "铂金会员", "黄金会员", "白银会员", "普通会员"};

    //单例模式
    public static VipSearchFragment newInstance() {
        VipSearchFragment messageListFragment = new VipSearchFragment();
        return messageListFragment;
    }

    //控件
    private RefreshAndLoadListView listView;
    private MessageListAdapter adapter;

    //变量
    private String maxTime = "";
    private String minTime = "";
    ArrayList<MessageListModel> listdate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_search_list, container, false);
        initView(view);
        ButterKnife.bind(view);
        //        getDate();
        return view;
    }


    //界面详细
    public void initView(View view) {
        listView = (RefreshAndLoadListView) view.findViewById(R.id.refreshList);
        tv_level = (TextView) view.findViewById(R.id.tv_level);

        adapter = new MessageListAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置popupWindow的宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int with = (int) (170 * dm.scaledDensity);//170是textView设置的dp值

        // 找到需要填充到pop的布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_list, null);
        // 根据此布局建立pop
        final PopupWindow popupWindow = new PopupWindow(view);
        // <<<<<<<<<<<<<<<<<<<极其重要>>>>>>>>>>>>>>>>>>>>>
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setWidth(with);

        //这样设置pop才可以缩回去
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // 填充此布局上的列表
        ListView listView = (ListView) view.findViewById(R.id.pop_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_content, popContents);
        listView.setAdapter(adapter);

        // 当listView受到点击时替换mTextView当前显示文本
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                tv_level.setText(popContents[arg2]);
                popupWindow.dismiss();
            }
        });
        // 当mTextView受到点击时显示pop
        tv_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(v);
            }
        });
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_DATA_SUCCESS://加载数据
                    listdate = (ArrayList<MessageListModel>) msg.obj;
                    adapter.setEntityList(listdate);
                    break;

                case GET_DATA_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;

            }
        }
    };


    @Override
    public String getFragmentName() {
        return TAG;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public boolean hasMultiFragment() {
        return false;
    }

    @Override
    protected String setFragmentName() {
        return null;
    }

    //重写setMenuVisibility方法，不然会出现叠层的现象
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

}
