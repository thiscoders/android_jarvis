package ye.droid.jarvis.service;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import ye.droid.jarvis.beans.SmsBean;

/**
 * Created by ye on 2017/5/23.
 */

public class SmsHandler extends Handler {
    private final String TAG = SmsHandler.class.getSimpleName();
    private Context context;

    public SmsHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        SmsBean smsBean = (SmsBean) msg.obj;
        if (smsBean.getAction() == 1) {
            ContentValues values = new ContentValues();
            values.put("read", "1");
            this.context.getContentResolver().update(
                    Uri.parse("content://sms/inbox"), values, "thread_id=?",
                    new String[]{smsBean.getThread_id()});
        } else if (smsBean.getAction() == 2) {
            Uri mUri = Uri.parse("content://sms/");
            this.context.getContentResolver().delete(mUri, "_id=?",
                    new String[]{smsBean.get_id()});
        }
    }
}
