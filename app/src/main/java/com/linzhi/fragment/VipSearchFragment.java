package com.linzhi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.linzhi.VipUpdateActivity;
import com.linzhi.R;
import com.linzhi.adapter.VipSearchAdapter;
import com.linzhi.base.BaseFragment;
import com.linzhi.common.MyException;
import com.linzhi.dialog.DetailRecordDialog;
import com.linzhi.dialog.Loading;
import com.linzhi.helper.UserHelper;
import com.linzhi.model.DetailModel;
import com.linzhi.model.VipSearchListModel;
import com.linzhi.utils.AppCommonUtils;
import com.linzhi.utils.PageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * BottomBar 应用
 */

public class VipSearchFragment extends BaseFragment {

    //搜索姓名
    @BindView(R.id.tv_searchName)
    TextView tv_searchName;

    //客户等级
    TextView tv_level;

    //搜索
    @BindView(R.id.tv_search)
    TextView tv_search;

    ListView listView;

    //刷新
    @BindView(R.id.tv_fresh)
    TextView tv_fresh;

    //
    //常量
    private static final String TAG = "VipSearchFragment";
    private static final int SEARCH_DETA_SUCCESS = 10;
    private static final int SEARCH_DETA_FAILED = 11;
    private static final int SEARCH_DETAILE_SUCCESS = 12;
    private static final int SEARCH_DETAILE_FAILED = 13;
    // 会员列表数据 等级依次是1--5
    private String[] popContents = new String[]{"钻石会员", "铂金会员", "黄金会员", "白银会员", "普通会员"};

    //单例模式
    public static VipSearchFragment newInstance() {
        VipSearchFragment messageListFragment = new VipSearchFragment();
        return messageListFragment;
    }

    //控件
    private VipSearchAdapter adapter;
    //变量
    private String searchName = "";
    private String levelNum = "";
    ArrayList<VipSearchListModel> listdate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_search_list, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initListener();
        //        getDate();
        return view;
    }


    //界面详细
    public void initView(View view) {

        listView = (ListView) view.findViewById(R.id.refreshList);
        tv_level = (TextView) view.findViewById(R.id.tv_level);

        adapter = new VipSearchAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    //监听
    private void initListener() {
        //点击查看详情
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemClick: item详情position=" + position);
                VipSearchListModel model = (VipSearchListModel) adapter.getItem(position);
                getDetailDate(model.getClientID());
            }
        });
    }

    //搜索
    @OnClick(R.id.tv_search)
    public void ForSearch(View view) {

        adapter = new VipSearchAdapter(getActivity());
        listView.setAdapter(adapter);

        searchName = tv_searchName.getText().toString();
        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                JSONObject json = new JSONObject();
                try {
                    json.put("clientLevel", levelNum);
                    json.put("clientName", searchName);

                    List<VipSearchListModel> list = UserHelper.searchVip(getActivity(), json);
                    if (list != null && list.size() > 0) {
                        handler.sendMessage(handler.obtainMessage(SEARCH_DETA_SUCCESS, list));
                    } else {
                        handler.sendMessage(handler.obtainMessage(SEARCH_DETA_FAILED, "没有最新数据！"));
                    }

                } catch (MyException e) {
                    Log.d(TAG, "run: e=" + e.getMessage());
                    handler.sendMessage(handler.obtainMessage(SEARCH_DETA_FAILED, e.getMessage()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * vip查询详情
     */
    private void getDetailDate(final String clientID) {
        //详情
        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    DetailModel model = UserHelper.getItemDetail(getActivity(), clientID);
                    handler.sendMessage(handler.obtainMessage(SEARCH_DETAILE_SUCCESS, model));

                } catch (MyException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: error=" + e.getMessage());
                    handler.sendMessage(handler.obtainMessage(SEARCH_DETAILE_FAILED, e.getMessage()));
                }
            }
        });
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEARCH_DETA_SUCCESS://
                    listdate = (ArrayList<VipSearchListModel>) msg.obj;
                    adapter.setEntityList(listdate);
                    break;

                case SEARCH_DETA_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;
                case SEARCH_DETAILE_SUCCESS://
                    DetailModel model=(DetailModel) msg.obj;
                    dialogShow(model);
                    break;

                case SEARCH_DETAILE_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;

            }
        }
    };

    /**
     * 弹窗现实详情
     */
    private void dialogShow(final DetailModel model) {
        Log.d("SJY", "dialogShow: 弹窗详情数据：" + (new Gson()).toJson(model).toString());


        final DetailRecordDialog dialog = new DetailRecordDialog(getActivity(), model);
        dialog.show();
        dialog.setClicklistener(new DetailRecordDialog.ClickListenerInterface() {
            @Override
            public void forSure() {
                //修改
                Intent intent = new Intent(getActivity(), VipUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DetailModel", model);
                intent.putExtras(bundle);
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void forCancel() {
                dialog.dismiss();
            }
        });
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


    /**
     * 设置 会员textView弹窗显示
     */

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
                String str = popContents[arg2];
                levelNum = AppCommonUtils.getLevelNum(str);
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

    //重写setMenuVisibility方法，不然会出现叠层的现象
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }
}
