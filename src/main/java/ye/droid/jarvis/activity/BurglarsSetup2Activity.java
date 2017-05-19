package ye.droid.jarvis.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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

public class BurglarsSetup2Activity extends AppCompatActivity {
    private final String TAG = BurglarsSetup2Activity.class.getSimpleName();
    private SettingItem st_bind_sim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burglars_setup2);
        initUI();
    }

    private void initUI() {
        st_bind_sim = (SettingItem) findViewById(R.id.st_bind_sim);
        //回显数据
        String sim_number = SharedPreferencesUtils.getString(this, ConstantValues.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)) { //Sim序列号未null，SIM卡未绑定
            st_bind_sim.setCheck(false);
        } else {
            st_bind_sim.setCheck(true);
        }
    }

    public void startNextPage(View view) {
        String sim_number = SharedPreferencesUtils.getString(this, ConstantValues.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)) {
            Log.i(TAG, "请绑定SIM卡序列号");
            Toast.makeText(this, "请绑定SIM卡序列号", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, BurglarsSetup3Activity.class);
        startActivity(intent);
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
     * 自定义控件点击事件
     *
     * @param view
     */
    public void bindSIM(View view) {
        boolean check = st_bind_sim.isCheck(); //false，未绑定
        if (!check) {
            //获取SIM卡序列号
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = manager.getSimSerialNumber();
            Toast.makeText(this, simSerialNumber, Toast.LENGTH_SHORT).show();
            //保存SIM序列号
            SharedPreferencesUtils.putString(this, ConstantValues.SIM_NUMBER, simSerialNumber);
            st_bind_sim.setCheck(true);
        } else {
            //删除SIM卡序列号
            SharedPreferencesUtils.removeAttr(this, ConstantValues.SIM_NUMBER);
            st_bind_sim.setCheck(false);
        }

    }
}
