package ye.droid.jarvis.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import ye.droid.jarvis.R;

/**
 * Created by ye on 2017/5/7.
 */

public class HomeActivity extends AppCompatActivity {
    private final String TAG = HomeActivity.class.getSimpleName();

    private GridView gv_home;

    private String[] mMenuItems = new String[]{"手机防盗", "通信卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"};
    private int[] mMenuIcons = new int[]{R.drawable.home_against_burglars, R.drawable.home_comm_guard, R.drawable.home_soft_manager,
            R.drawable.home_thread_manager, R.drawable.home_flow_statistic, R.drawable.home_anti_virus,
            R.drawable.home_cache_clean, R.drawable.home_advance_tool, R.drawable.home_setting};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new gridAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent funcIntent = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        funcIntent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(funcIntent);
                        break;
                }
            }
        });

    }

    private class gridAdapter extends BaseAdapter {
        private ImageView iv_menu_icon;
        private TextView tv_menu_title;


        @Override
        public int getCount() {
            return mMenuItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mMenuItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_home_menu, null);
            } else {
                view = convertView;
            }
            iv_menu_icon = (ImageView) view.findViewById(R.id.iv_menu_icon);
            tv_menu_title = (TextView) view.findViewById(R.id.tv_menu_title);

            iv_menu_icon.setBackgroundResource(mMenuIcons[position]);
            tv_menu_title.setText(mMenuItems[position]);

            return view;
        }
    }

}
