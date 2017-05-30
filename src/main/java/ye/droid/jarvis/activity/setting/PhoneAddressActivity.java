package ye.droid.jarvis.activity.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ye.droid.jarvis.R;
import ye.droid.jarvis.cvs.ChangeItem;

/**
 * Created by ye on 2017/5/30.
 */

public class PhoneAddressActivity extends AppCompatActivity {
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

    private void initUI() {
        ct_suspend_visible = (ChangeItem) findViewById(R.id.ct_suspend_visible);
        ct_suspend_color = (ChangeItem) findViewById(R.id.ct_suspend_color);
        ct_suspend_location = (ChangeItem) findViewById(R.id.ct_suspend_location);
    }

    private void initData() {
        //关闭颜色和位置的switch显示
        ct_suspend_color.setSwitchVisible(false);
        ct_suspend_location.setSwitchVisible(false);
    }


    public void openSuspend(View view) {
        
    }

    public void selectColor(View view) {
        ct_suspend_color.setColor(Color.BLUE);
    }

    public void selectLocation(View view) {

    }


    private void nextAnim() {
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    private void preAnim() {
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
