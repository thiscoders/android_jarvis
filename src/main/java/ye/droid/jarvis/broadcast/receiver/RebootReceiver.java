package ye.droid.jarvis.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ye on 2017/5/22.
 */

public class RebootReceiver extends BroadcastReceiver {
    private final String TAG = RebootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Jarvis...手机重启完成！");
    }
}
