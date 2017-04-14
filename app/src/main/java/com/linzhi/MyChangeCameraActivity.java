package com.linzhi;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.linzhi.fragment.CameraFragment;
import com.linzhi.fragment.VipRegistFragment;


/**
 * 自定义camera
 *
 * @author JackSong
 */

public class MyChangeCameraActivity extends FragmentActivity {
    private static final String TAG = "MyChangeCameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.act_mychange_camera);

        if (savedInstanceState == null) {
            // 主Activity以frame填充Activity的方式交互管理Fragment
            Log.d(TAG, "CameraFragment<---savedInstanceState == null=" + (savedInstanceState == null));
            //相机详细操作
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance())
                    .commit();
        }
    }

    //保存图片路径，返回给回调函数使用
    public void returnPhotoUri(Uri uri) {
        Log.d(TAG, "保存图片路径，返回给回调函数使用");

        Intent data = new Intent();
        data.setData(uri);

        if (getParent() == null) {
            Log.d(TAG, "returnPhotoUri:getParent()==null");
            setResult(VipRegistFragment.RESULT_OK, data);
        } else {
            Log.d(TAG, "returnPhotoUri:getParent()!=null");
            getParent().setResult(VipRegistFragment.RESULT_OK, data);
        }

        finish();
    }

    public void onCancel(View view) {
        getSupportFragmentManager().popBackStack();
    }
}