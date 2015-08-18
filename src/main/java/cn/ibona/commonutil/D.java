package cn.ibona.commonutil;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class D {

    public static void init(Context context ) {

        D.context = context;
        NetUtil.mContext=context;
        AppUtil.mContext=context;
        mHander=new Handler();

    }
    private static Context context;
    private static Handler mHander;


    public static Context getContext() {
        return context;
    }
    /**
     * 用于打印log
     */
    private static final String TAG="Kevin";

    public static void w(String TypeName,String text) {

        Log.i(TAG, TypeName+":" + text);

    }

    ////////////// ////////////// //////////////  ////////////// ////////////// //////////////  ////////////// ////////////// ////////////// 吐司区域
    //todo 吐司
    private static Toast mToast;
    /**
     * @param text
     */
    public static void t(final   CharSequence text) {

        if(Looper.myLooper()!=Looper.getMainLooper())
        {
            //not ui thread
            mHander.post(new Runnable() {
                @Override
                public void run() {
                    showToast(text);
                }
            });
        }
        else
        {
            //sure its ui thread
            showToast( text);
        }

    }

    /**
     * 展示吐司
     * @param text
     */
    private static void showToast(final   CharSequence text)
    {
        if(mToast==null)
        {
            mToast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }
        else
        {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    ////////////// ////////////// //////////////  ////////////// ////////////// //////////////  ////////////// ////////////// //////////////吐司区域

    /**
     * 混淆时必须关闭DEBUG标志
     */
    private static boolean debug = true;

    /**
     * Log.i
     *
     * @param text
     */
    public static void i(String text) {
        if (debug) {
            String className = debugLine();
            Log.i(className, "" + text);
        }
    }



    /**
     * D.i(a,b,c,d,e,f,g);
     *
     * @param text
     */
    public static void i(Object... text) {
        if (debug) {
            String className = debugLine();
            Log.i(className, getText(null, text));
        }
    }


    /**
     * D.i("a,b,c,d,e,f,g",a,b,c,d,e,f,g);
     *
     * @param text
     */
    public static void i(String str, Object... text) {
        if (debug) {
            String className = debugLine();
            if (str != null && text != null) {
                String[] strs = str.split(",");
                if (strs != null && strs.length == text.length) {
                    Log.i(className, getText(strs, text));
                } else {
                    Log.i(className, str + "   " + getText(new String[]{}, text));
                }
            } else {
                Log.i(className, str + "   " + getText(new String[]{}));
            }
        }
    }

    private static <T> String getText(String[] strs, Object... text) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < text.length; i++) {
            Object s = text[i];
            if (strs != null && strs.length > 0) {
                sb.append(strs[i]);
                sb.append(":");
            }
            if (s == null) {
                sb.append("null");
            } else if (s.getClass().isArray()) {

                int count = Array.getLength(s);
                sb.append("[");
                for (int j = 0; j < count; j++) {
                    Object sss = Array.get(s, j);
                    if (j != 0) {
                        sb.append(",");
                    }
                    sb.append(sss.toString());
                }
                sb.append("]");

            } else {
                sb.append(s.toString());
            }
            sb.append("   ");
        }
        return sb.toString();
    }

    /**
     * Log.e
     *
     * @param text
     */
    public static void e(String text) {
        if (debug) {
            String className = debugLine();
            Log.e(className, "" + text);
        }
    }




    /**
     * 取得倒数第n个方法的方法名,0为当前方法，1为上一个方法
     *
     * @param last 倒数第几个方法
     */
    public static String m(int last) {
        String re = "no debug";
        if (debug) {
            if (last < 0) {
                re = "参数异常";
            } else {
                StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3 + last];
                re = " \tat " + getEnd(stackTraceElement);
            }
        }
        return re;
    }


    private static String debugLine(String tag) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String className = stackTraceElement.getFileName();
        className = className.substring(0, className.lastIndexOf("."));
        Log.d(tag, "\tat " + getEnd(stackTraceElement));
        return className;
    }

    private static String debugLine() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String className = stackTraceElement.getFileName();
        className = className.substring(0, className.lastIndexOf("."));
        Log.d(className, "\tat " + getEnd(stackTraceElement));
        return className;
    }

    public static String getStackTraceElements(StackTraceElement[] stackTraceElements) {
        StringBuffer sb = new StringBuffer();
        for (StackTraceElement ste : stackTraceElements) {
            String re = "\tat " + getStackTraceRow(ste);
            sb.append(re);
        }
        return sb.toString();
    }

    private static String getStackTraceRow(StackTraceElement stackTraceElement) {
        StringBuilder buf = new StringBuilder(50);
        String fName = stackTraceElement.getFileName();
        buf.append(stackTraceElement.getClassName());
        buf.append(".");
        buf.append(stackTraceElement.getMethodName());
        buf.append("()");
        if (fName == null) {
            buf.append("(Unknown Source)");
        } else {
            int lineNum = stackTraceElement.getLineNumber();
            buf.append('(');
            buf.append(fName);
            if (lineNum >= 0) {
                buf.append(':');
                buf.append(lineNum);
            }
            buf.append(')');
        }
        buf.append("\n");
        return buf.toString();

    }

    public static String getEnd(StackTraceElement stackTraceElement) {
        StringBuilder buf = new StringBuilder(50);
        String fName = stackTraceElement.getFileName();
        if (fName == null) {
            buf.append("(Unknown Source)");
        } else {
            int lineNum = stackTraceElement.getLineNumber();
            buf.append('(');
            buf.append(fName);
            if (lineNum >= 0) {
                buf.append(':');
                buf.append(lineNum);
            }
            buf.append(')');
        }
        buf.append('[');
        buf.append(stackTraceElement.getMethodName());
        buf.append(']');
        buf.append("\n");
        return buf.toString();

    }

    public static void z(String tag, Object... text) {
        if (debug) {
            String className = debugLine(tag);
            Log.i(tag, getText(null, text));
        }
    }


    ////////////// ////////////// //////////////  ////////////// ////////////// //////////////  ////////////// ////////////// //////////////
    //todo 像素 密度转化工具
    /**
     * 根据手机分辨率从px(像素)的单位转为dp(密度单位)
     * @param pxValue
     * @return 返回密度单位
     */
    public static int px2dip( float pxValue){

        final float scale =context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale + 0.5f);//这里返回值要4舍5入
    }

    /**
     * 根据手机分辨率从dp的单位转化为px(像素)
     * @param dpValue
     * @return px
     */
    public static int dip2px( float dpValue){

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);//必须要进行4舍5入

    }
    ////////////// ////////////// //////////////  ////////////// ////////////// //////////////  ////////////// ////////////// //////////////

