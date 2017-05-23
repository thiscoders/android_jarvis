package ye.droid.jarvis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ye.droid.jarvis.R;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * 手机防盗页面
 * Created by ye on 2017/5/11.
 */

public class BurglarsResultActivity extends AppCompatActivity {
    private final String TAG = BurglarsResultActivity.class.getSimpleName();
    private TextView tv_safe_phonenum;
    private TextView tv_safe_status;

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

    private void initUI() {
        tv_safe_phonenum = (TextView) findViewById(R.id.tv_safe_phonenum);
        tv_safe_status = (TextView) findViewById(R.id.tv_safe_status);
    }

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
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);//开启下一页动画
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //返回上一页动画
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
