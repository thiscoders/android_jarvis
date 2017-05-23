package ye.droid.jarvis.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import ye.droid.jarvis.beans.SmsBean;

/**
 * Created by ye on 2017/5/23.
 */

public class SmsObserver extends ContentObserver {
    private final String TAG = SmsObserver.class.getSimpleName();
    private Context context;
    private ContentResolver resolver;
    private SmsHandler handler;

    private String last_uri="";

    public SmsObserver(Context context, ContentResolver resolver, SmsHandler handler) {
        super(handler);
        this.context = context;
        this.resolver = resolver;
        this.handler = handler;
    }


    /**
     * 回调函数, 当所监听的Uri发生改变时，就会回调此方法
     * 注意当收到短信的时候会回调两次
     *
     * @param selfChange 此值意义不大 一般情况下该回调值false
     * @param uri
     */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange);
        String uri_string = uri.toString();
        Log.i(TAG, uri_string + "..." + last_uri);

        if (last_uri.equals(uri_string)) {
            Log.i(TAG, "重复了！");
            return;
        }

        last_uri = uri_string;

        // 第一次回调 不是我们想要的 直接返回
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }

        // 第二次回调 查询收件箱里的内容
        Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"),
                new String[]{"_id", "address", "read", "body", "thread_id"},
                "read=?", new String[]{"0"}, "date desc");

        if (cursor == null) {
            return;
        } else {
            if (cursor.moveToFirst()) {
                SmsBean smsBean = new SmsBean();
                int id_Index = cursor.getColumnIndex("_id");

                if (id_Index != -1) {
                    smsBean.set_id(cursor.getString(id_Index));
                }

                int thread_idIndex = cursor.getColumnIndex("thread_id");
                if (thread_idIndex != -1) {
                    smsBean.setThread_id(cursor.getString(thread_idIndex));
                }

                int addressIndex = cursor.getColumnIndex("address");
                if (addressIndex != -1) {
                    smsBean.setSmsAddress(cursor.getString(addressIndex));
                }

                int bodyIndex = cursor.getColumnIndex("body");
                if (bodyIndex != -1) {
                    smsBean.setSmsBody(cursor.getString(bodyIndex));
                }

                int readIndex = cursor.getColumnIndex("read");
                if (readIndex != -1) {
                    smsBean.setRead(cursor.getString(readIndex));
                }
                Log.i(TAG, "Jarvis..." + smsBean.toString());
                Message message = Message.obtain();
                message.obj = smsBean;
                this.handler.handleMessage(message);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

}
