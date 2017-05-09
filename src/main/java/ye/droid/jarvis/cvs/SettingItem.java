package ye.droid.jarvis.cvs;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import ye.droid.jarvis.R;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * Created by ye on 2017/5/9.
 */

public class SettingItem extends RelativeLayout {
    private final String TAG = SettingItem.class.getSimpleName();
    private final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private RelativeLayout rl_setting_item;
    private TextView tv_item_title;
    private TextView tv_item_subtitle;
    private Switch sw_item_check;

    private String title_content;
    private String subtitle_positive_content;
    private String subtitle_negative_content;

    public SettingItem(Context context) {
        this(context, null);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(getContext(), R.layout.item_setting, this);
        initUI(); //寻找控件
        initData(attrs); //初始化控件
        setUIText(); //设置控件上显示的提示文字
    }

    private void initUI() {
        rl_setting_item = (RelativeLayout) findViewById(R.id.rl_setting_item);
        tv_item_title = (TextView) findViewById(R.id.tv_item_title);
        tv_item_subtitle = (TextView) findViewById(R.id.tv_item_subtitle);
        sw_item_check = (Switch) findViewById(R.id.sw_item_check);
    }

    private void initData(AttributeSet attrs) {
        title_content = attrs.getAttributeValue(NAMESPACE, "title_content");
        subtitle_positive_content = attrs.getAttributeValue(NAMESPACE, "subtitle_positive_content");
        subtitle_negative_content = attrs.getAttributeValue(NAMESPACE, "subtitle_negative_content");
    }

    private void setUIText() {
        Log.i(TAG, "..." + title_content + "---" + subtitle_positive_content + "---" + subtitle_negative_content);
        tv_item_title.setText(title_content);
        tv_item_subtitle.setText(subtitle_positive_content);
    }

    /**
     * 获取switch的选中状态
     *
     * @return
     */
    public boolean isCheck() {
        return sw_item_check.isChecked();
    }

    /**
     * 设置自动更新状态
     *
     * @param checked
     */
    public void setCheck(boolean checked) {
        sw_item_check.setChecked(checked);
        if (checked) {
            SharedPreferencesUtils.putBoolean(SettingItem.this.getContext(), ConstantValues.AUTO_UPDATE, true);
            tv_item_subtitle.setText(subtitle_positive_content);
        } else {//ture的时候开启自动更新
            SharedPreferencesUtils.putBoolean(SettingItem.this.getContext(), ConstantValues.AUTO_UPDATE, false);
            tv_item_subtitle.setText(subtitle_negative_content);
        }
    }

    public void setSwitchVisible(boolean visible) {
        if (visible)
            sw_item_check.setVisibility(VISIBLE);
        else
            sw_item_check.setVisibility(INVISIBLE);
    }
}
