package cn.ibona.commonutil;

import android.os.Build;
import android.os.Environment;

import org.litepal.LitePalApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import cn.ibona.commonutil.utils.W;

/**
 * Created by Kevin on 2015/9/2.
 */                         // 继承了LitePalApplication 初始化litepal
public class BaseApp extends LitePalApplication {

    /**
     * 集成的有  volley gson litepal butterknife indicator pingyin sidebar util
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化所有工具类
        W.init(this);

        //初始化异常捕获线程
        Thread.currentThread().setUncaughtExceptionHandler(
                new MyUncaughtExceptionHandler());


    }



    /**
     * 异常捕获线程
     */
    class MyUncaughtExceptionHandler implements
            Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            /**
             * 获取异常信息(补救) 把异常文件写道sd卡中,再通过服务器上传
             */
            File file = new File(Environment.getExternalStorageDirectory(),
                    "whatthefuck.log");

            StringWriter sw;
            FileOutputStream fos;
            PrintWriter err;
            try {

                fos = new FileOutputStream(file);
                sw = new StringWriter();
                err = new PrintWriter(sw);

                Field[] fields = Build.class.getFields();
                for (Field field : fields) {
                    //静态属性,不需要对象
                    sw.write(field.getName()+":"+field.get(null)+"\n");
                }
                ex.printStackTrace(err);
                fos.write(sw.toString().getBytes());//把sw的缓存值写到文件中
                fos.close();
                sw.close();
                err.close();
                // startService(new Intent(getApplicationContext(), AService.class));

            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }
}

