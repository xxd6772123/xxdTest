package com.xxd.common.basic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:SharedPreferences存取工具类
 */
public final class SPUtil {

    private SPUtil(){}
    /**
     * 配置文件，文件名
     */
    private static final String SHARE_NAME = "sp_config";

    /**
     * 存字符串
     *
     * @param context 上下文
     * @param key     键
     * @param values  值
     */
    public static void putString(Context context, String key, String values) {
        SharedPreferences sp = getInstance(context);
        sp.edit().putString(key, values).apply();
    }


    /**
     * 取字符串
     *
     * @param context 上下文
     * @param key     键
     * @param values  默认值
     * @return 取出的值
     */
    public static String getString(Context context, String key, String values) {
        SharedPreferences sp = getInstance(context);
        return sp.getString(key, values);
    }

    /**
     * 存字符串
     *
     * @param context 上下文
     * @param key     键
     * @param list    值
     */
    public static void putStringList(Context context, String key, ArrayList<String> list) {
        SharedPreferences sp = getInstance(context);
        String json = new Gson().toJson(list);
        sp.edit().putString(key, json).apply();
    }


    /**
     * 取字符串
     *
     * @param context 上下文
     * @param key     键
     * @return 取出的值
     */
    public static ArrayList<String> getStringList(Context context, String key) {
        SharedPreferences sp = getInstance(context);
        String json = sp.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    /**
     *  通过类名字去获取一个Json对象
     */
    public static <T> T getObject(Context context, Class<T> clazz) {
        String key = clazz.getName();
        String json = getString(context, key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return new Gson().fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 保存一个普通JSon对象，object必须是普通类，而不是泛型，如果是泛型,请使用 {@link SPUtil#putObject(Context, Object, Type)}
     */
    public static void putObject(Context context, Object object) {
        String key = object.getClass().getName();
        putString(context, key, new Gson().toJson(object));
    }

    /**
     * 通过Type去获取一个泛型对象,存储少量数据时使用
     * @param context
     * @param type 如果你要保存 List<Person> 这个类, type应该 传入 new TypeToken<List<Person>>() {}.getType()
     * @param <T> 类似List<Person>
     */
    public static <T> T getObject(Context context, Type type) {
        String key = type.toString();
        String json = getString(context, key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 保存一个泛型对象
     * @param context
     * @param object 类似List<Person> 这种对象
     * @param type 如果你要保存 List<Person> 这个类, type应该 传入 new TypeToken<List<Person>>() {}.getType()
     */
    public static void putObject(Context context, Object object,Type type) {
        String key = type.toString();
        putString(context, key, new Gson().toJson(object));
    }

    /**
     * 存布尔值
     *
     * @param context 上下文
     * @param key     键
     * @param values  值
     */
    public static void putBoolean(Context context, String key, boolean values) {
        SharedPreferences sp = getInstance(context);
        sp.edit().putBoolean(key, values).apply();
    }

    /**
     * 取布尔值
     *
     * @param context 上下文
     * @param key     键
     * @param values  默认值
     * @return true/false
     */
    public static boolean getBoolean(Context context, String key, boolean values) {
        SharedPreferences sp = getInstance(context);
        return sp.getBoolean(key, values);
    }

    /**
     * 存int值
     *
     * @param context 上下文
     * @param key     键
     * @param values  值
     */
    public static void putInt(Context context, String key, int values) {
        SharedPreferences sp = getInstance(context);
        sp.edit().putInt(key, values).apply();
    }

    /**
     * 取int值
     *
     * @param context 上下文
     * @param key     键
     * @param values  默认值
     * @return
     */
    public static int getInt(Context context, String key, int values) {
        SharedPreferences sp = getInstance(context);
        return sp.getInt(key, values);
    }

    /**
     * 删除一条字段
     *
     * @param context 上下文
     * @param key     键
     */
    public static void deleteKey(Context context, String key) {
        SharedPreferences sp = getInstance(context);
        sp.edit().remove(key).apply();
    }

    /**
     * 删除全部数据
     *
     * @param context 上下文
     */
    public static void clear(Context context) {
        SharedPreferences sp = getInstance(context);
        sp.edit().clear().apply();
    }

    /**
     * 查看SharedPreferences保存的所有内容
     * @param context 上下文
     */
    public static void printAllKV(Context context){
        try {
            FileInputStream stream = new FileInputStream(new File("/data/data/" +
                    context.getPackageName() + "/shared_prefs", SHARE_NAME + ".xml"));
            BufferedReader bff = new BufferedReader(new InputStreamReader(stream));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bff.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            LogUtil.e(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SharedPreferences getInstance(Context context){
        return context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
    }

//    public static void main(){
//        Person p =  new Person("test",13);
//        List<Person> list = new ArrayList<>();
//        list.add(p);
//        list.add(p);
//
//        SPUtil.putObject(this,p);
//        SPUtil.putObject(this,list,new TypeToken<List<Person>>(){}.getType());
//
//        p = SPUtil.getObject(this,Person.class);
//        LogUtil.e(p,""+list.size());
//
//        list = SPUtil.getObject(this,new TypeToken<List<Person>>() {}.getType());
//        for (Person p1 : list){
//            LogUtil.e(p1);
//        }
//    }
}
