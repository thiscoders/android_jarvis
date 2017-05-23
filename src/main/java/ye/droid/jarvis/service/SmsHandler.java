package ye.droid.jarvis.service;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import ye.droid.jarvis.beans.SmsBean;
import ye.droid.jarvis.utils.ConstantValues;

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
        String content = smsBean.getSmsBody();
        if (content.equals(ConstantValues.SAFE_MESSAGE_ALARM)) {
            Toast.makeText(this.context, "报警铃声", Toast.LENGTH_SHORT).show();
        } else if (content.equals(ConstantValues.SAFE_MESSAGE_LOCATION)) {
            Toast.makeText(this.context, "发送位置", Toast.LENGTH_SHORT).show();
        } else if (content.equals(ConstantValues.SAFE_MESSAGE_WIPEDATA)) {
            Toast.makeText(this.context, "清空数据", Toast.LENGTH_SHORT).show();
        } else if (content.equals(ConstantValues.SAFE_MESSAGE_LOCKSCREEN)) {
            Toast.makeText(this.context, "手机锁屏", Toast.LENGTH_SHORT).show();
        }


        //删除安全短信
        Uri mUri = Uri.parse("content://sms/");
        Log.i(TAG, "lala..." + smsBean.toString());
        this.context.getContentResolver().delete(mUri, "_id=?",
                new String[]{smsBean.get_id()});
    }
}
