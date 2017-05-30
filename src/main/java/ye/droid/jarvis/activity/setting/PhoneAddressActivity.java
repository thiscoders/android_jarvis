package ye.droid.jarvis.activity.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.cvs.ChangeItem;
import ye.droid.jarvis.dbdao.PhoneNumAddressDao;
import ye.droid.jarvis.service.SuspendFrameService;
import ye.droid.jarvis.utils.ColorPickerDialog;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.ServiceUtils;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * Created by ye on 2017/5/30.
 */

public class PhoneAddressActivity extends AppCompatActivity {
    private String TAG = PhoneNumAddressDao.class.getSimpleName();
    private ChangeItem ct_suspend_visible;
    private ChangeItem ct_suspend_color;
    private ChangeItem ct_suspend_location;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_address_setting);
        initUI();
        initData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preAnim();
    }

    //继续窗口的时候仍然需要判断服务是否正在运行
    @Override
    protected void onResume() {
        super.onResume();
        //判断服务是否运行，并且为ct_suspend_visible赋值
        boolean isRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.SuspendFrameService", false);
        ct_suspend_visible.setCheck(isRunning);
    }

    private void initUI() {
        ct_suspend_visible = (ChangeItem) findViewById(R.id.ct_suspend_visible);
        ct_suspend_color = (ChangeItem) findViewById(R.id.ct_suspend_color);
        ct_suspend_location = (ChangeItem) findViewById(R.id.ct_suspend_location);
    }

    private void initData() {
        //关闭颜色和位置的switch显示
        ct_suspend_color.setSwitchVisible(false);
        ct_suspend_location.setSwitchVisible(false);
        //判断服务是否运行，并且为ct_suspend_visible赋值
        boolean isRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.SuspendFrameService", false);
        ct_suspend_visible.setCheck(isRunning);
        int color = SharedPreferencesUtils.getInteger(getApplicationContext(), ConstantValues.TOAST_PHONE_ADDRESS, 0);
        if (color != 0) {
            ct_suspend_color.setColor(color);
        }
    }

    //是否显示悬浮框
    public void openSuspend(View view) {
        boolean ischeck = ct_suspend_visible.isCheck();//是否开启悬浮框
        Intent intent = new Intent(this, SuspendFrameService.class);//悬浮框服务

        if (!ischeck) { //true，开启服务
            //开启服务前检查是否允许在别的应用上面出现，重点
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getApplicationContext())) {
                    Toast.makeText(PhoneAddressActivity.this, "请允许请用在其他应用上层显示！", Toast.LENGTH_SHORT).show();
                    Intent overlay = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(overlay);
                    return;
                }
            }
            startService(intent); //开启服务
        } else {
            stopService(intent); //停止服务
        }
        ct_suspend_visible.setCheck(!ischeck); //设置状态
    }

    //选择悬浮框颜色
    public void selectColor(View view) {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, ct_suspend_color.getColor(), "选择颜色", new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                ct_suspend_color.setColor(color);
                SharedPreferencesUtils.putInteger(PhoneAddressActivity.this, ConstantValues.TOAST_PHONE_ADDRESS, color);
            }
        });
        colorPickerDialog.show();
    }

    //选择悬浮框位置
    public void selectLocation(View view) {
        Toast.makeText(this, "悬浮框位置！", Toast.LENGTH_SHORT).show();
    }


    private void nextAnim() {
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    private void preAnim() {
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
