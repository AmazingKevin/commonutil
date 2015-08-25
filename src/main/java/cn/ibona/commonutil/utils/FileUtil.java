package cn.ibona.commonutil.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Kevin on 2015/8/25.
 */
public class FileUtil {

    public static Context mContext;


    /**
     * 将bitmap图片根据url写入到缓存中去
     * @param bitmap 数据
     * @param url 名字
     */
    public static  boolean putBitmap2LocalCache(Bitmap bitmap, String url)
    {
        String name;
        FileOutputStream fos = null;
        try
        {
            name =  Md5Util.encode(url);
            File file = new File(getCacheDir(), name);
            fos = new FileOutputStream(file);

            return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                    fos = null;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 根据url从本地缓存中获取数据
     * @param url
     * @return
     */
    public static Bitmap getBitmapFromLocalCache(String url)
    {
        String name;
        try
        {
            name =  Md5Util.encode(url);
            File file = new File(getCacheDir(), name);

            if (file.exists())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                return bitmap;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存文字到SD卡
     * @param filename 文件名
     * @param filecontent 文件内容
     *
     */
    public static void writeWord2SDCard(String filename, String filecontent)
    {

        String sdDir = getSDcardDir();

        if(sdDir!=null)
        {
            File file = new File(Environment.getExternalStorageDirectory(),filename);
            FileWriter fw = null;
            try {
                fw = new FileWriter(file);
                fw.write(filecontent );
                fw.flush();
                fw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 返回缓存目录的地址
     * @return 私有数据的硬盘缓存目录
     */
    public static String getCacheDir()
    {
        return mContext.getCacheDir().getAbsolutePath();
    }

    /**
     * 返回sd卡目录 / 包名
     * @return 返回目录 返回null表示没有sdcard
     */
    public static String getSDcardDir()
    {
        if(Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED)
        {
            return Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+mContext.getPackageName();
        }

        return null;
    }

}
