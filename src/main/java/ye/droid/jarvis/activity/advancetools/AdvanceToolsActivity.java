package ye.droid.jarvis.activity.advancetools;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ye.droid.jarvis.R;
import ye.droid.jarvis.activity.PerfectActivity;

/**
 * Created by ye on 2017/5/29.
 */

public class AdvanceToolsActivity extends PerfectActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancetools);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preAnim();
    }

    /**
     * 归属地查询
     *
     * @param view
     */
    public void attributionLookup(View view) {
        startActivity(new Intent(this, AttrLookupActivity.class));
        nextAnim();
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
