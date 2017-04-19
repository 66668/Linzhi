package com.linzhi.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.linzhi.R;
import com.linzhi.adapter.MessageListAdapter;
import com.linzhi.base.BaseFragment;
import com.linzhi.common.MyException;
import com.linzhi.dialog.Loading;
import com.linzhi.helper.UserHelper;
import com.linzhi.model.DetailModel;
import com.linzhi.model.MessageListModel;
import com.linzhi.utils.PageUtil;
import com.linzhi.utils.Utils;
import com.linzhi.widget.DetailModelDialog;
import com.linzhi.widget.RefreshAndLoadListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * BottomBar 应用
 */

public class MessageListFragment extends BaseFragment implements RefreshAndLoadListView.ILoadMoreListener, RefreshAndLoadListView.IReflashListener {

    @BindView(R.id.refreshList)
    RefreshAndLoadListView listView;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.et_searchName)
    EditText et_searchName;

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
    private MessageListAdapter adapter;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private DatePickerDialog.OnDateSetListener mDateSetListener;//日历

    //变量
    private String maxTime = "";
    private String minTime = "";
    private String searchTime;
    private String searchName;
    String currentTime;
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
        View view = inflater.inflate(R.layout.frg_msg_list, container, false);

        ButterKnife.bind(this, view);//绑定framgent

        initView(view);
        getDate();
        initListener();
        return view;
    }


    //界面详细
    public void initView(View view) {
        currentTime = Utils.getCurrentDate();

        tv_time.setText(currentTime);
        adapter = new MessageListAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    private void initListener() {
        //详情数据显示
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
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    List<MessageListModel> listdate = UserHelper.getMessageList(getActivity(), maxTime, "");
                    if (listdate == null || listdate.size() <= 0) {
                        handler.sendMessage(handler.obtainMessage(FRESH_DATA_FAILED, "没有最新数据"));
                    } else {
                        handler.sendMessage(handler.obtainMessage(FRESH_DATA_SUCCESS, listdate));
                    }
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

                    if (listdate == null || listdate.size() <= 0) {
                        handler.sendMessage(handler.obtainMessage(LOAD_DATA_FAILED, "没有最新数据"));
                    } else {
                        handler.sendMessage(handler.obtainMessage(LOAD_DATA_SUCCESS, listdate));
                    }
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
                    if (listdate == null || listdate.size() <= 0) {
                        handler.sendMessage(handler.obtainMessage(GET_DATA_FAILED, "没有最新数据"));
                    } else {
                        handler.sendMessage(handler.obtainMessage(GET_DATA_SUCCESS, listdate));
                    }


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

    /**
     * 查询数据
     */
    @OnClick(R.id.tv_search)
    public void searchDate(View view) {
        searchName = et_searchName.getText().toString();
        Log.d(TAG, "searchDate: 查询数据--searchTime=" + searchTime + "--searchName=" + searchName);

        //        if (TextUtils.isEmpty(searchTime)) {
        //            PageUtil.DisplayToast("查询时间请在选择确定！");
        //            return;
        //        }
        //        if (TextUtils.isEmpty(searchName)) {
        //            PageUtil.DisplayToast("查询时间请在选择确定！");
        //            return;
        //        }
        try {
            UserHelper.searchMessageList(getActivity(),searchTime,searchTime);
        } catch (MyException e) {
            e.printStackTrace();
        }
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
     * 弹窗现实详情
     */
    private void dialogShow(DetailModel model) {
        Log.d("SJY", "dialogShow: 弹窗详情数据：" + (new Gson()).toJson(model).toString());


        final DetailModelDialog dialog = new DetailModelDialog(getActivity(), model);
        dialog.show();
        dialog.setClicklistener(new DetailModelDialog.ClickListenerInterface() {
            @Override
            public void forSure() {
                dialog.dismiss();
            }

            @Override
            public void forCancel() {
                dialog.dismiss();
            }
        });
    }

    //赋值
    private void setMaxTime(MessageListModel model) {
        maxTime = model.getTime();
    }

    private void setMinTime(MessageListModel model) {
        minTime = model.getTime();
    }

    /**
     * 选择时间
     */
    @OnClick(R.id.tv_time)
    public void CalendarClick() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                searchTime = year + "-" + ((month > 9) ? month : ("0" + month)) + "-" + ((day > 9) ? day : ("0" + day));
                tv_time.setText(searchTime);
            }
        };
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
