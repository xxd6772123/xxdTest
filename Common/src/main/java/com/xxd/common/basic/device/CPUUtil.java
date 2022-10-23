package com.xxd.common.basic.device;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:获取手机CPU相关信息
 */
public final class CPUUtil {

    private static final String PROC_CPU_INFO_PATH = "/proc/cpuinfo";
    private static final String SYS_CPU_PATH = "/sys/devices/system/cpu/";

    /**
     * 获取CPU最小频率（单位KHZ）
     */
    public static final String CPU_MIN_FREQ_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";

    /**
     * 获取CPU当前频率（单位KHZ）
     */
    public static final String CPU_CUR_FREQ_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq";

    /**
     * 获取CPU最大频率（单位KHZ）
     */
    public static final String CPU_MAX_FREQ_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";


    /**
     *  获取当前设备CPU个数，
     * @return 如果返回-1，说明出现读数据异常
     */
    public static int getCPUNumber() {
        try {
            File dir = new File(SYS_CPU_PATH);
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("cpu[0-9]", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据输入路径来获取CPU频率（单位KHZ）
     * @param path 频率数据内容对应的文件路径，目前仅支持 CPU_MIN_FREQ_PATH,CPU_CUR_FREQ_PATH,CPU_MAX_FREQ_PATH
     * @return 频率
     */
    public static String getCPUFreq(String path) {
        String result = "N/A";
        try {
            byte[] data = new byte[24];
            FileInputStream fis = new FileInputStream(path);
            fis.read(data);
            result = new String(data).trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//    有bug，cpu1的信息会覆盖CPU0的信息，结果只返回最后一个CPU的信息
//    public static Map<String,String> getCPUInfo(){
//
//        Map<String,String> maps = new LinkedHashMap<>();
//        try {
//            String line = null;
//            FileReader fr = new FileReader(PROC_CPU_INFO_PATH);
//            BufferedReader br = new BufferedReader(fr);
//            while ((line = br.readLine()) != null) {
//                String[] kv = line.split(":");
//                if (kv != null && kv.length == 2) {
//                    String key = kv[0].trim();
//                    String value = kv[1].trim();
//                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)){
//                        maps.put(key,value);
//                    }
//                }
//            }
//            fr.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return maps;
//    }

}

