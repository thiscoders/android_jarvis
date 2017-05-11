package ye.droid.jarvis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ye.droid.jarvis.R;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * 手机防盗页面
 * Created by ye on 2017/5/11.
 */

public class BurglarsResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean BURGLARS_SET_OVER = SharedPreferencesUtils.getBoolean(this, ConstantValues.BURGLARS_SET_OVER, false);
        if (BURGLARS_SET_OVER) { //手机防盗参数设置完成，直接显示防盗参数
            setContentView(R.layout.activity_burglars_result);
        } else { //手机防盗未设置，进入设置界面，并且关闭当前页面
            Intent intent = new Intent(this, BurglarsSetup1Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
