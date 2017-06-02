package com.linzhi;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.linzhi.base.BaseActivity;
import com.linzhi.base.BaseFragment;
import com.linzhi.fragment.MessageListFragment;
import com.linzhi.fragment.VipRegistFragment;
import com.linzhi.fragment.VipSearchFragment;
import com.linzhi.inject.ViewInject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjy on 2017/3/23.
 */

public class MainActivity extends BaseActivity {

    //退出
    @ViewInject(id = R.id.tv_quit, click = "toQuit")
    TextView tv_quit;


    //信息列表
    @ViewInject(id = R.id.btn_message)
    RadioButton btn_msg_List;

    //信息查询
    @ViewInject(id = R.id.btn_search)
    RadioButton btn_search_vip;

    //vip
    @ViewInject(id = R.id.btn_vip)
    RadioButton btn_vip_reg;

    @ViewInject(id = R.id.viewPaper)
    ViewPager viewPaper;

    @ViewInject(id = R.id.radiogroup)
    RadioGroup mRadioGroup;

    //变量
    MessageListFragment msgListFragment;
    VipSearchFragment msgSearchFragment;
    VipRegistFragment vipFragment;

    private int currentFragment;
    private List<BaseFragment> listFragment;

    private static final String TAG = "SJY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_main);

        initFragment();
        initListener();
        text();


    }

    private void text() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int hight = metric.heightPixels;
        float density = metric.density;
        float densityDpi = metric.densityDpi;
        final float scale = getResources().getDisplayMetrics().density;
        Log.d(TAG, "dp宽=" + (width / density) + "--dp高=" + (hight / density) + "--densityDpi=" + densityDpi + "--density=" + density + "--densityDpi=" + densityDpi + "--scale" + scale);
    }

    private void initFragment() {

        msgListFragment = MessageListFragment.newInstance();
        msgSearchFragment = VipSearchFragment.newInstance();
        vipFragment = VipRegistFragment.newInstance();

        listFragment = new ArrayList<>();
        listFragment.add(msgListFragment);
        listFragment.add(msgSearchFragment);
        listFragment.add(vipFragment);
        viewPaper.setOffscreenPageLimit(3);
        viewPaper.setOnPageChangeListener(onPageChangeListener);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mRadioGroup.check(R.id.btn_message);
                    break;
                case 1:
                    mRadioGroup.check(R.id.btn_search);
                    break;
                case 2:
                    mRadioGroup.check(R.id.btn_vip);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.btn_message:
                        currentFragment = 0;
                        break;

                    case R.id.btn_search:
                        currentFragment = 1;
                        break;

                    case R.id.btn_vip:
                        currentFragment = 2;
                        break;

                }


                viewPaper.setCurrentItem(currentFragment, false);

            }
        });

        viewPaper.setAdapter(new FragmentPagerAdapter(
                getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return listFragment.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return listFragment.get(arg0);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                super.destroyItem(container, position, object);
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fixInputMethodManagerLeak(getApplicationContext());

    }

    public void toQuit(View view) {
        Intent intent = new Intent();
        intent.setAction(EXIT_APP_ACTION);
        sendBroadcast(intent);//发送退出的广播
    }

    //
    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) {
            return;
        }
        try {
            // 对 mCurRootView mServedView mNextServedView 进行置空...
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }// author:sodino mail:sodino@qq.com

            Object obj_get = null;
            Field f_mCurRootView = imm.getClass().getDeclaredField("mCurRootView");
            Field f_mServedView = imm.getClass().getDeclaredField("mServedView");
            Field f_mNextServedView = imm.getClass().getDeclaredField("mNextServedView");

            if (f_mCurRootView.isAccessible() == false) {
                f_mCurRootView.setAccessible(true);
            }
            obj_get = f_mCurRootView.get(imm);
            if (obj_get != null) { // 不为null则置为空
                f_mCurRootView.set(imm, null);
            }

            if (f_mServedView.isAccessible() == false) {
                f_mServedView.setAccessible(true);
            }
            obj_get = f_mServedView.get(imm);
            if (obj_get != null) { // 不为null则置为空
                f_mServedView.set(imm, null);
            }

            if (f_mNextServedView.isAccessible() == false) {
                f_mNextServedView.setAccessible(true);
            }
            obj_get = f_mNextServedView.get(imm);
            if (obj_get != null) { // 不为null则置为空
                f_mNextServedView.set(imm, null);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 后台运行设置(无法设置home监听)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "onKeyDown: back键监听");
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startActivitySafely(startIntent);
            return true;
        } else
            return super.onKeyDown(keyCode, event);

    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
