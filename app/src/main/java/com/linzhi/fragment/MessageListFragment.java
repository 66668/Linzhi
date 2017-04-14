package com.linzhi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.linzhi.R;
import com.linzhi.adapter.MessageListAdapter;
import com.linzhi.base.BaseFragment;
import com.linzhi.common.MyException;
import com.linzhi.dialog.Loading;
import com.linzhi.helper.UserHelper;
import com.linzhi.model.DetailModel;
import com.linzhi.model.MessageListModel;
import com.linzhi.utils.PageUtil;
import com.linzhi.widget.RefreshAndLoadListView;

import java.util.ArrayList;
import java.util.List;


/**
 * BottomBar 应用
 */

public class MessageListFragment extends BaseFragment implements RefreshAndLoadListView.ILoadMoreListener, RefreshAndLoadListView.IReflashListener {

    //常量
    private static final String TAG = "MessageListFragment";
    private static final int GET_DATA_SUCCESS = 10;
    private static final int FRESH_DATA_SUCCESS = 11;
    private static final int LOAD_DATA_SUCCESS = 12;
    private static final int GET_DETAIL_SUCCESS = 9;

    private static final int GET_DATA_FAILED = 13;
    private static final int FRESH_DATA_FAILED = 14;
    private static final int LOAD_DATA_FAILED = 15;

    //控件
    private RefreshAndLoadListView listView;
    private MessageListAdapter adapter;

    //变量
    private String maxTime = "";
    private String minTime = "";
    private ArrayList<MessageListModel> listdate;
    private DetailModel model;

    //单例模式
    public static MessageListFragment newInstance() {
        MessageListFragment messageListFragment = new MessageListFragment();
        return messageListFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_msg_list, container, false);
        initView(view);
        getDate();
        initListener();
        return view;
    }


    //界面详细
    public void initView(View view) {
        listView = (RefreshAndLoadListView) view.findViewById(R.id.refreshList);

        adapter = new MessageListAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewsCount = listView.getHeaderViewsCount();//得到header的总数量
                int newPosition = position - headerViewsCount;//得到新的修正后的position

                MessageListModel model = (MessageListModel) adapter.getItem(newPosition);//
                getItemDetail(model.getClientId());
            }
        });
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    List<MessageListModel> listdate = UserHelper.getMessageList(getActivity(), maxTime, "");
                    handler.sendMessage(handler.obtainMessage(FRESH_DATA_SUCCESS, listdate));
                } catch (MyException e) {
                    Log.d(TAG, "run: error=" + e.toString());
                    PageUtil.DisplayToast(e.toString());
                    handler.sendMessage(handler.obtainMessage(FRESH_DATA_FAILED, e.toString()));
                }
            }
        });
    }

    /**
     * 加载
     */
    @Override
    public void onLoadMore() {
        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    List<MessageListModel> listdate = UserHelper.getMessageList(getActivity(), "", minTime);
                    handler.sendMessage(handler.obtainMessage(LOAD_DATA_SUCCESS, listdate));
                } catch (MyException e) {
                    Log.d(TAG, "run: error=" + e.toString());
                    PageUtil.DisplayToast(e.toString());
                    handler.sendMessage(handler.obtainMessage(LOAD_DATA_FAILED, e.toString()));

                }
            }
        });

    }

    /**
     * 获取数据
     */
    private void getDate() {
        //        //测试 msg
        minTime = "";
        maxTime = "";
        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    List<MessageListModel> listdate = UserHelper.getMessageList(getActivity(), maxTime, minTime);
                    handler.sendMessage(handler.obtainMessage(GET_DATA_SUCCESS, listdate));
                } catch (MyException e) {
                    Log.d(TAG, "run: error=" + e.toString());
                    handler.sendMessage(handler.obtainMessage(GET_DATA_FAILED, e.toString()));
                }
            }
        });
    }

    /**
     * 获取详情数据
     */
    private void getItemDetail(final String clientidDD) {

        //详情
        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    DetailModel model = UserHelper.getItemDetail(getActivity(), clientidDD);
                    handler.sendMessage(handler.obtainMessage(GET_DETAIL_SUCCESS, model));

                } catch (MyException e) {
                    e.printStackTrace();
                }
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
                    setMaxTime(listdate.get(0));
                    setMinTime(listdate.get(listdate.size() - 1));
                    adapter.setEntityList(listdate);
                    break;
                case FRESH_DATA_SUCCESS:
                    listdate = (ArrayList<MessageListModel>) msg.obj;
                    setMaxTime(listdate.get(0));
                    adapter.insertEntityList(listdate);
                    break;
                case LOAD_DATA_SUCCESS:
                    listdate = (ArrayList<MessageListModel>) msg.obj;
                    setMinTime(listdate.get(listdate.size() - 1));
                    adapter.addEntityList(listdate);
                    break;

                case GET_DETAIL_SUCCESS:
                    model = (DetailModel) msg.obj;
                    dialogShow(model);
                    break;


                case GET_DATA_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;
                case FRESH_DATA_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    listView.loadAndFreshComplete();
                    break;

                case LOAD_DATA_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    listView.loadAndFreshComplete();
                    break;
            }
        }
    };

    /**
     * 弹窗现实想请
     */
    private void dialogShow(DetailModel model) {

    }
    //赋值
    private void setMaxTime(MessageListModel model) {
        maxTime = model.getTime();
    }

    private void setMinTime(MessageListModel model) {
        minTime = model.getTime();
    }


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
