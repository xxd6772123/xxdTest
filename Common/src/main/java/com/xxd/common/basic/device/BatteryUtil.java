package com.xxd.common.basic.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:获取系统电量信息
 */
public final class BatteryUtil {

    /**
     * 获取电池相关信息回调接口
     */
    public interface OnBatteryStatusChangedListener {
        void onBatteryStatusChanged(BatteryInfo info);
    }

    /**
     * 获取电池相关信息,若要持续监听电池变化使用{@link #registerBatteryStateChangedListener(Context,OnBatteryStatusChangedListener)}
     * @param context
     * @return 电池信息对象
     */
    public static BatteryInfo getBatteryInfo(Context context){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent i = context.registerReceiver(null,intentFilter);
        return new BatteryInfo(i);
    }

    /**
     * 监听电池信息变化，使用完后一定要执行{@link  #unregisterBatteryListener(Context)}
     * @param context
     * @param listener 回调接口
     */
    public static void registerBatteryStateChangedListener(Context context,OnBatteryStatusChangedListener listener){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        BatteryChangedReceiver.Holder.sInstance.mListener = listener;
        context.registerReceiver(BatteryChangedReceiver.Holder.sInstance,intentFilter);
    }

    /**
     * 取消监听
     * @param context
     */
    public static void unregisterBatteryListener(Context context){
        BatteryChangedReceiver i = BatteryChangedReceiver.Holder.sInstance;
        i.mListener = null;
        context.unregisterReceiver(i);
    }

    /**
     * 电量变化通知,需添加权限{@link android.content.Intent#ACTION_BATTERY_CHANGED }
     */
    private static class BatteryChangedReceiver extends BroadcastReceiver {
        private static class Holder {
            private static final BatteryChangedReceiver sInstance = new BatteryChangedReceiver();
        }

        private OnBatteryStatusChangedListener mListener;
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                if (mListener != null){
                    mListener.onBatteryStatusChanged(new BatteryInfo(intent));
                }
            }
        }
    }

    public static class BatteryInfo{

        int battery_low;
        /**
         * 电池健康状态
         * {@link android.os.BatteryManager#BATTERY_HEALTH_GOOD}
         */
        int health;
        /**
         * 电池剩余电量
         */
        int level;
        /**
         * 电池充电方式
         * {@link android.os.BatteryManager#BATTERY_PLUGGED_USB}
         */
        int plugged;
        /**
         * 电池是否存在
         */
        boolean present;

        /**
         * 电池总量,满电100
         */
        int scale;

        /**
         * 电池状态
         * {@link android.os.BatteryManager#BATTERY_STATUS_CHARGING}
         */
        int status;

        /**
         * 电池类型
         */
        String technology;

        /**
         * 电池温度：0.1度为单位（int）即197表示19.7度
         */
        int temperature;

        /**
         * 电池电压
         */
        int voltage;

        private  BatteryInfo(Intent intent){
            this.battery_low = intent.getIntExtra(BatteryManager.EXTRA_BATTERY_LOW,0);
            this.health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            this.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            this.plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            this.present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT,true);
            this.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            this.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
            this.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            this.temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            this.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
        }

        public int getBattery_low() {
            return battery_low;
        }

        public int getHealth() {
            return health;
        }

        public int getLevel() {
            return level;
        }

        public int getPlugged() {
            return plugged;
        }

        public boolean isPresent() {
            return present;
        }

        public int getScale() {
            return scale;
        }

        public int getStatus() {
            return status;
        }

        public String getTechnology() {
            return technology;
        }

        public int getTemperature() {
            return temperature;
        }

        public int getVoltage() {
            return voltage;
        }

        @Override
        public String toString() {
            return "BatteryInfo{" +
                    "battery_low=" + battery_low +
                    ", health=" + health +
                    ", level=" + level +
                    ", plugged=" + plugged +
                    ", present=" + present +
                    ", scale=" + scale +
                    ", status=" + status +
                    ", technology=" + technology +
                    ", temperature=" + temperature +
                    ", voltage=" + voltage +
                    '}';
        }
    }


//    /**
//     * 从BatteryManager对象中获取相关电池信息
//     */
//    public static class BatteryInfo2 {
//        /**
//         * 电池容量
//         */
//        int capacity;
//
//        /**
//         * 充电次数
//         */
//        int changeCount;
//
//        /**
//         * 使能次数
//         */
//        int energyCount;
//
//        /**
//         * 平均电池电流，单位微安
//         */
//        int currentAverage;
//
//        /**
//         * 当前电流
//         */
//        int currentNow;
//
//        /**
//         * 电池状态
//         * {@link BatteryManager.BATTERY_STATUS_CHARGING}
//         */
//        int status;
//
//        /**
//         * 计算电池充满剩余时长
//         * {@link BatteryManager.BATTERY_STATUS_CHARGING}
//         */
//        long chargeTimeRemaining;
//
//        boolean isCharging;
//
//        private BatteryInfo2(BatteryManager manager){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                this.isCharging = manager.isCharging();
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                this.chargeTimeRemaining = manager.computeChargeTimeRemaining();
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                this.capacity = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//                this.changeCount =  manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
//                this.energyCount =  manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
//                this.currentAverage =  manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
//                this.currentNow =  manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
//                this.status =  manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS);
//            }
//        }
//
//        @Override
//        public String toString() {
//            return "BatteryInfo2{" +
//                    "capacity=" + capacity +
//                    ", changeCount=" + changeCount +
//                    ", energyCount=" + energyCount +
//                    ", currentAverage=" + currentAverage +
//                    ", currentNow=" + currentNow +
//                    ", status=" + status +
//                    ", chargeTimeRemaining=" + chargeTimeRemaining +
//                    ", isCharging=" + isCharging +
//                    '}';
//        }
//    }

}
