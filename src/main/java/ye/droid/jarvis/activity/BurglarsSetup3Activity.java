package ye.droid.jarvis.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.cvs.SettingItem;

/**
 * 手机防盗设置页面01
 * Created by ye on 2017/5/11.
 */

public class BurglarsSetup3Activity extends AppCompatActivity {
    private String TAG = BurglarsSetup3Activity.class.getSimpleName();

    private EditText et_phone_num;
    private Button btn_select_contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burglars_setup3);

        initUI();
    }

    private void initUI() {
        et_phone_num = (EditText) findViewById(R.id.et_phone_num);
        btn_select_contact = (Button) findViewById(R.id.btn_select_contact);
    }


    public void selectContact(View view) {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivityForResult(intent, 0);
    }

    public void startNextPage(View view) {
        Intent intent = new Intent(this, BurglarsSetup4Activity.class);
        startActivity(intent);
    }

    public void returnBeforePage(View view) {
        finish();
    }
}
