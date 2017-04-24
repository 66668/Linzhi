package com.linzhi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linzhi.R;
import com.linzhi.common.ImageLoadingConfig;
import com.linzhi.model.DetailModel;
import com.linzhi.utils.WebUrl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by sjy on 2017/4/14.
 */

public class DetailModelDialog extends Dialog {

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

    public DetailModelDialog(Context context, DetailModel model) {
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

    public void setValue() {
        tv_name.setText(model.getClientName());
        tv_gender.setText(model.getClientGender());
        tv_cardId.setText(model.getIDCardNo());
        tv_phone.setText(model.getClientPhone());
        tv_level.setText(model.getClientLevel());
        tv_remark.setText(model.getRemark());

        String url = WebUrl.getURL() + model.getImgPath();
        Log.d(TAG, "setValue: 图片路径：" + url);
        imgLoader.displayImage(url, img, imgOptions);

        btn_cancel.setOnClickListener(new ClickListener());
        btn_sure.setOnClickListener(new ClickListener());

        //弹窗大小
        //        Window dialogWindow = getWindow();
        //        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        //        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        //        dialogWindow.setAttributes(lp);

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
