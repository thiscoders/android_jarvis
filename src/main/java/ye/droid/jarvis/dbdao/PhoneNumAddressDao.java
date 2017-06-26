package ye.droid.jarvis.dbdao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 访问电话归属地数据库引擎
 * Created by ye on 2017/5/29.
 */

public class PhoneNumAddressDao {
    private static final String TAG = PhoneNumAddressDao.class.getSimpleName();
    private static String dbPath = "/data/data/ye.droid.jarvis/files/location.db";

    public static List<String> getAddress(String phone) {
        if (phone == null) {
            return new ArrayList<String>();
        }

        String regex = "[0-9]*";
        //phone不是数字组成的
        if (!Pattern.matches(regex, phone)) {
            return null;
        }

        int len = phone.length();
        if (len > 7) {
            phone = phone.substring(0, 7);
        }

        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.query("phone_location", new String[]{"area"}, "_id like ?", new String[]{phone + "%"}, "area", null, "area");
        List<String> results = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String area = cursor.getString(0);
            results.add(area);
            Log.i(TAG, "地区是..." + area);
        }
        cursor.close();
        return results;
    }

}
