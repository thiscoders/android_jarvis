package ye.droid.jarvis.activity.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ye.droid.jarvis.R;

/**
 * 来电悬浮框位置选择
 * Created by ye on 2017/6/4.
 */
// TODO: 2017/6/4 为了半透明窗体效果，暂时继承Activity
public class AddressLocationActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresslocation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
