package ye.droid.jarvis.activity.setting;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ye.droid.jarvis.R;
import ye.droid.jarvis.activity.PerfectActivity;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.DisplayUtils;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * 来电悬浮框位置选择
 * Created by ye on 2017/6/4.
 */
public class SuspendLocationActivity extends PerfectActivity {
    private final String TAG = SuspendLocationActivity.class.getSimpleName();

    private TextView tv_location_top;
    private TextView tv_location_drag;
    private TextView tv_location_bottom;

    private int screenWidth;
    private int screenHeight;

    //双击事件次数
    private long[] mHits = new long[2];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspend_location);

        initUI();
        initData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preAnim();
    }

    private void initUI() {
        tv_location_top = (TextView) findViewById(R.id.tv_location_top);
        tv_location_drag = (TextView) findViewById(R.id.tv_location_drag);
        tv_location_bottom = (TextView) findViewById(R.id.tv_location_bottom);
    }

    private void initData() {
        //获取屏幕信息
        screenWidth = (int) DisplayUtils.getDisInfo(SuspendLocationActivity.this, DisplayUtils.WIDTHPIXELS);
        screenHeight = (int) DisplayUtils.getDisInfo(SuspendLocationActivity.this, DisplayUtils.HEIGHTPIXELS);

        //获取悬浮框保存位置的数据并还原保存位置
        int locationX = SharedPreferencesUtils.getInteger(SuspendLocationActivity.this, ConstantValues.SUSPEND_LOACTION_X, 0);
        int locationY = SharedPreferencesUtils.getInteger(SuspendLocationActivity.this, ConstantValues.SUSPEND_LOACTION_Y, 0);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = locationX;
        layoutParams.topMargin = locationY;

        tv_location_drag.setLayoutParams(layoutParams);

        //确定悬浮框的显示
        if (locationY > screenHeight / 2) {
            tv_location_top.setVisibility(View.VISIBLE);
            tv_location_bottom.setVisibility(View.INVISIBLE);
        } else {
            tv_location_top.setVisibility(View.INVISIBLE);
            tv_location_bottom.setVisibility(View.VISIBLE);
        }


        //添加监听事件
        tv_location_drag.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;
            private int moveX;
            private int moveY;
            private int disX;
            private int disY;
            private int left;
            private int top;
            private int right;
            private int bottom;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //获取起始位置
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //获取移动坐标
                        moveX = (int) event.getRawX();
                        moveY = (int) event.getRawY();

                        Log.i(TAG, "move is ..." + moveX + "..." + moveY);
                        //计算移动距离
                        disX = moveX - startX;
                        disY = moveY - startY;
                        //计算新的坐标点
                        left = tv_location_drag.getLeft() + disX;
                        top = tv_location_drag.getTop() + disY;
                        right = tv_location_drag.getRight() + disX;
                        bottom = tv_location_drag.getBottom() + disY;


                        //容错处理，控件不能移出屏幕
                        if (left < 0) {
                            Log.i(TAG, "left overflow ..." + tv_location_drag.getLeft());
                            return true;
                        }
                        if (top < 0) {
                            Log.i(TAG, "top overflow ..." + tv_location_drag.getTop());
                            return true;
                        }
                        if (right > screenWidth) {
                            Log.i(TAG, "right overflow ..." + tv_location_drag.getRight());
                            return true;
                        }
                        if (bottom > screenHeight - 70) {
                            Log.i(TAG, "bottom overflow ..." + tv_location_drag.getBottom());
                            return true;
                        }

                        if (top > screenHeight / 2) {
                            tv_location_top.setVisibility(View.VISIBLE);
                            tv_location_bottom.setVisibility(View.INVISIBLE);
                        } else {
                            tv_location_top.setVisibility(View.INVISIBLE);
                            tv_location_bottom.setVisibility(View.VISIBLE);
                        }

                        //重新设置控件位置
                        tv_location_drag.layout(left, top, right, bottom);

                        //重置起始位置坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        //将位置信息持久化， 存储控件左上角的坐标值
                        SharedPreferencesUtils.putInteger(SuspendLocationActivity.this, ConstantValues.SUSPEND_LOACTION_X, tv_location_drag.getLeft());
                        SharedPreferencesUtils.putInteger(SuspendLocationActivity.this, ConstantValues.SUSPEND_LOACTION_Y, tv_location_drag.getTop());

                        break;
                }
                //返回false则不响应事件
                return false;
            }
        });
    }

    public void resetLocation(View view) {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[mHits.length - 1] - mHits[0] < 500) {
            int left = screenWidth / 2 - tv_location_drag.getWidth() / 2;
            int top = screenHeight / 2 - tv_location_drag.getHeight() / 2;
            int right = screenWidth / 2 + tv_location_drag.getWidth() / 2;
            int bottom = screenHeight / 2 + tv_location_drag.getHeight() / 2;

            tv_location_drag.layout(left, top, right, bottom);
        }
    }


}
