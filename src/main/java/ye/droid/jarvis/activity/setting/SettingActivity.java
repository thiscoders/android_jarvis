package ye.droid.jarvis.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ye.droid.jarvis.R;
import ye.droid.jarvis.activity.PerfectActivity;
import ye.droid.jarvis.cvs.ChangeItem;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * Created by ye on 2017/5/9.
 */

public class SettingActivity extends PerfectActivity {
    private ChangeItem ct_auto_update;
    private ChangeItem ct_about_app;
    private ChangeItem ct_phone_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUI();
        initData();
    }

    private void initUI() {
        ct_auto_update = (ChangeItem) findViewById(R.id.ct_auto_update);
        ct_about_app = (ChangeItem) findViewById(R.id.ct_about_app);
        ct_phone_address = (ChangeItem) findViewById(R.id.ct_phone_address);
    }

    private void initData() {
        //1.还原自动更新状态
        boolean isUpdate = SharedPreferencesUtils.getBoolean(this, ConstantValues.AUTO_UPDATE, true);//自动更新默认开启
        ct_auto_update.setCheck(isUpdate);
        //2.隐藏相关控件的switch
        ct_phone_address.setSwitchVisible(false);
        ct_about_app.setSwitchVisible(false);
    }

    //自动更新触发事件
    public void setAutoUpdate(View view) {
        boolean isUpdate = SharedPreferencesUtils.getBoolean(this, ConstantValues.AUTO_UPDATE, true); //自动更新默认开启
        SharedPreferencesUtils.putBoolean(this, ConstantValues.AUTO_UPDATE, !isUpdate);
        ct_auto_update.setCheck(!isUpdate);
    }

    //来电归属地设置
    public void openPhoneAddress(View view) {
        Intent intent = new Intent(this, PhoneAddressActivity.class);
        startActivity(intent);
        nextAnim();
    }

    /**
     * 软件的详细信息
     *
     * @param view
     */
    public void showAboutInfo(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        nextAnim();//开启下一页动画
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preAnim();
    }
}