//todo md5加密
    /**
     * md5加密
     * @param string
     * @return
     * @throws Exception
     */
    public static   String encode(String string) throws Exception {
        byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //todo double数据规范化
    /**
     * 2可以进行调整,规范到小数第几位
     *
     * 小数 四舍五入
     * @param val
     * @return
     */
    public static Double roundDouble(double val)
    {
        Double ret = null;
        try
        {
            double factor = Math.pow(10, 2);
            ret = Math.floor(val * factor + 0.5) / factor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //todo 字符流工具
    /**
     * 增加一个将流解析成字符串的方法
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is)   {

        ByteArrayOutputStream baos=null;
        try {
            byte[] buffer = new byte[1024];

            baos = new ByteArrayOutputStream();
            int len = 0;
            while(( len = is.read(buffer))!=-1){
                baos.write(buffer, 0, len);
            }

            String result = baos.toString("utf-8");//utf-8
            // System.out.println(baos.toString("utf-8"));

            return result;

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {

            try {
                if(is!=null)
                {
                    is.close();
                    is=null;
                }
                if(baos!=null)
                {
                    baos.close();
                    baos=null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

       return null;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //todo GZIP压缩

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //todo notification
    public static void  sendNotification(String title,String msg,int smallIcon,Bitmap largeIcon)
    {
        //发消息栏 通知
       NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle(title)
                .setContentText(msg )
                .setSmallIcon(smallIcon);



        if(largeIcon!=null)
        {
            builder.setLargeIcon(largeIcon);

        }

        Notification notification = builder.build();
       // notification.flags=Notification.FLAG_NO_CLEAR;//这行代码让通知栏无法通过点击和侧拉消失掉
        // notification.defaults=notification.DEFAULT_SOUND|notification.DEFAULT_VIBRATE;
        /**
         long[] vibrate = {0,100,200,300};
         notification.vibrate = vibrate;
         */

        //  notification.defaults |= Notification.DEFAULT_LIGHTS;
        manager.notify(1,notification);


    }

    /**
     * 验证输入数字是否是
     * @param str
     * @return
     */
    protected static boolean match( String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}