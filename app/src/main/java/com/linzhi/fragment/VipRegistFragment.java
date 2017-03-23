package com.linzhi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.linzhi.R;
import com.linzhi.base.BaseFragment;

import java.util.ArrayList;


/**
 * BottomBar 应用
 */

public class VipRegistFragment extends BaseFragment {
    private static final String TAG = "MessageListFragment";
    private ViewPager viewPager;
    private ArrayList<GridView> array;//分页使用
    //    private LinePageIndicator mIndicator;//横条

    //单例模式
    public static VipRegistFragment newInstance() {
        VipRegistFragment messageListFragment = new VipRegistFragment();
        return messageListFragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_msg_list, container, false);
        initView(view);
        return view;
    }

    //界面详细
    public void initView(View view) {

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
