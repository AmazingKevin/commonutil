package cn.ibona.commonutil;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;


public class D {

    public static void init(Context context ) {
         D.context = context;
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


}