package com.linzhi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linzhi.base.BaseActivity;
import com.linzhi.common.MyException;
import com.linzhi.dialog.Loading;
import com.linzhi.helper.UserHelper;
import com.linzhi.inject.ViewInject;
import com.linzhi.model.DetailModel;
import com.linzhi.utils.PageUtil;
import com.linzhi.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * 修改注册信息
 * Created by sjy on 2017/4/21.
 */

public class VipUpdateActivity extends BaseActivity {
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

    @ViewInject(id = R.id.btn_signin, click = "forChange")
    Button btn_signin;


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

    //常量
    private static final int VIP_UPDATE_SUCCESS = 10;
    private static final int VIP_UPDATE_FAILED = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_change);
        ButterKnife.bind(this);
        initMyView();
        initListener();
    }

    //
    private void initMyView() {
        tv_right.setText("");
        Bundle bundle = this.getIntent().getExtras();
        model = (DetailModel) bundle.getSerializable("DetailModel");
        setShow(model);
    }

    private void setShow(DetailModel model) {
        et_name.setText(model.getClientName());
        et_phone.setText(model.getClientPhone());
        et_cardid.setText(model.getIDCardNo());
        et_remark.setText(model.getRemark());
        Log.d("SJY", "setShow:radioBtn_level1==null: " + (radioBtn_level1 == null));
        //RadioGroup赋值1 钻石 -->5普通
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

        switch (model.getClientGender()) {
            case "1":

                radioBtn_male.setChecked(true);
                break;
            case "2":
                radioBtn_female.setChecked(true);
                break;
        }

    }


    /**
     * 客户等级
     */
    private void initListener() {

        radiogroup_level.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
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
        });
    }

    /**
     * 修改
     */
    public void forChange(View view) {
        getInput();//获取输入内容
        if(!Utils.isPhoneLegal(phone)){
            PageUtil.DisplayToast("请输入正确手机号");
            return;
        }
        if(!TextUtils.isEmpty(cardid)){

            if(!Utils.isIDCard(cardid)){
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
                        js.put("clientID", model.getClientID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("SJY", "getMessageList: js=" + js.toString());
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
                    VipUpdateActivity.this.finish();
                    break;
                case VIP_UPDATE_FAILED:
                    PageUtil.DisplayToast((String) msg.obj);
                    break;


            }
        }
    };

    private void getInput() {

        name = et_name.getText().toString();
        phone = et_phone.getText().toString();
        cardid = et_cardid.getText().toString();
        phone = et_phone.getText().toString();
        remark = et_remark.getText().toString();
        gender = radiogroup_gender.getCheckedRadioButtonId() == R.id.radioBtn_male ? "1" : "2";//性别
        Log.d("SJY", "getInput: 获取修改数值");
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
