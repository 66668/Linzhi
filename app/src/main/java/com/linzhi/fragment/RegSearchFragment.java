package com.linzhi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linzhi.R;
import com.linzhi.adapter.MessageListAdapter;
import com.linzhi.base.BaseFragment;
import com.linzhi.model.MessageListModel;
import com.linzhi.utils.PageUtil;
import com.linzhi.widget.RefreshAndLoadListView;

import java.util.ArrayList;


/**
 * BottomBar 应用
 */

public class RegSearchFragment extends BaseFragment {

    //常量
    private static final String TAG = "RegSearchFragment";
    private static final int GET_DATA_SUCCESS = 10;
    private static final int GET_DATA_FAILED = 11;

    //单例模式
    public static RegSearchFragment newInstance() {
        RegSearchFragment messageListFragment = new RegSearchFragment();
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
        View view = inflater.inflate(R.layout.act_search_list, container, false);
        initView(view);
//        getDate();
        return view;
    }


    //界面详细
    public void initView(View view) {
        listView = (RefreshAndLoadListView) view.findViewById(R.id.refreshList);

        adapter = new MessageListAdapter(getActivity());
        listView.setAdapter(adapter);
    }

//    /**
//     * 获取数据
//     */
//    private void getDate() {
//        //        //测试 msg
//        minTime = "";
//        maxTime = "";
//        Loading.run(getActivity(), new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    List<MessageListModel> listdate = UserHelper.getMessageList(getActivity(), maxTime, minTime);
//                    handler.sendMessage(handler.obtainMessage(GET_DATA_SUCCESS, listdate));
//                } catch (MyException e) {
//                    Log.d(TAG, "run: error=" + e.toString());
//                    handler.sendMessage(handler.obtainMessage(GET_DATA_FAILED, e.toString()));
//                }
//            }
//        });
//    }

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
