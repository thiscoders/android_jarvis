package ye.droid.jarvis.activity.burglars;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ye.droid.jarvis.R;

/**
 * 手机防盗设置页面01
 * Created by ye on 2017/5/11.
 */

public class BurglarsSetup1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burglars_setup1);
    }

    public void startNextPage(View view) {
        Intent intent = new Intent(this, BurglarsSetup2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);//开启下一页动画
        //立刻关闭前一个
        finish();
    }

    public void returnBeforePage(View view) {
        backPre();
    }

    private void backPre() {
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    public void onBackPressed() {
        backPre();
    }
}