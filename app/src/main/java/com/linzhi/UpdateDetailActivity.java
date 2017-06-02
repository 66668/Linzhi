package com.linzhi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linzhi.base.BaseActivity;
import com.linzhi.common.ImageLoadingConfig;
import com.linzhi.common.MyException;
import com.linzhi.dialog.Loading;
import com.linzhi.helper.UserHelper;
import com.linzhi.inject.ViewInject;
import com.linzhi.model.DetailModel;
import com.linzhi.utils.ImageUtils;
import com.linzhi.utils.PageUtil;
import com.linzhi.utils.Utils;
import com.linzhi.utils.WebUrl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.ButterKnife;

/**
 * 修改注册信息
 * Created by sjy on 2017/4/21.
 */

public class UpdateDetailActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;


    @ViewInject(id = R.id.et_name)
    EditText et_name;

    @ViewInject(id = R.id.et_phone)
    EditText et_phone;

    @ViewInject(id = R.id.et_cardid)
    EditText et_cardid;

    @ViewInject(id = R.id.et_remark)
    EditText et_remark;

    @ViewInject(id = R.id.et_carNum)
    EditText et_carNum;

    @ViewInject(id = R.id.btn_signin, click = "forChange")
    Button btn_signin;

    //图片清空按钮
    @ViewInject(id = R.id.btn_clearimg, click = "forCleanNewImg")
    Button btn_clearimg;

    //添加图片
    @ViewInject(id = R.id.img_add, click = "addNewPic")
    ImageView img_add;

    //原始图片
    @ViewInject(id = R.id.img)
    ImageView img;


    //等级
    @ViewInject(id = R.id.radiogroup_level)
    RadioGroup radiogroup_level;

    @ViewInject(id = R.id.radioBtn_level1)
    RadioButton radioBtn_level1;

    @ViewInject(id = R.id.radioBtn_level2)
    RadioButton radioBtn_level2;

    @ViewInject(id = R.id.radioBtn_level3)
    RadioButton radioBtn_level3;

    @ViewInject(id = R.id.radioBtn_level4)
    RadioButton radioBtn_level4;

    @ViewInject(id = R.id.radioBtn_level5)
    RadioButton radioBtn_level5;

    //性别 1男   2女
    @ViewInject(id = R.id.radiogroup_gender)
    RadioGroup radiogroup_gender;

    @ViewInject(id = R.id.radioBtn_male)
    RadioButton radioBtn_male;

    @ViewInject(id = R.id.radioBtn_female)
    RadioButton radioBtn_female;

    //变量
    private DetailModel model;
    private String name;
    private String gender;
    private String phone;
    private String cardid;
    private String level;
    private String remark;
    private String carNum;
    private Bitmap picbitmap;
    private String mFilePath;//图片指定路径

    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;

    private boolean isNameChanged = false;//根据该参数判断按钮是否需要修改
    private boolean isCarNumChanged = false;//根据该参数判断按钮是否需要修改
    private boolean isPhoneChanged = false;//根据该参数判断按钮是否需要修改
    private boolean isRemarkChanged = false;//根据该参数判断按钮是否需要修改
    private boolean isGenderChanged = false;//根据该参数判断按钮是否需要修改
    private boolean isLevelChanged = false;//根据该参数判断按钮是否需要修改
    private boolean isPicChanged = false;//根据该参数判断按钮是否需要修改
    private boolean isCardidChanged = false;//根据该参数判断按钮是否需要修改
    //常量
    private static final int VIP_UPDATE_SUCCESS = 10;
    private static final int VIP_UPDATE_FAILED = 11;
    private static final int REQUEST_CAMERA_1 = 22;
    private static final String TAG = "UpdateDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.act_detail_change);
        ButterKnife.bind(this);
        initMyView();
        initListener();
    }

    //
    private void initMyView() {
        tv_title.setText("VIP详情");
        tv_right.setText("");

        //图片加载设置
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(this));
        //加载不出图片，显示默认的图片
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.vip_default_photo);
        //获取跳转值
        Bundle bundle = this.getIntent().getExtras();
        model = (DetailModel) bundle.getSerializable("DetailModel");
        setShow(model);

        //清空 取消图片 按钮不可使用
        btnFalse();

    }

    //
    private void btnFalse() {
        cleanImgBtnFalse();
        updateBtnFalse();
    }

    //取消图片 按钮 不可使用
    private void cleanImgBtnFalse() {
        btn_clearimg.setClickable(false);
        btn_clearimg.setBackground(getResources().getDrawable(R.drawable.btn_checkfalse_style, null));//按钮变灰
    }

    //修改按钮 不可用
    private void updateBtnFalse() {
        btn_signin.setClickable(false);
        btn_signin.setBackground(getResources().getDrawable(R.drawable.btn_checkfalse_style, null));//按钮变灰
    }


    //取消添加按钮 可使用
    private void cleanImgBtnTrue() {
        btn_clearimg.setClickable(true);
        btn_clearimg.setBackground(getResources().getDrawable(R.drawable.btn_signin_style, null));//按钮变回
    }

    //修改按钮 可使用
    private void updateBtnTrue() {
        btn_signin.setClickable(true);
        btn_signin.setBackground(getResources().getDrawable(R.drawable.btn_signin_style, null));//按钮变回
    }

    //显示详情信息
    private void setShow(DetailModel model) {

        //显示文本
        et_name.setText(model.getClientName());
        et_phone.setText(model.getClientPhone());
        et_cardid.setText(model.getIDCardNo());
        et_remark.setText(model.getRemark());
        et_carNum.setText(model.getCarNumList().get(0).getCarNum());

        //显示等级 RadioGroup赋值1 钻石 -->5普通
        switch (model.getClientLevel()) {
            case "1":
                level = "1";
                radioBtn_level1.setChecked(true);
                break;
            case "2":
                level = "2";
                radioBtn_level2.setChecked(true);
                break;
            case "3":
                level = "3";
                radioBtn_level3.setChecked(true);
                break;
            case "4":
                level = "4";
                radioBtn_level4.setChecked(true);
                break;
            case "5":
                level = "5";
                radioBtn_level5.setChecked(true);
                break;
        }

        //显示性别
        switch (model.getClientGender()) {
            case "1":
                radioBtn_male.setChecked(true);
                break;
            case "2":
                radioBtn_female.setChecked(true);
                break;
        }

        //显示图片
        String url = WebUrl.getURL() + model.getImgPath();
        Log.d(TAG, "setValue: 图片路径：" + url);
        imgLoader.displayImage(url, img, imgOptions);


    }


    //根据参数确定按钮变色
    private void changeUpdateBtn() {
        if (isNameChanged || isCarNumChanged || isGenderChanged || isLevelChanged || isPhoneChanged || isCardidChanged || isPicChanged || isRemarkChanged) {
            updateBtnTrue();
        } else {
            updateBtnFalse();
        }
    }

    /**
     * 添加图片
     */
    public void addNewPic(View view) {
        //
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + "/" + "newpic.png";// 指定路径
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
                    if (bitmap != null) {
                        picbitmap = bitmap;
                        img_add.setBackground(null);
                        img_add.setImageBitmap(bitmap);// 显示图片
                        cleanImgBtnTrue();//按钮可用
                        //
                        isPicChanged = true;
                        changeUpdateBtn();
                    } else {

                        cleanImgBtnFalse();//按钮不可用
                        //
                        isPicChanged = true;
                        changeUpdateBtn();

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

    /**
     * 修改按钮
     */
    public void forChange(View view) {
        getInput();//获取输入内容
        final String base64 = ImageUtils.imgToBase64(null, picbitmap);
        if (!Utils.isPhoneLegal(phone)) {
            PageUtil.DisplayToast("请输入正确手机号");
            return;
        }
        if (!TextUtils.isEmpty(cardid)) {

            if (!Utils.isIDCard(cardid)) {
                PageUtil.DisplayToast("请输入正确身份证号");
                return;
            }
        }
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject js = new JSONObject();
                    try {
                        js.put("remark", remark);
                        js.put("name", name);
                        js.put("gender", gender);
                        js.put("IDCardNo", cardid);
                        js.put("clientPhone", phone);
                        js.put("level", level);
                        js.put("image", base64);
                        js.put("IsNewImage", isPicChanged);
                        js.put("carNum", carNum);
                        js.put("IsNewCarNum", isCarNumChanged);
                        js.put("clientID", model.getClientID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "getMessageList: js=" + js.toString());
                    UserHelper.postChangeVip(getApplicationContext(), js);
                    handler.sendMessage(handler.obtainMessage(VIP_UPDATE_SUCCESS, "修改成功！"));

                } catch (MyException e) {
                    e.printStackTrace();
                    Log.d("SJY", "run: error=" + e.toString());
                    handler.sendMessage(handler.obtainMessage(VIP_UPDATE_FAILED, e.getMessage()));
                }
            }
        });
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case VIP_UPDATE_SUCCESS:
                    PageUtil.DisplayToast((String) msg.obj);
                    UpdateDetailActivity.this.finish();
                    break;
                case VIP_UPDATE_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;
            }
        }
    };

    private void getInput() {
        carNum = et_carNum.getText().toString();
        Log.d(TAG, "getInput: carNum" + carNum);
        name = et_name.getText().toString();
        phone = et_phone.getText().toString();
        cardid = et_cardid.getText().toString();
        phone = et_phone.getText().toString();
        remark = et_remark.getText().toString();
        gender = radiogroup_gender.getCheckedRadioButtonId() == R.id.radioBtn_male ? "1" : "2";//性别
        Log.d("SJY", "getInput: 获取修改数值");
    }

    /**
     * 监听
     */
    private void initListener() {

        //图片清空按钮
        btn_clearimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_add.setImageBitmap(null);
                img_add.setBackground(getResources().getDrawable(R.mipmap.vip_default_photo));
                picbitmap = null;
                cleanImgBtnFalse();//按钮变色
                //
                isPicChanged = false;
                changeUpdateBtn();
            }
        });

        //姓名监听 是否修改
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString().trim()).equals(model.getClientName())) {
                    isNameChanged = false;
                } else {
                    isNameChanged = true;
                }
                changeUpdateBtn();
            }
        });

        //检测电话
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString().trim()).equals(model.getClientPhone())) {
                    isPhoneChanged = false;
                } else {
                    isPhoneChanged = true;
                }
                changeUpdateBtn();
            }
        });

        //监测车牌
        et_carNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString().trim()).equals(model.getCarNumList().get(0).getCarNum())) {
                    isCarNumChanged = false;
                } else {
                    isCarNumChanged = true;
                }
                changeUpdateBtn();
            }
        });

        //监测身份证
        et_cardid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString().trim()).equals(model.getIDCardNo())) {
                    isCardidChanged = false;
                } else {
                    isCardidChanged = true;
                }
                changeUpdateBtn();
            }
        });

        //监测备注信息
        et_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString().trim()).equals(model.getRemark())) {
                    isRemarkChanged = false;
                } else {
                    isRemarkChanged = true;
                }
                changeUpdateBtn();
            }
        });

        //性别监听
        radiogroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtn_female:
                        gender = "2";
                        if (gender.equals(model.getClientGender())) {
                            isGenderChanged = false;
                        } else {
                            isGenderChanged = true;
                        }
                        break;
                    case R.id.radioBtn_male:
                        gender = "1";
                        if (gender.equals(model.getClientGender())) {
                            isGenderChanged = false;
                        } else {
                            isGenderChanged = true;
                        }
                        break;
                }
                changeUpdateBtn();
            }
        });

        //会员等级监听
        radiogroup_level.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtn_level1:
                        level = "1";
                        if (level.equals(model.getClientLevel())) {
                            isLevelChanged = false;
                        } else {
                            isLevelChanged = true;
                        }
                        break;
                    case R.id.radioBtn_level2:
                        level = "2";
                        if (level.equals(model.getClientLevel())) {
                            isLevelChanged = false;
                        } else {
                            isLevelChanged = true;
                        }
                        break;
                    case R.id.radioBtn_level3:
                        level = "3";
                        if (level.equals(model.getClientLevel())) {
                            isLevelChanged = false;
                        } else {
                            isLevelChanged = true;
                        }
                        break;
                    case R.id.radioBtn_level4:
                        level = "4";
                        if (level.equals(model.getClientLevel())) {
                            isLevelChanged = false;
                        } else {
                            isLevelChanged = true;
                        }
                        break;
                    case R.id.radioBtn_level5:
                        level = "5";
                        if (level.equals(model.getClientLevel())) {
                            isLevelChanged = false;
                        } else {
                            isLevelChanged = true;
                        }
                        break;
                }
                changeUpdateBtn();
            }
        });
    }

    /**
     * 退出
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }


}
