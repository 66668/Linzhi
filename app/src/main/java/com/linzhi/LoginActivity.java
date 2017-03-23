package com.linzhi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.linzhi.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_netID)
    TextView tvtv_netID;

    @BindView(R.id.tv_userName)
    TextView tv_userName;

    @BindView(R.id.tv_psd)
    TextView tv_psd;

    @BindView(R.id.btn_login)
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        initView();

    }

    private void initView() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void Onclick(){
        startActivity(MainActivity.class);
    }
}

