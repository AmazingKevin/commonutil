package cn.ibona.commonutil.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * FileName: AppUtil.java description：AppUtil
 *
 */
public class AppUtil {


    public static Context mContext;

    /**
     * 描述：安装指定文件路径的apk文件
     * @param path
     */
    public static void installApk(  String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent); // 安装新版本
    }

    /**
     * 描述：卸载程序.
     *
     * @param packageName
     *            包名
     */
    public static void uninstallApk(  String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        mContext.startActivity(intent);
    }

    /**
     * 描述：创建桌面快捷方式
     *
     * @param shortcutName
     *            ,应用图标名字
     * @param resId
     *            应用图标 需要权限<uses-permission
     *            android:name="com.android.launcher.permission.INSTALL_SHORTCUT"
     *            />
     */
    public static void createShortcut( String shortcutName,
                                      int resId) {
        Intent shortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        shortcut.putExtra("duplicate", false);
        ComponentName comp = new ComponentName(mContext.getPackageName(), "."
                + ((Activity) mContext).getLocalClassName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
                Intent.ACTION_MAIN).setComponent(comp));
        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
                mContext, resId);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        mContext.sendBroadcast(shortcut);
    }

    /**
     * 描述：用来判断服务是否运行.
     *
     *            the context
     * @param className
     *            判断的服务名字 服务包名"com.xxx.xx..XXXService"
     * @return true 在运行;false不在运行.
     */
    public static boolean isServiceRunning( String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> servicesList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        Iterator<RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            RunningServiceInfo si = (RunningServiceInfo) l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 描述：停止服务.
     *
     * @param className
     *            the class name
     * @return true, if successful
     */
    public static boolean stopRunningService(  String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(mContext, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = mContext.stopService(intent_service);
        }
        return ret;
    }


    /**
     * Gps是否打开 需要<uses-permission
     * android:name="android.permission.ACCESS_FINE_LOCATION" />权限
     * @return true, if is gps enabled
     */
    public static boolean isGpsEnabled( ) {
        LocationManager lm = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    /**
     * 导入数据库.
     * @param dbName
     *            the db name
     * @param rawRes
     *            the raw res
     * @return true, if successful
     */
    public static boolean importDatabase(  String dbName,
                                         int rawRes) {
        int buffer_size = 1024;
        InputStream is = null;
        FileOutputStream fos = null;
        boolean flag = false;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("/data/data/");
            sb.append(mContext.getPackageName());
            sb.append("/databases/");
            sb.append(dbName);
            String dbPath = sb.toString();
            File dbfile = new File(dbPath);
            // 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            if (!dbfile.exists()) {
                // 欲导入的数据库
                if (!dbfile.getParentFile().exists()) {
                    dbfile.getParentFile().mkdirs();
                }
                dbfile.createNewFile();
                is = mContext.getResources().openRawResource(rawRes);
                fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[buffer_size];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
        return flag;
    }

    /**
     * 描述： 获取屏幕尺寸与密度.
     *            the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics() {
        Resources mResources;
        if (mContext == null) {
            mResources = Resources.getSystem();

        } else {
            mResources = mContext.getResources();
        }
        // DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5,
        // xdpi=160.421, ydpi=159.497}
        // DisplayMetrics{density=2.0, width=720, height=1280,
        // scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }

    /**
     * 描述：打开键盘.
     */
    public static void showSoftInput( ) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 描述：关闭键盘事件.
     *            the context
     */
    public static void closeSoftInput( ) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null
                && ((Activity) mContext).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) mContext)
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 描述：获取包信息.
     *            the context
     */
    public static PackageInfo getPackageInfo( ) {
        PackageInfo info = null;
        try {
            String packageName = mContext.getPackageName();
            info = mContext.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 描述：获取versionName
     * @return
     */
    public static String getVersionName( ) {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述：获取当前正在运行的Activity
     *
     * @return shortClassName类名 需要权限<uses-permission
     *         android:name="android.permission.GET_TASKS"/>
     */
    @SuppressWarnings("deprecation")
    public static String getActivityName( ) {
        ActivityManager manager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName(); // 类名
        // String className = info.topActivity.getClassName(); //完整类名
        // String packageName = info.topActivity.getPackageName(); //包名
        return shortClassName; // 类名
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * 获得核心数
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    // Check if filename is "cpu", followed by a single digit
                    // number
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }

            });
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     *
     * 描述：获取可用内存.
     * @return
     */
    public static long getAvailMemory() {
        // 获取android当前可用内存大小
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        // 当前系统可用内存 ,将获得的内存大小规格化
        return memoryInfo.availMem;
    }

    /**
     *
     * 描述：总内存.
     * @return
     */
    public static long getTotalMemory( ) {
        // 系统内存信息文件
        String file = "/proc/meminfo";
        String memInfo;
        String[] strs;
        long memory = 0;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
            // 读取meminfo第一行，系统内存大小
            memInfo = bufferedReader.readLine();
            strs = memInfo.split("\\s+");

            // 获得系统总内存，单位KB
            memory = Integer.valueOf(strs[1]).intValue() * 1024;
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Byte转位KB或MB
        return memory;
    }

    /**
     *
     * 描述：获取mac地址.
     *
     * @return
     */
    public static String getMac( ) {
        WifiManager wifi = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info.getMacAddress() == null) {
            return null;
        } else {
            return info.getMacAddress();
        }
    }

    /**
     *
     * 描述：获取SSID地址.
     * @return
     */
    public static String getSSID( ) {

        WifiManager wifi = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info.getSSID() == null) {
            return null;
        } else {
            return info.getSSID();
        }
    }

    /**
     *
     * 描述：获取IMSI.
     *
     * @return
     */
    public static String getIMSI( ) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getSubscriberId() == null) {
            return null;
        } else {
            return telephonyManager.getSubscriberId();
        }
    }

    /**
     *
     * 描述：获取IMEI.
     *
     * @return
     */
    public static String getIMEI( ) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getDeviceId() == null) {
            return null;
        } else {
            return telephonyManager.getDeviceId();
        }
    }

    /**
     * 描述：获取手机号码
     *
     * @return
     */
    public static String getPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getLine1Number() == null
                || telephonyManager.getLine1Number().length() < 11) {
            return null;
        } else {
            return telephonyManager.getLine1Number();
        }
    }
}