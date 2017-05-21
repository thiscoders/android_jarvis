package ye.droid.jarvis.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ye.droid.jarvis.R;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = SplashActivity.class.getSimpleName();


    private RelativeLayout rl_up_screen;
    private TextView tv_splash_version_name;
    private TextView tv_server_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initUI(); //初始化UI
    }

    /**
     * 初始化UI，寻找控件
     */
    private void initUI() {
        rl_up_screen = (RelativeLayout) findViewById(R.id.rl_up_screen);
        tv_splash_version_name = (TextView) findViewById(R.id.tv_splash_version_name);
        tv_server_message = (TextView) findViewById(R.id.tv_server_message);
    }
}
