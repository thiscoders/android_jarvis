package ye.droid.jarvis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.cvs.SettingItem;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * Created by ye on 2017/5/9.
 */

public class SettingActivity extends AppCompatActivity {
    private SettingItem st_auto_update;
    private SettingItem st_about_app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUI();
        initData();
    }

    private void initUI() {
        st_auto_update = (SettingItem) findViewById(R.id.st_auto_update);
        st_about_app = (SettingItem) findViewById(R.id.st_about_app);
    }

    private void initData() {
        //1.还原自动更新状态
        boolean isUpdate = SharedPreferencesUtils.getBoolean(this, ConstantValues.AUTO_UPDATE, true);//自动更新默认开启
        st_auto_update.setCheck(isUpdate);
        //2.隐藏关于软件控件的switch
        st_about_app.setSwitchVisible(false);
    }

    /**
     * 自动更新触发事件
     *
     * @param view
     */
    public void setAutoUpdate(View view) {
        boolean isUpdate = SharedPreferencesUtils.getBoolean(this, ConstantValues.AUTO_UPDATE, true); //自动更新默认开启
        SharedPreferencesUtils.putBoolean(this, ConstantValues.AUTO_UPDATE, !isUpdate);
        st_auto_update.setCheck(!isUpdate);
    }

    /**
     * 软件的详细信息
     *
     * @param view
     */
    public void showAboutInfo(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);//开启下一页动画
    }

}
