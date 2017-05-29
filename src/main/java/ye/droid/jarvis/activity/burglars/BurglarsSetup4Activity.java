package ye.droid.jarvis.activity.burglars;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.broadcast.receiver.JDeviceAdminReceiver;
import ye.droid.jarvis.cvs.ChangeItem;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * 手机防盗设置页面01
 * Created by ye on 2017/5/11.
 */

public class BurglarsSetup4Activity extends AppCompatActivity {
    private final String TAG = BurglarsSetup4Activity.class.getSimpleName();

    private ChangeItem st_open_burglars;
    private ChangeItem st_device_admin;

    private ComponentName mDeviceAdminSample;
    private DevicePolicyManager mDevicePolicyManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burglars_setup4);

        initUI();
        initData();
    }

    private void initUI() {
        st_open_burglars = (ChangeItem) findViewById(R.id.st_open_burglars);
        st_device_admin = (ChangeItem) findViewById(R.id.st_device_admin);
    }

    private void initData() {
        boolean isopen = SharedPreferencesUtils.getBoolean(this, ConstantValues.OPEN_SECURE_FLAG, false);
        st_open_burglars.setCheck(isopen);
        mDeviceAdminSample = new ComponentName(this, JDeviceAdminReceiver.class);
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (mDevicePolicyManager.isAdminActive(mDeviceAdminSample)) {
            st_device_admin.setCheck(true);
        } else {
            st_device_admin.setCheck(false);
        }
    }

    public void startNextPage(View view) {
        //开启防盗保护才能前往下一页
        boolean isopen = SharedPreferencesUtils.getBoolean(this, ConstantValues.OPEN_SECURE_FLAG, false);
        boolean isAdmin = mDevicePolicyManager.isAdminActive(mDeviceAdminSample);
        //有任何一个是false都不行
        if (!(isopen && isAdmin)) {
            Toast.makeText(this, "请开启上述两个选项！", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferencesUtils.putBoolean(this, ConstantValues.BURGLARS_SET_OVER, true);
        Intent intent = new Intent(this, BurglarsResultActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        finish();
    }

    public void returnBeforePage(View view) {
        backPre();
    }

    private void backPre() {
        Intent intent = new Intent(this, BurglarsSetup3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
        finish();
    }

    @Override
    public void onBackPressed() {
        backPre();
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


    /**
     * 激活设备管理器
     * @param view
     */
    public void openDeviceAdmin(View view) {
        if (mDevicePolicyManager.isAdminActive(mDeviceAdminSample)) {
            Toast.makeText(this, "管理员权限已经授予！", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, ConstantValues.JEXTRA_ADD_EXPLANATION);
            startActivityForResult(intent, ConstantValues.BURGLARS4_DEVICE_ADMIN_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValues.BURGLARS4_DEVICE_ADMIN_REQUEST_CODE) {
            if (mDevicePolicyManager.isAdminActive(mDeviceAdminSample)) {
                st_device_admin.setCheck(true);
            } else {
                st_device_admin.setCheck(false);
            }
        }
    }
}
