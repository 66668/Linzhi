package com.linzhi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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

import com.linzhi.R;
import com.linzhi.base.BaseFragment;
import com.linzhi.common.MyException;
import com.linzhi.dialog.Loading;
import com.linzhi.helper.UserHelper;
import com.linzhi.utils.ImageUtils;
import com.linzhi.utils.PageUtil;
import com.linzhi.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


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
    private static final int REQUEST_CAMERA_1 = 21;

    public static final int REGIST_SUCCESS = 24;
    public static final int REGIST_FAILED = 25;

    //变量
    private String name;
    private String gender;
    private String phone;
    private String cardid;
    private String level;
    private String remark;
    private File picPath = null;
    private Point mPoint;//获取屏幕像素尺寸
    private Bitmap picbitmap;
    private String mFilePath;//图片指定路径


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
        final String base64 = ImageUtils.imgToBase64(null, picbitmap);
        if (isEmpty()) {
            return;
        }
        if (!Utils.isChinaPhoneLegal(phone)) {
            PageUtil.DisplayToast("请输入正确手机号！");
            return;
        }
        //身份证号可以不输入，或者输入正确的id
        if (!TextUtils.isEmpty(cardid)) {
            if (!Utils.isIDCard(cardid)) {
                PageUtil.DisplayToast("请输入正确身份证号！");
                return;
            }
        }
        if (base64 == null) {
            PageUtil.DisplayToast("请选择相机拍照");
            return;
        }
        Loading.run(getActivity(), new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject js = new JSONObject();
                    try {
                        js.put("image", base64);
                        js.put("remark", remark);
                        js.put("name", name);
                        js.put("gender", gender);
                        js.put("IDCardNo", cardid);
                        js.put("clientPhone", phone);
                        js.put("level", level);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "getMessageList: js=" + js.toString());
                    UserHelper.postVipRegist(getActivity(), js);
                    handler.sendMessage(handler.obtainMessage(REGIST_SUCCESS, "注册成功！"));
                } catch (MyException e) {
                    e.printStackTrace();
                    handler.sendMessage(handler.obtainMessage(REGIST_FAILED, e.getMessage()));
                }
            }
        });
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REGIST_SUCCESS://
                    PageUtil.DisplayToast((String) msg.obj);
                    clean();//清空注册数据
                    break;

                case REGIST_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;

            }
        }
    };

    //注册成功，清空
    private void clean() {
        et_name.setText("");
        et_phone.setText("");
        et_cardid.setText("");
        et_remark.setText("");
        img.setImageBitmap(null);
        img.setBackground(getResources().getDrawable(R.mipmap.vip_default_photo, null));
    }

    private void getInput() {
        name = et_name.getText().toString();
        phone = et_phone.getText().toString();
        cardid = et_cardid.getText().toString();
        phone = et_phone.getText().toString();
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
        return false;
    }

    /**
     * 拍照
     */
    @OnClick(R.id.img)
    public void toShot() {

        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + "/" + "temp.png";// 指定路径
        //系统相机调用
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        Uri photoUri = Uri.fromFile(new File(mFilePath)); // 传递路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径
        startActivityForResult(intent, REQUEST_CAMERA_1);
    }

    //拍照回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) { // 如果返回数据
            if (requestCode == REQUEST_CAMERA_1) {
                Log.d(TAG, "onActivityResult: 返回回调");
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFilePath); // 根据路径获取数据
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    if(bitmap!=null){
                        picbitmap = bitmap;
                        img.setBackground(null);
                        img.setImageBitmap(bitmap);// 显示图片
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();// 关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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
