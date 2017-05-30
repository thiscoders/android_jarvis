package ye.droid.jarvis.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import ye.droid.jarvis.R;
import ye.droid.jarvis.dbdao.PhoneNumAddressDao;

/**
 * Created by ye on 2017/5/30.
 */

public class SuspendFrameService extends Service {
    private String TAG = SuspendFrameService.class.getSimpleName();
    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;

    private TextView tv_phone_address;
    private String mAddress;
    //自定义吐司
    private WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    private WindowManager mWindowManager;
    private View mToastView;

    //此处只能通过消息机制进行UI更新
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_phone_address.setText(mAddress);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: //空闲状态，没有任何活动
                        Log.i(TAG, "CALL_STATE_IDLE...");
                        if (mWindowManager != null && mToastView != null) {
                            mWindowManager.removeView(mToastView);
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING: //来电状态，电话铃声响起的那段时间或者正在通话中又来了新的电话，而新电话必须等待的那段时间
                        Log.i(TAG, "CALL_STATE_RINGING...");
                        showToast(incomingNumber);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: //摘机状态,至少有一个电话活动，该活动是拨打或者是通话或者是on hold。并且没有电话响铃或等待
                        Log.i(TAG, "CALL_STATE_OFFHOOK...");
                        break;
                }
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (telephonyManager == null || phoneStateListener == null) {
            Log.i(TAG, "为null，关闭失败");
            return;
        }
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE); //取消电话监听
        Log.i(TAG, "服务关闭...");
    }


    private void showToast(final String incomingNumber) {
        WindowManager.LayoutParams layoutParams = mLayoutParams;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.setTitle("Toast");
        layoutParams.gravity = Gravity.LEFT + Gravity.TOP;

        mToastView = View.inflate(this, R.layout.toast_phone_address, null);
        tv_phone_address = (TextView) mToastView.findViewById(R.id.tv_phone_address);

        new Thread() {
            @Override
            public void run() {
                super.run();
                List<String> results = PhoneNumAddressDao.getAddress(incomingNumber);
                if (results == null || results.size() == 0) {
                    mAddress = "未知号码";
                } else {
                    mAddress = results.get(0);
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();

        mWindowManager.addView(mToastView, layoutParams);
    }

}
