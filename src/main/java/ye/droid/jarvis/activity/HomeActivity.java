package ye.droid.jarvis.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ye.droid.jarvis.R;
import ye.droid.jarvis.beans.UpDateBean;
import ye.droid.jarvis.service.LocationChangeService;
import ye.droid.jarvis.service.SmsListenerService;
import ye.droid.jarvis.utils.AppUpdateUtils;
import ye.droid.jarvis.utils.CommonUtils;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.DialogFactory;
import ye.droid.jarvis.utils.MD5Utils;
import ye.droid.jarvis.utils.ServiceUtils;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * Created by ye on 2017/5/7.
 */

public class HomeActivity extends AppCompatActivity {
    private final String TAG = HomeActivity.class.getSimpleName();

    private GridView gv_home;
    private TextView tv_showinfo;

    private Handler handler;
    private int delayMillis = 3000;

    private String[] mMenuItems = new String[]{"手机防盗", "通信卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"};
    private int[] mMenuIcons = new int[]{
            R.drawable.home_against_burglars, R.drawable.home_comm_guard, R.drawable.home_soft_manager,
            R.drawable.home_thread_manager, R.drawable.home_flow_statistic, R.drawable.home_anti_virus,
            R.drawable.home_cache_clean, R.drawable.home_advance_tool, R.drawable.home_setting};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        initData();
        checkAllPermission();
        boolean auto_update = SharedPreferencesUtils.getBoolean(this, ConstantValues.AUTO_UPDATE, true); //获取自动更新设置，默认自动更新
        if (auto_update) { //自动更新开启
            tv_showinfo.setText("正在检测更新...");
            checkAppUpdate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValues.CANCEL_INSTALL_UPDATE) {
            Toast.makeText(this, "取消安装新版本！", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        tv_showinfo = (TextView) findViewById(R.id.tv_showinfo);
        gv_home = (GridView) findViewById(R.id.gv_home);
        // 判断Sms卡监听状态，如果监听服务未开启就开启监听服务
        boolean isRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.SmsListenerService", false);
        if (!isRunning) {
            Intent intent = new Intent(this, SmsListenerService.class);
            startService(intent);
        } else {
            Log.i(TAG, "短信监听已经在运行中了！");
        }
    }

    private void initData() {
        handler = new Handler();
        gv_home.setAdapter(new gridAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent funcIntent = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // showPwdDialog();
                        // TODO: 2017/5/11 为了开发方便，暂时取消输入密码的步骤
                        startActivity(new Intent(HomeActivity.this, BurglarsResultActivity.class));
                        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
                        return;
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
                        funcIntent = new Intent(HomeActivity.this, CacheClearActivity.class);
                        startActivity(funcIntent);
                        break;
                    case 7:
                        startActivity(new Intent(HomeActivity.this, AdvanceToolsActivity.class));
                        break;
                    case 8:
                        funcIntent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(funcIntent);
                        break;
                }
                overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);//开启下一页动画
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
                            Intent intent = new Intent(HomeActivity.this, BurglarsResultActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);//开启下一页动画
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
                            Intent intent = new Intent(HomeActivity.this, BurglarsResultActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);//开启下一页动画
                            dialog.dismiss();
                            break;
                    }
                }
            });
        }
    }

    /**
     * 显示彩蛋
     *
     * @param view
     */
    public void showInfo(View view) {
        tv_showinfo.setText("我只是一个彩蛋！");
        resetInfo();
        // TODO: 2017/5/24 屏幕像素测试
        /* if (!tv_showinfo_show) {
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
        }*/
        //todo 发短信测试
        /*SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(SharedPreferencesUtils.getString(HomeActivity.this, ConstantValues.CONTACT_PHONEV2, ""), null, "不去！", null, null);
        Log.i(TAG, "lalala..." + SharedPreferencesUtils.getString(HomeActivity.this, ConstantValues.CONTACT_PHONEV2, ""));*/
        // TODO: 2017/5/24 位置变更测试
      /*  boolean isRunning = ServiceUtils.serviceIsRunning(HomeActivity.this, "ye.droid.jarvis.service.LocationChangeService", false);
        //返回false代表服务没有运行，那么开启服务
        if (!isRunning) {
            Intent intent = new Intent(this, LocationChangeService.class);
            startService(intent);
            Toast.makeText(getApplicationContext(), "位置监听初始化...", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getApplicationContext(), "位置监听已经运行，无需开启...", Toast.LENGTH_SHORT).show();
        tv_showinfo.setText("位置监听运行中...");*/
    }

    /**
     * 检查并申请权限
     * 1. 读取外部存储设备
     * 2. 读取电话状态
     * 3. 读取联系人
     * 4. 发送短信
     * 5. 读取短信
     */
    private void checkAllPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    ConstantValues.HOME_ACTIVITY_REQUEST_ALL_PERMISSION_CODE);
        } else {
            Log.i(TAG, "所有所需权限已经授予！");
        }
    }

    /**
     * 检查App更新
     */
    private void checkAppUpdate() {
        new Thread() {
            @Override
            public void run() {
                final UpDateBean upDateBean = AppUpdateUtils.haveNewVersion(HomeActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (upDateBean.getVersionInfo()) {
                            case ConstantValues.HAVE_UPDATE:
                                //弹出对话框，用户选择是否更新
                                Dialog dialog = DialogFactory.generateDialog(HomeActivity.this,
                                        R.drawable.app_update,
                                        upDateBean.getVersionInfo(),
                                        upDateBean.getVersionDesc(),
                                        null,
                                        "更新",
                                        "忽略",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        //APK
                                                        File apk = AppUpdateUtils.downNewVersionApp(upDateBean);
                                                        if (apk == null) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(getApplicationContext(), ConstantValues.ERROR_UPDATE, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            return;
                                                        }
                                                        installApk(apk);
                                                    }
                                                }.start();
                                            }
                                        }, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(getApplicationContext(), "忽略更新...", Toast.LENGTH_SHORT).show();
                                                tv_showinfo.setText("忽略更新！");
                                                resetInfo();
                                            }
                                        }, new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "取消更新...", Toast.LENGTH_SHORT).show();
                                                tv_showinfo.setText("取消更新！");
                                                resetInfo();
                                            }
                                        });
                                dialog.show();
                                break;
                            case ConstantValues.NOT_UPDATE:
                                //软件已经是最新版本
                                tv_showinfo.setText(upDateBean.getVersionInfo());
                                break;
                            case ConstantValues.ERROR_UPDATE:
                                //服务器异常
                                Toast.makeText(HomeActivity.this, upDateBean.getVersionInfo(), Toast.LENGTH_SHORT).show();
                                tv_showinfo.setText(upDateBean.getVersionInfo());
                                break;
                        }
                        resetInfo();
                    }
                });
            }
        }.start();
    }

    /**
     * 安装APK
     *
     * @param file 待安装软件
     */
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW); //开启安装软件界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //android n 及其以上需要独立设置意图过滤器
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "ye.droid.jarvis.fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else { // android n 一下不需要单独设置意图过滤器
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        //开启安装app界面
        startActivityForResult(intent, ConstantValues.CANCEL_INSTALL_UPDATE);
    }

    /**
     * 重置菜单信息
     */
    private void resetInfo() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_showinfo.setText(getString(R.string.home_odd_egg));
            }
        }, delayMillis);
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
            View view;
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
