package com.tiendas3b.almacen.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.tiendas3b.almacen.db.dao.User;

/**
 * Created by dfa on 29/02/2016.
 */
public class Preferences {

    public static final String FILE_PREFERENCES = "almacen";
    public static final String KEY_REGION = "A";
    public static final String KEY_LAST_NAME = "B";
    public static final String KEY_PASS = "C";
    public static final String KEY_LOGIN = "D";
    public static final String KEY_URL = "E";
    public static final String KEY_LAST_UPDATE = "F";
    public static final String KEY_NAME = "G";
    public static final String KEY_UID = "H";
    public static final String KEY_GROUP = "I";
    public static final String KEY_VOICE = "J";
    public static final String KEY_TRUCK_ID = "K";
    public static final String KEY_TRAVEL_STATUS = "L";
    public static final String KEY_USER_ID = "M";
    private final Context mContext;

    public Preferences(Context context) {
        mContext = context;
    }

    public void saveData(User user) {
        setSharedStringSafe(KEY_LOGIN, user.getLogin());
        setSharedStringSafe(KEY_PASS, user.getPassword());
        setSharedStringSafe(KEY_NAME, user.getName() + " " + user.getLastName());
        setSharedStringSafe(KEY_GROUP, user.getGroupId());
//        setSharedStringSafe(KEY_LAST_NAME, user.getLastName());
        String regionId = user.getRegion().toString();
        setSharedStringSafe(KEY_REGION, regionId.equals("0") ? "1000" : regionId);
//        setSharedStringSafe(KEY_URL, getUrl(Integer.parseInt(regionId)));
        setSharedLongSafe(KEY_USER_ID, user.getSeq());
    }

    public void saveData(String user, String password, String regionId) {
        setSharedStringSafe(KEY_LOGIN, user);
        setSharedStringSafe(KEY_PASS, password);
        setSharedStringSafe(KEY_REGION, regionId);
//        setSharedStringSafe(KEY_URL, getUrl(Integer.parseInt(regionId)));

//        setSharedLongSafe(KEY_PROFILE, profileId);
//        setSharedStringSafe(KEY_LOGIN, user);
//        setSharedStringSafe(KEY_PASS, password);
//        Encryption encryption = new Encryption();
//        setSharedStringSafe(KEY_FAKE_USU, encryption.generateRandomString());
//        setSharedStringSafe(KEY_FAKE_PASS, encryption.generateRandomString());

    }

//    private String getUrl(int regionId) {
//        switch (regionId){
//            case 1000:
//                return Constants.URL_1000;
//            case 1001:
//                return Constants.URL_1001;
//            case 1002:
//                return Constants.URL_1002;
//            case 1003:
//                return Constants.URL_1003;
//            case 1004:
//                return Constants.URL_1004;
//            default:
//                return Constants.URL_LOCAL;
//        }
//    }

    private void setSharedLongSafe(String key, long value) {
        setSharedString(key, new Encryption().encrypt(String.valueOf(value)));
    }

    public void deleteData() {
//        removeShared(KEY_LOGIN);
        removeShared(KEY_PASS);
//        removeShared(KEY_URL);
//        removeShared(KEY_REGION);
//        removeShared(KEY_LAST_UPDATE);
//        removeShared(KEY_NAME);
////        removeShared(KEY_PROFILE);
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences goSharedPreferences = mContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE);
        return goSharedPreferences.edit();
    }

    public void setSharedStringSafe(String key, String value) {
        setSharedString(key, new Encryption().encrypt(value));
    }

    public void setSharedString(String key, String value) {
        SharedPreferences.Editor voEditor = getEditor();
        voEditor.putString(key, value);
        voEditor.commit();
    }

    public String getSharedStringSafe(String key, String defaultValue) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences pref = mContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE);
        String str = new Encryption().decrypt(pref.getString(key, defaultValue));
        return str == null ? defaultValue : str;
    }

    public void removeShared(String key) {
        SharedPreferences.Editor voEditor = getEditor();
        voEditor.remove(key);
        voEditor.commit();
    }

    public void removeSharedFile(String psFile) {
        SharedPreferences.Editor voEditor = getEditor();
        voEditor.clear();
        voEditor.commit();
    }

    public long getSharedLongSafe(String key, long defaultValue) {
        SharedPreferences pref = mContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE);
        String str = new Encryption().decrypt(pref.getString(key, ""));
        return str == null ? defaultValue : Long.parseLong(str);
    }

    public int getInt(String key, int defaultValue) {
        SharedPreferences pref = mContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultValue);
    }
}
