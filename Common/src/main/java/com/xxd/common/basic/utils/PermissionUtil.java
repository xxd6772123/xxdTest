package com.xxd.common.basic.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:对第三方权限请求框架的封装工具类（后续如果需要替换第三方框架，只需改这里面的代码即可）
 *  * 备注：在targetSdkVersion为30及以上（即Android11以上）的，在获取读写权限时，一定要记得
 *  * 在AndroidManifest.xml中声明android.permission.MANAGE_EXTERNAL_STORAGE权限
 */
public class PermissionUtil {


    public interface ICallback {
        /**
         * 有权限被同意授予时回调
         *
         * @param permissions 请求成功的权限组
         * @param all         是否全部授予了
         */
        void onGranted(List<String> permissions, boolean all);

        /**
         * 有权限被拒绝授予时回调
         *
         * @param permissions 请求失败的权限组
         * @param never       是否有某个权限被永久拒绝了
         */
        default void onDenied(List<String> permissions, boolean never) {
        }

    }

    /**
     * 简单的回调接口（该接口只管所申请的权限是否有全部通过）
     */
    public interface ISimpleCallback {

        /**
         * 回调方法
         *
         * @param granted true：表示所申请权限全部被授予  false：表示没有被全部授予
         */
        void onGranted(boolean granted);

    }

    private FragmentActivity mFragmentActivity;
    private List<String> mPermissionList = new ArrayList<>();

    private PermissionUtil(FragmentActivity activity) {
        mFragmentActivity = activity;
    }

    public static PermissionUtil with(FragmentActivity activity) {
        return new PermissionUtil(activity);
    }

    public static PermissionUtil with(Fragment fragment) {
        return with(fragment.getActivity());
    }

    public PermissionUtil permission(String... permissions) {
        if (permissions != null && permissions.length > 0) {
            mPermissionList.addAll(Arrays.asList(permissions));
        }
        return this;
    }

    public PermissionUtil permission(List<String> permissionList) {
        if (permissionList != null && permissionList.size() > 0) {
            mPermissionList.addAll(permissionList);
        }
        return this;
    }

    public void request(final ICallback callback) {
        request(callback, null);
    }

    public void request(final ISimpleCallback callback) {
        request(null, callback);
    }

    private void request(final ICallback callback, final ISimpleCallback simpleCallback) {
        handlePermissionListFirst();
        XXPermissions.with(mFragmentActivity)
                .permission(mPermissionList)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (callback != null) {
                            callback.onGranted(permissions, all);
                        }
                        if (all) {
                            if (simpleCallback != null) {
                                simpleCallback.onGranted(true);
                            }
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (callback != null) {
                            callback.onDenied(permissions, never);
                        }
                        if (simpleCallback != null) {
                            simpleCallback.onGranted(false);
                        }
                    }
                });
    }

    /**
     * 该方法主要是针对Android11上读写权限变更做的适配处理
     */
    private void handlePermissionListFirst() {
        if (mPermissionList == null || mPermissionList.size() == 0) {
            return;
        }
        if (mFragmentActivity.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.R) {
            //目标版本为Android11的设备上，就将读写权限替换成MANAGE_EXTERNAL_STORAGE
            boolean needAdd = false;
            for (int i = 0; i < mPermissionList.size(); i++) {
                if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(mPermissionList.get(i)) ||
                        Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(mPermissionList.get(i))) {
                    mPermissionList.remove(i);
                    i--;
                    needAdd = true;
                }
            }
            if (!mPermissionList.contains(Manifest.permission.MANAGE_EXTERNAL_STORAGE) && needAdd) {
                mPermissionList.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
            }
            return;
        }
    }


    /**--------------------------下面为Static的工具方法-----------------------------*/

    /**
     * 申请应用使用情况权限
     *
     * @param activity
     * @param reqCode
     */
    public static void reqUsageStatsPermission(Activity activity, int reqCode) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        activity.startActivityForResult(intent, reqCode);
    }

    /**
     * 申请应用使用情况权限
     *
     * @param fragment
     * @param reqCode
     */
    public static void reqUsageStatsPermission(Fragment fragment, int reqCode) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        fragment.startActivityForResult(intent, reqCode);
    }
}
