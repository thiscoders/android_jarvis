package ye.droid.jarvis.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.cvs.SettingItem;

/**
 * 手机防盗设置页面01
 * Created by ye on 2017/5/11.
 */

public class BurglarsSetup2Activity extends AppCompatActivity {
    private SettingItem st_bind_sim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burglars_setup2);
        st_bind_sim = (SettingItem) findViewById(R.id.st_bind_sim);
    }

    public void startNextPage(View view) {
        Toast.makeText(this, "开启下一个", Toast.LENGTH_SHORT).show();
    }

    public void returnBeforePage(View view) {
        finish();
    }

    /**
     * 自定义控件点击事件
     * @param view
     */
    public void bindSIM(View view) {
        boolean is = st_bind_sim.isCheck();
        st_bind_sim.setCheck(!is);
    }

}
