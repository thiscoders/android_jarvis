package ye.droid.jarvis.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ye.droid.jarvis.R;

public class AboutActivity extends AppCompatActivity {
    private final String TAG = AboutActivity.class.getSimpleName();

    private TextView tv_splash_version_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initUI(); //初始化UI
        initData(); //初始化数据
    }
    //https://github.com/thiscoders/Android_Jarvis
    private void initUI() {
        tv_splash_version_name = (TextView) findViewById(R.id.tv_about_version_name);
    }

    private void initData() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
