package ye.droid.jarvis.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import ye.droid.jarvis.activity.HomeActivity;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * Created by ye on 2017/5/22.
 */

public class RebootReceiver extends BroadcastReceiver {
    private final String TAG = RebootReceiver.class.getSimpleName();
    private final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Log.i(TAG, "Jarvis...手机重启完成！");
            Toast.makeText(context, "手机重启完成了！", Toast.LENGTH_SHORT).show();
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String dontSimNum = manager.getSimSerialNumber() + "a";
            String safeSimNum = SharedPreferencesUtils.getString(context, ConstantValues.SIM_NUMBER, "");
            if (!dontSimNum.equals(safeSimNum)) {
                SmsManager smsManager = SmsManager.getDefault();
                String phone = SharedPreferencesUtils.getString(context, ConstantValues.CONTACT_PHONEV2, "");
                smsManager.sendTextMessage(phone, null, "sim change!", null, null);
                Log.i(TAG, "Jarvis...SIM卡变更短信已经发送！");
            }
        } else {
            Log.i(TAG, "Jarvis...傻逼了吧！");
        }
    }
}
