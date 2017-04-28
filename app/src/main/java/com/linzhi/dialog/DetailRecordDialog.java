package com.linzhi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linzhi.R;
import com.linzhi.common.ImageLoadingConfig;
import com.linzhi.model.DetailModel;
import com.linzhi.utils.AppCommonUtils;
import com.linzhi.utils.DpUtils;
import com.linzhi.utils.WebUrl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by sjy on 2017/4/14.
 */

public class DetailRecordDialog extends AlertDialog {//AlertDialog和Dialog两种继承

    private final String TAG = "SJY";
    //变量

    private Context context;
    private DetailModel model;

    private ImageLoader imgLoader;
    private DisplayImageOptions imgOptions;

    private ClickListenerInterface clickListenerInterface;
    //控件
    private ImageView img;
    private TextView tv_name;
    private TextView tv_gender;
    private TextView tv_cardId;
    private TextView tv_phone;
    private TextView tv_level;
    private TextView tv_remark;

    private Button btn_sure;
    private Button btn_cancel;

    public interface ClickListenerInterface {
        public void forSure();

        public void forCancel();
    }

    public DetailRecordDialog(Context context, DetailModel model) {
        super(context);
        this.context = context;
        this.model = model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setValue();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_detail, null);
        setContentView(view);

        imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        //加载不出图片，显示默认的图片
        imgOptions = ImageLoadingConfig.generateDisplayImageOptions(R.mipmap.default_photo);

        //初始化
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        tv_cardId = (TextView) view.findViewById(R.id.tv_cardId);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_level = (TextView) view.findViewById(R.id.tv_level);
        tv_remark = (TextView) view.findViewById(R.id.tv_remark);
        img = (ImageView) view.findViewById(R.id.img);

        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        //
    }

    //显示
    public void setValue() {

        //参数
        tv_name.setText(model.getClientName());
        if ((model.getClientGender()).contains("1")) {
            tv_gender.setText("男");
        } else if ((model.getClientGender()).contains("2")) {
            tv_gender.setText("女");

        } else {
            tv_gender.setText(model.getClientGender());
        }

        String cardNum = model.getIDCardNo();
        tv_cardId.setText(TextUtils.isEmpty(cardNum) ? "无" : cardNum);
        tv_phone.setText(model.getClientPhone());
        tv_level.setText(AppCommonUtils.getTransLevel(model.getClientLevel()));
        tv_remark.setText(model.getRemark());
        String url = WebUrl.getURL() + model.getImgPath();
        Log.d(TAG, "setValue: 图片路径：" + url);
        imgLoader.displayImage(url, img, imgOptions);

        btn_cancel.setOnClickListener(new ClickListener());
        btn_sure.setOnClickListener(new ClickListener());

        //弹窗大小,代码设置
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        layoutParams.width = DpUtils.dp2px(context, 430); // 宽430dp
        layoutParams.height = DpUtils.dp2px(context, 525); // 高525dp
        dialogWindow.setAttributes(layoutParams);

    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_sure:
                    clickListenerInterface.forSure();
                    break;
                case R.id.btn_cancel:
                    clickListenerInterface.forCancel();
                    break;
            }
        }

    }

    ;
}
