package ye.droid.jarvis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.cvs.SettingItem;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * 手机防盗设置页面01
 * Created by ye on 2017/5/11.
 */

public class BurglarsSetup4Activity extends AppCompatActivity {
    private SettingItem st_open_burglars;
    private final String TAG = BurglarsSetup4Activity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burglars_setup4);

        initUI();
        initData();
    }

    private void initUI() {
        st_open_burglars = (SettingItem) findViewById(R.id.st_open_burglars);
    }


    private void initData() {
        boolean isopen = SharedPreferencesUtils.getBoolean(this, ConstantValues.OPEN_SECURE_FLAG, false);
        st_open_burglars.setCheck(isopen);
    }

    public void startNextPage(View view) {
        //开启防盗保护才能前往下一页
        boolean isopen = SharedPreferencesUtils.getBoolean(this, ConstantValues.OPEN_SECURE_FLAG, false);
        if (!isopen) {
            Toast.makeText(this, "请开启防盗保护！", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferencesUtils.putBoolean(this, ConstantValues.BURGLARS_SET_OVER, true);
        Intent intent = new Intent(this, BurglarsResultActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    public void returnBeforePage(View view) {
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    /**
     * 打开手机防盗
     *
     * @param view
     */
    public void openBurglars(View view) {
        boolean ischeck = st_open_burglars.isCheck();
        st_open_burglars.setCheck(!ischeck);
        if (ischeck) {
            SharedPreferencesUtils.putBoolean(this, ConstantValues.OPEN_SECURE_FLAG, false);
        } else {
            SharedPreferencesUtils.putBoolean(this, ConstantValues.OPEN_SECURE_FLAG, true);
        }
    }
}
