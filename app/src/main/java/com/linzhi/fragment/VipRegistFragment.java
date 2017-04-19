package com.linzhi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.linzhi.MainActivity;
import com.linzhi.MyChangeCameraActivity;
import com.linzhi.R;
import com.linzhi.base.BaseFragment;
import com.linzhi.common.MyException;
import com.linzhi.dialog.Loading;
import com.linzhi.helper.UserHelper;
import com.linzhi.utils.ImageUtils;
import com.linzhi.utils.PageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * vip注册
 */

public class VipRegistFragment extends BaseFragment {

    //图片
    @BindView(R.id.img)
    ImageView img;

    //姓名
    @BindView(R.id.et_name)
    EditText et_name;

    //电话
    @BindView(R.id.et_phone)
    EditText et_phone;

    //身份证
    @BindView(R.id.et_cardid)
    EditText et_cardid;

    //备注
    @BindView(R.id.et_remark)
    EditText et_remark;

    //性别 1男   2女
    @BindView(R.id.radiogroup_gender)
    RadioGroup radiogroup_gender;

    @BindView(R.id.radioBtn_male)
    RadioButton radioBtn_male;

    @BindView(R.id.radioBtn_female)
    RadioButton radioBtn_female;

    //等级
    @BindView(R.id.radiogroup_level)
    RadioGroup radiogroup_level;

    @BindView(R.id.radioBtn_level1)
    RadioButton radioBtn_level1;

    @BindView(R.id.radioBtn_level2)
    RadioButton radioBtn_level2;

    @BindView(R.id.radioBtn_level3)
    RadioButton radioBtn_level3;

    @BindView(R.id.radioBtn_level4)
    RadioButton radioBtn_level4;

    @BindView(R.id.radioBtn_level5)
    RadioButton radioBtn_level5;

    //注册
    @BindView(R.id.btn_signin)
    Button btn_signin;

    //常量
    private static final String TAG = "VipRegistFragment";
    private static final int REQUEST_CAMERA = 22;
    public static final int RESULT_OK = 23;

    //变亮
    private String name;
    private String gender;
    private String phone;
    private String cardid;
    private String level;
    private String clientid;
    private String remark;
    private File picPath = null;
    private Point mPoint;//获取屏幕像素尺寸
    private Bitmap picbitmap;


    //单例模式
    public static VipRegistFragment newInstance() {
        VipRegistFragment messageListFragment = new VipRegistFragment();
        return messageListFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_vip_reg, container, false);
        ButterKnife.bind(this, view);//绑定framgent

        //获取屏幕像素尺寸
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        mPoint = new Point();
        display.getSize(mPoint);
        return view;
    }

    /**
     * 注册
     */
    @OnClick(R.id.btn_signin)
    public void btn_signin() {
        getInput();//获取输入内容
        if (isEmpty()) {
            return;
        }

        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {
                    String base64 = ImageUtils.imgToBase64(null, picbitmap);
                    JSONObject js = new JSONObject();
                    try {
                        js.put("image", base64);
                        js.put("clientid", clientid);
                        js.put("remark", remark);
                        js.put("name", name);
                        js.put("gender", gender);
                        js.put("IDCardNo", cardid);
                        js.put("level", level);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "getMessageList: js=" + js.toString());
                    UserHelper.postVipRegist(getActivity(), js);
                } catch (MyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getInput() {
        name = et_name.getText().toString();
        phone = et_phone.getText().toString();
        cardid = et_cardid.getText().toString();
        clientid = et_cardid.getText().toString();
        remark = et_remark.getText().toString();
        gender = radiogroup_gender.getCheckedRadioButtonId() == R.id.radioBtn_male ? "1" : "2";//性别
    }

    /**
     * 客户等级
     */
    @OnClick({R.id.radioBtn_level1, R.id.radioBtn_level2, R.id.radioBtn_level3, R.id.radioBtn_level4, R.id.radioBtn_level5})
    public void getLevel(View view) {
        switch (view.getId()) {
            case R.id.radioBtn_level1:
                level = "1";
                break;
            case R.id.radioBtn_level2:
                level = "2";
                break;
            case R.id.radioBtn_level3:
                level = "3";
                break;
            case R.id.radioBtn_level4:
                level = "4";
                break;
            case R.id.radioBtn_level5:
                level = "5";
                break;
        }
    }

    /**
     * 非空判断
     */
    private boolean isEmpty() {
        //非空验证
        if (TextUtils.isEmpty(name.trim())) {
            PageUtil.DisplayToast("姓名不能为空");
            return true;
        }
        if (TextUtils.isEmpty(phone.trim())) {
            PageUtil.DisplayToast("手机号不能为空");
            return true;
        }
        if (TextUtils.isEmpty(cardid.trim())) {
            PageUtil.DisplayToast("身份证号不能为空");
            return true;
        }
        return false;
    }

    /**
     * 拍照
     */
    @OnClick(R.id.img)
    public void toShot() {
        //自定义相机2
        Intent mCameraIntent = new Intent((MainActivity) getActivity(), MyChangeCameraActivity.class);
        startActivityForResult(mCameraIntent, REQUEST_CAMERA);
    }

    //拍照回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        updateAvatarUtil.onActivityResultAction(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQUEST_CAMERA) {
            Uri photoUri = data.getData();
            Log.d(TAG, "onActivityResult: photoUri==null:" + (photoUri == null));

            picPath = new File(ImageUtils.getImageAbsolutePath((MainActivity) getActivity(), photoUri));
            //            picPath = new File(photoUri.toString());
            // Get the bitmap in according to the width of the device

            picbitmap = ImageUtils.decodeSampledBitmapFromPath(photoUri.getPath(), mPoint.x, mPoint.x);//获取的图片
            img.setImageBitmap(picbitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

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
