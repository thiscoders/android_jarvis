package ye.droid.jarvis.service.burglars;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * 开启短信监听服务
 * Created by ye on 2017/5/23.
 */

public class SmsListenerService extends Service {
    private String TAG = SmsListenerService.class.getSimpleName();
    private SmsObserver smsObserver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ContentResolver resolver = getContentResolver();
        smsObserver = new SmsObserver(this, resolver, new SmsHandler(this));
        resolver.registerContentObserver(Uri.parse("content://sms"), true, smsObserver);
        Log.i(TAG,"短信监听服务已经启动！");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(smsObserver);
        Process.killProcess(Process.myPid());
        Log.i(TAG,"短信监听服务已经关闭！");
    }

}
