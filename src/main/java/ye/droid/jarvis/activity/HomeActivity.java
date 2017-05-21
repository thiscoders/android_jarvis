package ye.droid.jarvis.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import ye.droid.jarvis.BuildConfig;
import ye.droid.jarvis.R;
import ye.droid.jarvis.utils.CommonUtils;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.DialogFactory;
import ye.droid.jarvis.utils.DisplayUtils;
import ye.droid.jarvis.utils.JHttpUtils;
import ye.droid.jarvis.utils.MD5Utils;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * Created by ye on 2017/5/7.
 */

public class HomeActivity extends AppCompatActivity {
    private final String TAG = HomeActivity.class.getSimpleName();

    private GridView gv_home;
    private TextView tv_showinfo;

    //更新相关
    private String mUpdateAddress = ConstantValues.UPDATE_ADDRESS;
    private int mLocalVersionCode; //本地版本号

    private boolean tv_showinfo_show = false; //false的时候tv_showinfo显示为null
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
        Toast.makeText(this, "取消安装新版本！", Toast.LENGTH_SHORT).show();
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
                        // TODO: 2017/5/11 为了开发方便，暂时取消输入密码的步骤
                        showPwdDialog();
                        //startActivity(new Intent(HomeActivity.this, BurglarsResultActivity.class));
                        //overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);//开启下一页动画
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
                        startActivity(new Intent(HomeActivity.this, CacheClearActivity.class));
                        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);//开启下一页动画
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
                            Intent intent = new Intent(HomeActivity.this, BurglarsResultActivity.class);
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
                            Intent intent = new Intent(HomeActivity.this, BurglarsResultActivity.class);
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

    /**
     * 检查并申请权限
     * 1.读取外部存储设备
     * 2.读取电话状态
     * 3. 读取联系人
     */
    private void checkAllPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_CONTACTS},
                    ConstantValues.HOME_ACTIVITY_REQUEST_ALL_PERMISSION_CODE);
        } else {
            Log.i(TAG, "所有所需权限已经授予！");
        }
    }

    private void checkAppUpdate() {
        new Thread() {
            @Override
            public void run() {
                //服务器获取的Json字符串
                String jsonzContent = JHttpUtils.getHttpStream(HomeActivity.this, mUpdateAddress);
                //1. 获取更新信息失败
                if (jsonzContent == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HomeActivity.this, "更新失败！服务器跑到火星上去了！！！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    //解析服务器返回的Json字符串
                    JSONObject jsonObject = new JSONObject(jsonzContent);
                    final String versionName = jsonObject.getString("versionName"); //获取新版本名称
                    String versionCode = jsonObject.getString("versionCode"); //获取新版本编号
                    final String versionDesc = jsonObject.getString("versionDesc"); //获取新版本描述
                    final String downloadURL = jsonObject.getString("downloadURL");//获取新版本下载的地址

                    mLocalVersionCode = getVersionCode(); //获取本地软件的版本号
                    if (mLocalVersionCode < Integer.parseInt(versionCode)) //如果本地版本小于远程版本，那么久说明有了新的版本，需要下载更新
                        //UI线程显示更新对话框
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //新建TextView显示更新信息
                                TextView tv_version_desc = new TextView(HomeActivity.this);
                                tv_version_desc.setPadding(60, tv_version_desc.getPaddingTop(), tv_version_desc.getPaddingRight(), tv_version_desc.getPaddingBottom());
                                tv_version_desc.setTextSize(18);
                                tv_version_desc.setText(versionDesc);

                                //获取更新软件提示对话框
                                Dialog updateDialog = DialogFactory.generateDialog(
                                        HomeActivity.this,  //上下文
                                        R.drawable.app_update, // 图标
                                        "更新", //对话框标题
                                        "检测到可用的更新！新版本名称：" + versionName, //对话框内容
                                        tv_version_desc, //显示新版本的textview控件
                                        "更新", //更新按钮 positive
                                        "忽略", //忽略按钮 negative
                                        new DialogInterface.OnClickListener() { //积极按钮点击事件，开始下载更新包
                                            //开始更新点击事件
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //使用xutils框架下载软件安装包，并进行安装
                                                //1. 判断sd卡是否挂载可用
                                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                                    //2. 获取下载路径
                                                    String path = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator + "jarvis.apk";
                                                    HttpUtils httpUtils = new HttpUtils(); //定义HttpUtils下载工具类

                                                    httpUtils.download(downloadURL, path, new RequestCallBack<File>() {
                                                        @Override
                                                        public void onSuccess(ResponseInfo<File> responseInfo) { //下载成功，安装APP
                                                            File apkFile = responseInfo.result;
                                                            Log.i(TAG, "XUtils下载更新包成功！>>>" + apkFile.getAbsolutePath() + "..." + apkFile.length() + "..." + Environment.getExternalStorageDirectory() + "..." + BuildConfig.APPLICATION_ID);
                                                            Toast.makeText(getApplicationContext(), "下载更新包成功，正在开启安装界面...", Toast.LENGTH_SHORT).show();
                                                            installApk(apkFile);
                                                        }

                                                        @Override
                                                        public void onFailure(HttpException e, String s) { //下载失败，进入主界面
                                                            Log.i(TAG, "XUtils下载更新包失败！");
                                                            Toast.makeText(getApplicationContext(), "下载更新包失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onStart() { //开始下载
                                                            super.onStart();
                                                            Log.i(TAG, "XUtils开始下载更新包！");
                                                        }

                                                        @Override
                                                        public void onLoading(long total, long current, boolean isUploading) { //下载过程中的数据更新
                                                            super.onLoading(total, current, isUploading);
                                                            Log.i(TAG, total + "XUtils正在下载更新包...onLoading..." + current);
                                                        }
                                                    });
                                                }
                                            }
                                        },
                                        new DialogInterface.OnClickListener() { //消极按钮点击事件，取消下载更新，进入主界面
                                            //3. 忽略更新点击事件,3s后延时进入主界面
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(getApplicationContext(), "忽略更新...", Toast.LENGTH_SHORT).show();
                                            }
                                        },
                                        new DialogInterface.OnCancelListener() { //点击对话框外的区域触发的事件，关闭更新对话框，进入主界面
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "取消更新...", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                //获取对话框并且显示
                                updateDialog.show();
                            }
                        });
                    else { //软件没发布更新
                        //2. 已经是最新版本了，3s后直接延时进入主界面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeActivity.this, "已经是最新版本了，不用更新！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) { //更新软件异常
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 获取当前软件版本名称
     *
     * @return
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机App版本号
     *
     * @return 返回0获取失败，否则就获取成功
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 安装APK
     *
     * @param file 待安装软件
     */
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "ye.droid.jarvis.fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivityForResult(intent, 100);
    }
}
