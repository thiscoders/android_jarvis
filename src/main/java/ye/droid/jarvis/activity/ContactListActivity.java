package ye.droid.jarvis.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ye.droid.jarvis.R;
import ye.droid.jarvis.beans.ContactBean;

/**
 * Created by ye on 2017/5/12.
 */

public class ContactListActivity extends AppCompatActivity {
    private final String TAG = ContactListActivity.class.getSimpleName();
    private ListView lv_contact_list;
    private ArrayList<ContactBean> arrayList = new ArrayList<ContactBean>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        initUI();
        initData();
    }

    private void initUI() {
        lv_contact_list = (ListView) findViewById(R.id.lv_contact_list);
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //1. 获取内容解析者
                ContentResolver resolver = getContentResolver();
                //按姓名进行排序
                Cursor cursor1
                        = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts"), null, null, null, ContactsContract.Contacts.SORT_KEY_PRIMARY);

                arrayList.clear();
                Cursor cursor2 = null;
                ContactBean contactBean;
                String last_id = "";
                String last_name = ""; //联系人去重
                boolean isName = false;
                //外循环
                while (cursor1.moveToNext()) {

                    String id = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.i(TAG, "                  ");
                    Log.i(TAG, "out..." + id + "..." + name + "-----------------------------------------------------");
                    //创建联系人Bean对象
                    contactBean = new ContactBean();
                    //开始赋值
                    contactBean.setContactID(id);
                    contactBean.setContactName(name);

                    cursor2 = resolver.query(Uri.parse("content://com.android.contacts/data"), new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);
                    if (name.equals(last_name)) {
                        continue;
                    }

                    last_name = name;
                    boolean is_phonev2 = false; //拦截第二次phone_v2，避免只出现短号
                    //内循环
                    while (cursor2.moveToNext()) {

                        String data1 = cursor2.getString(0);
                        if (data1 == null || data1.equals("null")) {
                            continue;
                        }
                        String mimetype = cursor2.getString(1);

                        data1 = (data1 != null) ? data1 : "00001111";
                        mimetype = (mimetype != null) ? mimetype : "mimetype";

                        switch (mimetype) {
                            case "vnd.android.cursor.item/vnd.com.tencent.mobileqq.voicecall.profile":
                                Log.i(TAG, "\t\t\t>>>profile=" + data1);
                                contactBean.setContactPhone(data1);
                                break;
                            case "vnd.android.cursor.item/name":
                                Log.i(TAG, "\t\t\t>>>name=" + data1);
                                contactBean.setContactName(data1);
                                isName = true;
                                last_name = data1;
                                break;
                            case "vnd.android.cursor.item/photo":
                                Log.i(TAG, "\t\t\t>>>photo=" + data1);
                                contactBean.setContactPhoto(data1);
                                break;
                            case "vnd.android.cursor.item/note":
                                Log.i(TAG, "\t\t\t>>>note=" + data1);
                                contactBean.setContactNote(data1);
                                break;
                            case "vnd.android.cursor.item/nickname":
                                Log.i(TAG, "\t\t\t>>>nickname=" + data1);
                                contactBean.setContactNickname(data1);
                                break;
                            case "vnd.com.google.cursor.item/contact_misc":
                                Log.i(TAG, "\t\t\t>>>contact_misc=" + data1);
                                contactBean.setContactMisc(data1);
                                break;
                            case "vnd.android.cursor.item/group_membership":
                                Log.i(TAG, "\t\t\t>>>group_membership=" + data1);
                                contactBean.setContactGroupMembership(data1);
                                break;
                            case "vnd.android.cursor.item/phone_v2":


                                if (is_phonev2) {
                                    is_phonev2 = false;
                                    continue;
                                }
                                Log.i(TAG, "\t\t\t>>>phone_v2=" + data1);
                                contactBean.setContactPhone_v2(data1);
                                is_phonev2 = true;
                                break;
                        }
                    }
                    cursor2.close();
                    arrayList.add(contactBean);
                    last_id = id;
                }
                cursor1.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv_contact_list.setAdapter(new ContactAdapter());
                    }
                });
            }
        }.start();

    }

    /**
     * 显示联系人的适配器
     */
    private class ContactAdapter extends BaseAdapter {
        private TextView tv_contact_name;
        private TextView tv_contact_phone;

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            LayoutInflater inflater;
            if (convertView == null) {
                inflater = (LayoutInflater) ContactListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_contact, null);
            } else {
                view = convertView;
            }

            tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
            tv_contact_phone = (TextView) view.findViewById(R.id.tv_contact_phone);

            ContactBean contactBean = arrayList.get(position);
            String contact_id = contactBean.getContactID();
            String name = contactBean.getContactName();
            String phone = contactBean.getContactPhone();
            String phone_v2 = contactBean.getContactPhone_v2();
            tv_contact_name.setText(name);
            tv_contact_phone.setText((phone_v2 != null) ? phone_v2 : phone);

            return view;
        }
    }
}
