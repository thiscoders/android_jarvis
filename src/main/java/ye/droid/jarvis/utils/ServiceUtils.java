package ye.droid.jarvis.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ye on 2017/5/23.
 */

public class ServiceUtils {

    private static String TAG = ServiceUtils.class.getSimpleName();

    /**
     * @param context   上下文
     * @param className 服务名
     * @param debug     是否开启debug,开启debug就会弹出toast
     * @return
     */
    public static boolean serviceIsRunning(Context context, String className, boolean debug) {
        boolean running = false;
        //获取ActivityManager对象
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取所有服务列表
        List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);

        //判断services是否可用
        if (services != null && services.size() > 0) {
            //遍历services
            for (ActivityManager.RunningServiceInfo serviceInfo : services) {
                //  Log.i(TAG, "className=" + serviceInfo.service.getClassName());
                //判断服务是否存在
                if (className.equals(serviceInfo.service.getClassName())) {
                    running = true;
                    if (debug)
                        Toast.makeText(context, className.substring(className.lastIndexOf(".") + 1) + "正在运行...", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (debug) {
            if (!running)
                Toast.makeText(context, className.substring(className.lastIndexOf(".") + 1) + "没有运行...", Toast.LENGTH_SHORT).show();
        }
        return running;
    }

}
