package ye.droid.jarvis.activity.advancetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ye.droid.jarvis.R;

/**
 * Created by ye on 2017/5/29.
 */

public class AdvanceToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancetools);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    /**
     * 归属地查询
     *
     * @param view
     */
    public void attributionLookup(View view) {

    }

    /**
     * 短信备份
     *
     * @param view
     */
    public void backupSms(View view) {

    }


    /**
     * 常用号码查询
     *
     * @param view
     */
    public void phonenumLookup(View view) {

    }

    /**
     * 程序锁
     *
     * @param view
     */
    public void appLock(View view) {

    }


    /**
     * CPU信息
     *
     * @param view
     */
    public void cpuinfo(View view) {

    }

    /**
     * 网络测速
     *
     * @param view
     */
    public void netSpeed(View view) {

    }

}
