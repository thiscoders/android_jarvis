package ye.droid.jarvis.cvs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import ye.droid.jarvis.R;

/**
 * Created by ye on 2017/5/9.
 */

public class SettingItem extends LinearLayout {
    private final String TAG = SettingItem.class.getSimpleName();
    private TextView tv_item_title;
    private TextView tv_item_subtitle;
    private CheckBox tv_item_check;


    public SettingItem(Context context) {
        this(context, null);
    }

    public SettingItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(getContext(), R.layout.item_setting, this);

        initUI();


    }

    private void initUI() {
        tv_item_title = (TextView) findViewById(R.id.tv_item_title);
        tv_item_subtitle = (TextView) findViewById(R.id.tv_item_subtitle);
        tv_item_check = (CheckBox) findViewById(R.id.tv_item_check);
    }

    private void initData() {

    }

    /*public SettingItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/
}
