package com.rapik.example.aplikasiform;


import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    /* Pendeklarasian key-value berupa String, sebagai variabel penyimpan data,
    sehingga setiap data memiliki key yang berbeda satu sama lain */
    static final String KEY_USER_TEREGISTER ="user", KEY_PASS_TEREGISTER ="pass";
    static final String KEY_USERNAME_SEDANG_LOGIN = "Username_logged_in";
    static final String KEY_STATUS_SEDANG_LOGIN = "Status_logged_in";
    /* Mendeklarasikan Shared Preferences dengan paramater context */
    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    /* Mendeklarasikan Edit Preferences dan mengubah data KEY_USER_TEREGISTER
    dengan parameter username */
    public static void setRegisteredUser(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USER_TEREGISTER, username);
        editor.apply();
    }
    /* Mengembalikan nilai dari key KEY_USER_TEREGISTER berupa String */
    public static String getRegisteredUser(Context context){return getSharedPreference(context).getString(KEY_USER_TEREGISTER,"");
    }
    /* Mendeklarasikan Edit Preferences KEY_PASS_TEREGISTER dengan parameter
    password */
    public static void setRegisteredPass(Context context, String password){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_PASS_TEREGISTER, PasswordHasher.hash(password));
        editor.apply();
    }
    /* Mengembalikan nilai dari key KEY_PASS_TEREGISTER berupa String */
    public static String getRegisteredPass(Context context){
        return getSharedPreference(context).getString(KEY_PASS_TEREGISTER,"");
    }
    /* Mendeklarasikan Edit Preferences KEY_USERNAME_SEDANG_LOGIN dengan parameter
    username */
    public static void setLoggedInUser(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USERNAME_SEDANG_LOGIN, username);
        editor.apply();
    }
    /* Mengembalikan nilai dari key KEY_USERNAME_SEDANG_LOGIN berupa String */
    public static String getLoggedInUser(Context context){
        return
                getSharedPreference(context).getString(KEY_USERNAME_SEDANG_LOGIN,"");
    }
    /* Mendeklarasikan Edit Preferences KEY_STATUS_SEDANG_LOGIN dengan parameter
    status */
    public static void setLoggedInStatus(Context context, boolean status){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_STATUS_SEDANG_LOGIN,status);
        editor.apply();
    }
    /* Mengembalikan nilai dari key KEY_STATUS_SEDANG_LOGIN berupa boolean */
    public static boolean getLoggedInStatus(Context context){
        return
                getSharedPreference(context).getBoolean(KEY_STATUS_SEDANG_LOGIN,false)
                        && !getRegisteredUser(context).isEmpty()
                        && getRegisteredUser(context).equals(getLoggedInUser(context));
    }
    /* Mendeklarasikan Edit Preferences dan menghapus data, sehingga menjadikannya
    bernilai default. Khusus untuk data yang memiliki key KEY_USERNAME_SEDANG_LOGIN
    dan KEY_STATUS_SEDANG_LOGIN */
    public static boolean clearLoggedInUser (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_USERNAME_SEDANG_LOGIN);
        editor.remove(KEY_STATUS_SEDANG_LOGIN);
        return editor.commit();
    }
    public static boolean hasRegisteredAccount(Context context) {
        return !getRegisteredUser(context).isEmpty();
    }

    public static boolean registerAccount(Context context, String username, String password) {
        if (hasRegisteredAccount(context)) return false;
        return getSharedPreference(context).edit()
                .putString(KEY_USER_TEREGISTER, username)
                .putString(KEY_PASS_TEREGISTER, PasswordHasher.hash(password))
                .remove(KEY_USERNAME_SEDANG_LOGIN)
                .remove(KEY_STATUS_SEDANG_LOGIN).commit();
    }

    public static boolean verifyPassword(Context context, String password) {
        PasswordHasher.Verification result = PasswordHasher.verify(password, getRegisteredPass(context));
        if (!result.matches) return false;
        if (result.replacementHash != null && !getSharedPreference(context).edit()
                .putString(KEY_PASS_TEREGISTER, result.replacementHash).commit()) {
            throw new IllegalStateException("Password upgrade could not be saved.");
        }
        return true;
    }

    public static boolean signIn(Context context) {
        if (!hasRegisteredAccount(context)) return false;
        return getSharedPreference(context).edit()
                .putString(KEY_USERNAME_SEDANG_LOGIN, getRegisteredUser(context))
                .putBoolean(KEY_STATUS_SEDANG_LOGIN, true).commit();
    }

    public static boolean requireLogin(Activity activity) {
        if (getLoggedInStatus(activity)) return true;
        activity.startActivity(new Intent(activity, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        activity.finish();
        return false;
    }
}
