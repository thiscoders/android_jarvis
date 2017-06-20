package ye.droid.jarvis.activity.burglars;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.activity.PerfectActivity;
import ye.droid.jarvis.service.burglars.LocationChangeService;
import ye.droid.jarvis.service.burglars.SmsListenerService;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.ServiceUtils;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * 手机防盗页面
 * Created by ye on 2017/5/11.
 */

public class BurglarsResultActivity extends PerfectActivity {
    private final String TAG = BurglarsResultActivity.class.getSimpleName();
    private TextView tv_safe_phonenum;
    private TextView tv_safe_status;

    private TextView tv_sms_listener_status;
    private TextView tv_location_listener_status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean BURGLARS_SET_OVER = SharedPreferencesUtils.getBoolean(this, ConstantValues.BURGLARS_SET_OVER, false);
        if (BURGLARS_SET_OVER) { //手机防盗参数设置完成，直接显示防盗参数
            setContentView(R.layout.activity_burglars_result);
            initUI();
            initData();
        } else { //手机防盗未设置，进入设置界面，并且关闭当前页面
            Intent intent = new Intent(this, BurglarsSetup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    //初始化UI
    private void initUI() {
        tv_safe_phonenum = (TextView) findViewById(R.id.tv_safe_phonenum);
        tv_safe_status = (TextView) findViewById(R.id.tv_safe_status);

        tv_sms_listener_status = (TextView) findViewById(R.id.tv_sms_listener_status);
        tv_location_listener_status = (TextView) findViewById(R.id.tv_location_listener_status);
    }

    //初始化数据
    private void initData() {
        String contact_name = SharedPreferencesUtils.getString(this, ConstantValues.CONTACT_NAME, "");
        String contact_phonev2 = SharedPreferencesUtils.getString(this, ConstantValues.CONTACT_PHONEV2, "");
        boolean open_secure_flag = SharedPreferencesUtils.getBoolean(this, ConstantValues.OPEN_SECURE_FLAG, false);

        if (TextUtils.isEmpty(contact_name) || TextUtils.isEmpty(contact_phonev2)) {
            Log.i(TAG, "安全号码为空！");
            return;
        }
        if (!open_secure_flag) {
            Log.i(TAG, "手机防盗未开启！");
            return;
        }
        tv_safe_phonenum.setText("安全联系人：" + contact_name + "(" + contact_phonev2 + ")");
        tv_safe_status.setText("防盗保护状态：已开启");

        //判断对应服务的状态并且进行赋值
        boolean smsListenerServiceRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.burglars.SmsListenerService", false);
        boolean locationChangeServiceRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.burglars.LocationChangeService", false);

        if (smsListenerServiceRunning) {
            tv_sms_listener_status.setText(getString(R.string.burglars_result_sim_lintener_status) + "：已开启");
        } else {
            tv_sms_listener_status.setText(getString(R.string.burglars_result_sim_lintener_status) + "：未开启(点击开启)");
        }
        if (locationChangeServiceRunning) {
            tv_location_listener_status.setText(getString(R.string.burglars_result_location_lintener_status) + "：已开启");
        } else {
            tv_location_listener_status.setText(getString(R.string.burglars_result_location_lintener_status) + "：未开启(点击开启)");
        }

    }

    //重新进入导航界面
    public void resetBurglars(View view) {
        //删除设置完成标记
        SharedPreferencesUtils.removeAttr(this, ConstantValues.BURGLARS_SET_OVER);

        /*
        SharedPreferencesUtils.removeAttr(this, ConstantValues.BURGLARS_SET_OVER);
        SharedPreferencesUtils.removeAttr(this, ConstantValues.OPEN_SECURE_FLAG);
        SharedPreferencesUtils.removeAttr(this, ConstantValues.CONTACT_NAME);
        SharedPreferencesUtils.removeAttr(this, ConstantValues.CONTACT_PHONEV2);
        SharedPreferencesUtils.removeAttr(this, ConstantValues.SIM_NUMBER);
        */

        startActivity(new Intent(this, BurglarsResultActivity.class));
        finish();
        nextAnim(); //开启下一页动画
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //返回上一页动画
        preAnim();
    }
    /**
     * 启动报警铃声
     *
     * @param view
     */
    public void openSmsListenerService(View view) {
        //判断短信监听服务的状态并且进行赋值
        boolean smsListenerServiceRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.burglars.SmsListenerService", false);
        if (!smsListenerServiceRunning) {
            Intent intent = new Intent(this, SmsListenerService.class);
            startService(intent);
            tv_sms_listener_status.setText(getString(R.string.burglars_result_sim_lintener_status) + "：已开启");
        } else {
            Toast.makeText(this, getString(R.string.burglars_result_sim_lintener_status) + "服务已启动，无需再启动！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 启动位置监听
     *
     * @param view
     */
    public void openLocationListenerService(View view) {
        //位置改变发送位置信息服务的运行状态
        boolean locationChangeServiceRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.burglars.LocationChangeService", false);
        if (!locationChangeServiceRunning) {
            Intent intent = new Intent(this, LocationChangeService.class);
            startService(intent);
            tv_location_listener_status.setText(getString(R.string.burglars_result_location_lintener_status) + "：已开启");
        } else {
            Toast.makeText(this, getString(R.string.burglars_result_location_lintener_status) + "服务已启动，无需再启动！", Toast.LENGTH_SHORT).show();
        }
    }
}
