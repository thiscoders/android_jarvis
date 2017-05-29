package ye.droid.jarvis.activity.advancetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ye.droid.jarvis.R;
import ye.droid.jarvis.dbdao.PhoneNumAddressDao;

/**
 * 归属地查询
 * Created by ye on 2017/5/29.
 */

public class AttrLookupActivity extends AppCompatActivity {
    private final String TAG = AttrLookupActivity.class.getSimpleName();

    private String dbName = "location.db";

    private EditText et_attr;
    private TextView tv_attr_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attr_lookup);

        initUI();
        initData();
        initDB();
    }

    private void initUI() {
        et_attr = (EditText) findViewById(R.id.et_attr);
        tv_attr_result = (TextView) findViewById(R.id.tv_attr_result);
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
                String phone = et_attr.getText().toString();
                executeQuery(phone);
            }
        });
    }

    /**
     * 将数据库文件拷贝到项目file目录下
     */
    private void initDB() {
        File dir = getFilesDir();
        File dbFile = new File(dir, dbName);
        if (dbFile.exists()) {
            Log.i(TAG, "数据库文件已经存在！");
            return;
        }
        InputStream dbStream = null;
        FileOutputStream outputStream = null;
        try {
            dbStream = getResources().openRawResource(R.raw.location);

            outputStream = new FileOutputStream(dbFile);

            byte[] temp = new byte[1024];
            int len = -1;
            while ((len = dbStream.read(temp)) != -1) {
                outputStream.write(temp, 0, len);
            }
        } catch (IOException e) {
            Log.i(TAG, "lalala..." + e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (dbStream != null)
                    dbStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "归属地数据库拷贝完成！");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preAnim();
    }

    public void eueryPhone(View view) {
        String phone = et_attr.getText().toString();
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

    }

    private void nextAnim() {
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    private void preAnim() {
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
