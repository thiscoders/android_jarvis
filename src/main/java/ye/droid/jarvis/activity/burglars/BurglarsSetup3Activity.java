package ye.droid.jarvis.activity.burglars;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.activity.PerfectActivity;
import ye.droid.jarvis.activity.autils.ContactListActivity;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * 手机防盗设置页面01
 * Created by ye on 2017/5/11.
 */

public class BurglarsSetup3Activity extends PerfectActivity {

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
        nextAnim();
        finish();
    }

    public void returnBeforePage(View view) {
        backPre();
    }

    private void backPre() {
        Intent intent = new Intent(this, BurglarsSetup2Activity.class);
        startActivity(intent);
        preAnim();
        finish();
    }

    @Override
    public void onBackPressed() {
        backPre();
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
