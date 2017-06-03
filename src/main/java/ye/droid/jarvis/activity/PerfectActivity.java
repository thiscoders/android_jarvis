package ye.droid.jarvis.activity;

import android.support.v7.app.AppCompatActivity;

import ye.droid.jarvis.R;

/**
 * Created by ye on 2017/6/4.
 */

public class PerfectActivity extends AppCompatActivity {

    public void nextAnim() {
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    public void preAnim() {
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
