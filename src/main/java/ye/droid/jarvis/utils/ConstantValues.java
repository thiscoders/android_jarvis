package ye.droid.jarvis.utils;

/**
 * 已经使用的数字 0-10(自动更新),20(请求权限码),100-110(开启Activity使用)
 * <p>
 * Created by ye on 2017/5/9.
 */
public class ConstantValues {
    /**
     * app设置相关
     */
    //开启APP自动更新标识
    public final static String UPDATE_ADDRESS = "http://192.168.191.2:8080/jarvis/jarvis.json";

    //SettingActivity：自动更新KAY
    public final static String AUTO_UPDATE = "auto_update";
    //取消安装更新包的请求码
    public final static int CANCEL_INSTALL_UPDATE = 200;

    //HomeActivity：手机防盗密码
    public final static String STORE_PWD = "store_pwd";

    //防盗页面设置信息
    public final static String BURGLARS_SET_OVER = "burglars_set_over";

    //BurglarsSetup3Activity : sim卡编号
    public final static String SIM_NUMBER = "sim_number";

    //BurglarsSetup3Activity : SharedPreferences保存姓名键
    public final static String CONTACT_NAME = "contact_name";
    //BurglarsSetup3Activity : SharedPreferences保存电话号码键
    public final static String CONTACT_PHONEV2 = "contact_phonev2";

    //BurglarsSetup4Activity : 是否开启手机防盗
    public final static String OPEN_SECURE_FLAG = "open_secure_flag";

    /**
     * 开启activity的各种请求码，响应码等
     */
    //  homeActivity请求所有APP所需权限的请求码
    public final static int HOME_ACTIVITY_REQUEST_ALL_PERMISSION_CODE = 20;
    public final static int BURGLARS4_DEVICE_ADMIN_REQUEST_CODE = 30;

    /**
     * 各种activity返回的时候所对应的键
     */
    // BURGLARSSETUP3_ACTIVITY开启SelectContacts界面的意图请求码
    public final static int BURGLARSSETUP3_ACTIVITY_SELECT_CONTACTS_REQUEST_CODE = 100;
    //CONTACTLIST_ACTIVITY返回BURGLARSSETUP3_ACTIVITY的意图结果码
    public final static int CONTACTLIST_ACTIVITY_SELECT_CONTACTS_RESULT_CODE = 101;
    //归属地查询界面打开本地联系人
    public final static int ATTRLOOKUP_ACTIVITY_SELECT_CONTACTS_REQUEST_CODE = 102;
    //CONTACTLIST_ACTIVITY返回BURGLARSSETUP3_ACTIVITY的联系人姓名KEY
    public final static String CONTACTLIST_ACTIVITY_CONTACTS_NAME_FLAG = "contactlist_activity_select_contacts_name_result_flag";
    //CONTACTLIST_ACTIVITY返回BURGLARSSETUP3_ACTIVITY的联系人手机号码KEY
    public final static String CONTACTLIST_ACTIVITY_CONTACTS_PHONEV2_FLAG = "contactlist_activity_select_contacts_phonev2_result_flag";


    /**
     * 以下为工具相关类
     */
    //字符串为null
    public final static int STRING_NULL = 0;
    //两个字符串不一样
    public final static int STRING_DIFF = 1;
    //两个字符串相同
    public final static int STRING_MATCH = 2;

    //自动更新APP相关
    public final static String HAVE_UPDATE = "有新的软件版本";
    public final static String NOT_UPDATE = "已经是最新版本";
    public final static String ERROR_UPDATE = "服务器飞到火星上去了";


    public static String SAFE_MESSAGE_ALARM = "##**ALARM**##";
    public static String SAFE_MESSAGE_LOCATION = "##**LOCATION**##";
    public static String SAFE_MESSAGE_WIPEDATA = "##**WIPEDATA**##";
    public static String SAFE_MESSAGE_LOCKSCREEN = "##**LOCKSCREEN**##";

    //广播接受者相关
    public static String JEXTRA_ADD_EXPLANATION = "jextra_add_explanation";

    //来电归属地悬浮框的键
    public static String TOAST_PHONE_ADDRESS = "toast_phone_address";
    public static String PHONE_ADDRESS_SUSPEND = "phone_address_suspend";
    public static String SUSPEND_LOACTION_X = "suspend_loaction_x";
    public static String SUSPEND_LOACTION_Y = "suspend_loaction_y";


}
