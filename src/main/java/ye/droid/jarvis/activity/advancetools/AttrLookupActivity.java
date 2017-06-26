package ye.droid.jarvis.activity.advancetools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ye.droid.jarvis.R;
import ye.droid.jarvis.activity.PerfectActivity;
import ye.droid.jarvis.activity.autils.ContactListActivity;
import ye.droid.jarvis.dbdao.PhoneNumAddressDao;
import ye.droid.jarvis.utils.ConstantValues;

/**
 * 归属地查询
 * Created by ye on 2017/5/29.
 */

public class AttrLookupActivity extends PerfectActivity {
    private final String TAG = AttrLookupActivity.class.getSimpleName();

    private EditText et_attr;
    private TextView tv_attr_result;
    private TextView tv_contact_attrname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attr_lookup);

        initUI();
        initData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preAnim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValues.ATTRLOOKUP_ACTIVITY_SELECT_CONTACTS_REQUEST_CODE) {
            if (data == null) {
                Toast.makeText(this, "未选择联系人！", Toast.LENGTH_SHORT).show();
                return;
            }
            String contactName = data.getStringExtra(ConstantValues.CONTACTLIST_ACTIVITY_CONTACTS_NAME_FLAG);
            String contactPhoneV2 = data.getStringExtra(ConstantValues.CONTACTLIST_ACTIVITY_CONTACTS_PHONEV2_FLAG);
            et_attr.setText(contactPhoneV2);
            tv_contact_attrname.setText(contactName);
            executeQuery(contactPhoneV2);
        }
    }

    private void initUI() {
        et_attr = (EditText) findViewById(R.id.et_attr);
        tv_attr_result = (TextView) findViewById(R.id.tv_attr_result);
        tv_contact_attrname = (TextView) findViewById(R.id.tv_contact_attrname);
    }

    private void initData() {
        et_attr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_contact_attrname.setText("");
                String phone = et_attr.getText().toString();
                executeQuery(phone);
            }
        });
    }


    public void queryPhone(View view) {
        String phone = et_attr.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            //抖动
            //Toast.makeText(this, "请输入电话号码！", Toast.LENGTH_SHORT).show();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.jshake);
            et_attr.startAnimation(animation);
            //添加手机震动
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);//传递两个参数可以规律震动
            return;
        }
        executeQuery(phone);
    }

    private void executeQuery(final String phone) {
        new Thread() {
            @Override
            public void run() {
                final List<String> results = PhoneNumAddressDao.getAddress(phone);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (results == null) {
                            tv_attr_result.setText("电话号码格式错误！");
                            return;
                        }
                        if (results.size() == 0) {
                            tv_attr_result.setText("查无此号！");
                            return;
                        }

                        tv_attr_result.setText("");
                        for (String result : results) {
                            tv_attr_result.append(result + "\r\n");
                        }
                    }
                });
            }
        }.start();
    }

    public void choiceContact(View view) {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivityForResult(intent, ConstantValues.ATTRLOOKUP_ACTIVITY_SELECT_CONTACTS_REQUEST_CODE);
    }
}
