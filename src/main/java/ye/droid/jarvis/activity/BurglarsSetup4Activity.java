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

        st_open_burglars = (SettingItem) findViewById(R.id.st_open_burglars);

        boolean res = SharedPreferencesUtils.getBoolean(this, ConstantValues.BURGLARS_SET_OVER, false);
        st_open_burglars.setCheck(res);
    }

    public void startNextPage(View view) {
        SharedPreferencesUtils.putBoolean(this, ConstantValues.BURGLARS_SET_OVER, true);
        Intent intent = new Intent(this, BurglarsResultActivity.class);
        startActivity(intent);
        finish();
    }

    public void returnBeforePage(View view) {
        finish();
    }

    /**
     * 打开手机防盗
     *
     * @param view
     */
    public void openBurglars(View view) {
        Toast.makeText(this, "手机防盗", Toast.LENGTH_SHORT).show();
    }
}
