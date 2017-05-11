package ye.droid.jarvis.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ye.droid.jarvis.R;
import ye.droid.jarvis.utils.CommonUtils;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.DialogFactory;
import ye.droid.jarvis.utils.DisplayUtils;
import ye.droid.jarvis.utils.MD5Utils;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * Created by ye on 2017/5/7.
 */

public class HomeActivity extends AppCompatActivity {
    private final String TAG = HomeActivity.class.getSimpleName();

    private GridView gv_home;
    private TextView tv_showinfo;


    private boolean tv_showinfo_show = false; //false的时候tv_showinfo显示为null
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
        tv_showinfo = (TextView) findViewById(R.id.tv_showinfo);
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new gridAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent funcIntent = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showPwdDialog();
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

    /**
     * 点击手机防盗弹出对话框
     */
    private void showPwdDialog() {
        String pwd = SharedPreferencesUtils.getString(HomeActivity.this, ConstantValues.STORE_PWD, "");
        if (TextUtils.isEmpty(pwd)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.dialog_safe_pwd, null);
            final AlertDialog dialog = builder.create();
            dialog.setView(view);

            dialog.show();

            final EditText et_first_pwd = (EditText) view.findViewById(R.id.et_first_pwd);//初次输入密码
            final EditText et_second_pwd = (EditText) view.findViewById(R.id.et_second_pwd); //再次确认密码

            Button btn_pwd_cancel = (Button) view.findViewById(R.id.btn_pwd_cancel);
            Button btn_pwd_check = (Button) view.findViewById(R.id.btn_pwd_check);
            Button btn_pwd_confirm = (Button) view.findViewById(R.id.btn_pwd_confirm);

            btn_pwd_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btn_pwd_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String first = et_first_pwd.getText().toString();
                    String second = et_second_pwd.getText().toString();
                    int res = CommonUtils.checkString(first, second);
                    switch (res) {
                        case ConstantValues.STRING_NULL:
                            Toast.makeText(HomeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                            break;
                        case ConstantValues.STRING_DIFF:
                            Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                            break;
                        case ConstantValues.STRING_MATCH:
                            Toast.makeText(HomeActivity.this, "检测通过..", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

            btn_pwd_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String first = et_first_pwd.getText().toString();
                    String second = et_second_pwd.getText().toString();
                    int res = CommonUtils.checkString(first, second);
                    switch (res) {
                        case ConstantValues.STRING_NULL:
                            Toast.makeText(HomeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                            break;
                        case ConstantValues.STRING_DIFF:
                            Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                            break;
                        case ConstantValues.STRING_MATCH:
                            SharedPreferencesUtils.putString(HomeActivity.this, ConstantValues.STORE_PWD, MD5Utils.encodeMD5(second));
                            Intent intent = new Intent(HomeActivity.this, BurglarsActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            break;
                    }
                }
            });

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.dialog_confirm_pwd, null);
            final AlertDialog dialog = builder.create();
            dialog.setView(view);
            dialog.show();

            final EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);//初次输入密码
            Button btn_pwd2_cancel = (Button) view.findViewById(R.id.btn_pwd2_cancel);
            Button btn_pwd2_confirm = (Button) view.findViewById(R.id.btn_pwd2_confirm);

            btn_pwd2_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btn_pwd2_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String et_pwd = et_confirm_pwd.getText().toString(); //手动输入的密码
                    if (TextUtils.isEmpty(et_pwd)) {
                        Toast.makeText(HomeActivity.this, "请输入密码...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String sp_pwd = SharedPreferencesUtils.getString(HomeActivity.this, ConstantValues.STORE_PWD, ""); //获取sp的密码
                    int res = CommonUtils.checkString(MD5Utils.encodeMD5(et_pwd), sp_pwd);
                    switch (res) {
                        case ConstantValues.STRING_DIFF:
                            Toast.makeText(HomeActivity.this, "密码错误...", Toast.LENGTH_SHORT).show();
                            break;
                        case ConstantValues.STRING_MATCH:
                            Intent intent = new Intent(HomeActivity.this, BurglarsActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            break;
                    }
                }
            });
        }
    }


    public void showInfo(View view) {
        if (!tv_showinfo_show) {
            String disInfo = "";
            int flag = 0;
            List<String> list = DisplayUtils.getDisInfo(this);
            for (String info : list) {
                disInfo += info;
                flag++;
                if (flag != 6)
                    disInfo += "---";
            }
            tv_showinfo.setVisibility(View.VISIBLE);
            tv_showinfo.setText(disInfo);
            tv_showinfo_show = !tv_showinfo_show;
            tv_showinfo_show = true;
        } else {
            tv_showinfo.setText(this.getString(R.string.home_odd_egg));
            tv_showinfo_show = false;
        }
    }

    /**
     * GridView数据适配器
     */
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
