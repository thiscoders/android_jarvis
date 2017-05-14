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
                        = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"contact_id"}, null, null, "sort_key");

                Cursor cursor2 = null;
                arrayList.clear();
                ContactBean contactBean;
                String last_id = null;
                while (cursor1.moveToNext()) {
                    contactBean = new ContactBean();
                    String id = cursor1.getString(0);
                    //不重复添加
                    if (id == null || TextUtils.isEmpty(id) || id.equals(last_id))
                        continue;

                    contactBean.setContactID(id);
                    cursor2 = resolver.query(Uri.parse("content://com.android.contacts/data"), new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);
                    while (cursor2.moveToNext()) {
                        String data1 = cursor2.getString(0);
                        if (data1 == null || data1.equals("null"))
                            continue;
                        String mimetype = cursor2.getString(1);
                        data1 = (data1 != null) ? data1 : "data1";
                        mimetype = (mimetype != null) ? mimetype : "mimetype";
                        switch (mimetype) {
                            case "vnd.android.cursor.item/vnd.com.tencent.mobileqq.voicecall.profile":
                                contactBean.setContactPhone(data1);
                                break;
                            case "vnd.android.cursor.item/name":
                                contactBean.setContactName(data1);
                                break;
                            case "vnd.android.cursor.item/photo":
                                contactBean.setContactPhoto(data1);
                                break;
                            case "vnd.android.cursor.item/note":
                                contactBean.setContactNote(data1);
                                break;
                            case "vnd.android.cursor.item/nickname":
                                contactBean.setContactNickname(data1);
                                break;
                            case "vnd.com.google.cursor.item/contact_misc":
                                contactBean.setContactMisc(data1);
                                break;
                            case "vnd.android.cursor.item/group_membership":
                                contactBean.setContactGroupMembership(data1);
                                break;
                            case "vnd.android.cursor.item/phone_v2":
                                contactBean.setContactPhone_v2(data1);
                                break;
                        }
                    }
                    cursor2.close();
                    Log.i(TAG, contactBean.toString());
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
            tv_contact_name.setText(name + "..." + contact_id);
            tv_contact_phone.setText((phone_v2 != null) ? phone_v2 : phone);

            return view;
        }
    }
}
