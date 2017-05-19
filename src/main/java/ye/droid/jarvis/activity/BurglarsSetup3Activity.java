package ye.droid.jarvis.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.cvs.SettingItem;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

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
        initData();
    }


    private void initUI() {
        et_phone_num = (EditText) findViewById(R.id.et_phone_num);
        btn_select_contact = (Button) findViewById(R.id.btn_select_contact);
        et_phone_num.setFocusable(false);//禁用edittext
    }

    private void initData() {
        String name = SharedPreferencesUtils.getString(this, ConstantValues.CONTACT_NAME, "");
        String phonev2 = SharedPreferencesUtils.getString(this, ConstantValues.CONTACT_PHONEV2, "");
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phonev2)) {
            Log.i(TAG, "尚未选择联系人！");
            return;
        }
        Log.i(TAG, "联系人已经选择..." + name + "(" + phonev2 + ")");
        et_phone_num.setText(name + "(" + phonev2 + ")");
    }

    //选择联系人
    public void selectContacts(View view) {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivityForResult(intent, ConstantValues.BURGLARSSETUP3_ACTIVITY_SELECT_CONTACTS_REQUEST_CODE);
    }

    //重置联系人
    public void resetContacts(View view) {
        SharedPreferencesUtils.removeAttr(this, ConstantValues.CONTACT_NAME);
        SharedPreferencesUtils.removeAttr(this, ConstantValues.CONTACT_PHONEV2);
        et_phone_num.setText("");
        Toast.makeText(this, "联系人已经重置！", Toast.LENGTH_SHORT).show();
    }

    public void startNextPage(View view) {
        String name = SharedPreferencesUtils.getString(this, ConstantValues.CONTACT_NAME, "");
        String phonev2 = SharedPreferencesUtils.getString(this, ConstantValues.CONTACT_PHONEV2, "");
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phonev2)) {
            Toast.makeText(this, "请选择联系人！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, BurglarsSetup4Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    public void returnBeforePage(View view) {
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValues.BURGLARSSETUP3_ACTIVITY_SELECT_CONTACTS_REQUEST_CODE) {
            if (data == null) {
                Toast.makeText(this, "未选择联系人！", Toast.LENGTH_SHORT).show();
                return;
            }
            String contactName = data.getStringExtra(ConstantValues.CONTACTLIST_ACTIVITY_CONTACTS_NAME_FLAG);
            String contactPhoneV2 = data.getStringExtra(ConstantValues.CONTACTLIST_ACTIVITY_CONTACTS_PHONEV2_FLAG);
            Log.i(TAG, contactName + "..." + contactPhoneV2);
            SharedPreferencesUtils.putString(this, ConstantValues.CONTACT_NAME, contactName);
            SharedPreferencesUtils.putString(this, ConstantValues.CONTACT_PHONEV2, contactPhoneV2);
            et_phone_num.setText(contactName + "(" + contactPhoneV2 + ")");
        }
    }
}
