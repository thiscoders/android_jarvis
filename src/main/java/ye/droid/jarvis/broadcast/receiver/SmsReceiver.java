package ye.droid.jarvis.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ye on 2017/5/23.
 */

public class SmsReceiver extends BroadcastReceiver {
    private final String TAG = SmsReceiver.class.getSimpleName();
    private final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private final String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Log.i(TAG, "有新短信了！");
            Toast.makeText(context, "短信来了！", Toast.LENGTH_SHORT).show();
        }
    }
}
