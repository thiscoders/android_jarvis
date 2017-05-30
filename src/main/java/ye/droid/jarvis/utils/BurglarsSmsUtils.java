package ye.droid.jarvis.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.util.Log;

import ye.droid.jarvis.R;
import ye.droid.jarvis.broadcast.receiver.JDeviceAdminReceiver;
import ye.droid.jarvis.service.LocationChangeService;

/**
 * 用来梳理手机防盗的4个功能
 * Created by ye on 2017/5/24.
 */

public class BurglarsSmsUtils {
    private static String TAG = BurglarsSmsUtils.class.getSimpleName();
    private static boolean locFlag = false;


    private static ComponentName mDeviceAdminSample;
    private static DevicePolicyManager mDevicePolicyManager;

    /**
     * 播放报警音乐
     *
     * @param context
     */
    public static void playAlarm(Context context) {
        //获取媒体播放器对象
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm_spongebob);
        //设置无限循环播放
        mediaPlayer.setLooping(true);
        //开始播放
        mediaPlayer.start();
        String safePhone = SharedPreferencesUtils.getString(context, ConstantValues.CONTACT_PHONEV2, "");
        sendSms(safePhone, "报警音乐已经播放！");
    }

    /**
     * 给安全号码发送手机定位
     *
     * @param context
     */
    public static void sendLocation(Context context) {
        boolean isRunning = ServiceUtils.serviceIsRunning(context, "ye.droid.jarvis.service.LocationChangeService", false);
        //返回false代表服务没有运行，那么开启服务
        if (!isRunning) {
            Intent intent = new Intent(context, LocationChangeService.class);
            context.startService(intent);
            Log.i(TAG, "位置监听服务开始运行！");
            return;
        }
        Log.i(TAG, "位置变化服务已经开启！不用再次开启了！");
    }


    /**
     * 清除数据
     */
    public static void wipeLocData(Context context) {
        initDeviceAdmin(context);//初始化数据
        //判断是否授予设备管理器权限，是就清除数据，否就申请权限
        if (mDevicePolicyManager.isAdminActive(mDeviceAdminSample)) {
            // TODO: 2017/5/29 模拟器不能真实清除数据，项目发布时的时候再取消这个注释
            //mDevicePolicyManager.wipeData(0);
        } else {
            Log.i(TAG, "清除数据、、、设备管理员权限未授予！");
        }
    }

    /**
     * 锁屏
     *
     * @param context
     * @param pwd
     */
    public static void lockDevice(Context context, String pwd) {
        initDeviceAdmin(context); //初始化数据
        //判断是否授予设备管理器权限，是就锁屏，否就申请权限
        if (mDevicePolicyManager.isAdminActive(mDeviceAdminSample)) {
            mDevicePolicyManager.lockNow();
            // TODO: 2017/5/29 为了安全开发，真机调试暂时取消重置密码
            // mDevicePolicyManager.resetPassword("abc", 0);
        } else {
            Log.i(TAG, "手机锁屏、、、设备管理员权限未授予！");
        }
    }

    /**
     * 初始化设备管理器数据
     *
     * @param context
     */
    public static void initDeviceAdmin(Context context) {
        if (mDevicePolicyManager == null) {
            mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        }
        if (mDeviceAdminSample == null) {
            mDeviceAdminSample = new ComponentName(context, JDeviceAdminReceiver.class);
        }
    }

    /**
     * 发送短信
     *
     * @param phone   手机号码
     * @param smsBody 短信内容
     */
    public static void sendSms(String phone, String smsBody) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, smsBody, null, null);
    }

}
