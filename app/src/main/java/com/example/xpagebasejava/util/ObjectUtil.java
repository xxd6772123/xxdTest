package com.example.xpagebasejava.util;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author:XiaoDan
 * time:2022/6/25 17:29
 * desc:对象操作工具类
 */
public class ObjectUtil {


    /**
     * 由父类对象创建子类对象
     *
     * @param father 父类
     * @param child  子类
     * @param <T>    类型
     * @throws Exception
     */
    public static <T> void fatherToChild(T father, T child) throws Exception {
        if (child.getClass().getSuperclass() != father.getClass()) {
            throw new Exception("child 不是 father 的子类");
        }
        Class<?> fatherClass = father.getClass();
        Field[] declaredFields = fatherClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];

            Method method = fatherClass.getDeclaredMethod("get" + StringUtils.upperFirstLetter(field.getName()));
            Object obj = method.invoke(father);
            field.setAccessible(true);
            field.set(child, obj);
        }
    }

    /**
     * 创建Fragment对象
     *
     * @param fragmentClass
     * @return
     */
    public static Fragment createFragment(Class<? extends Fragment> fragmentClass) {
        try {
            Fragment fragment = fragmentClass.newInstance();
            return fragment;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
