package ye.droid.jarvis.activity.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ye.droid.jarvis.R;
import ye.droid.jarvis.activity.PerfectActivity;

/**
 * 来电悬浮框位置选择
 * Created by ye on 2017/6/4.
 */
public class SuspendLocationActivity extends PerfectActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspendlocation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
