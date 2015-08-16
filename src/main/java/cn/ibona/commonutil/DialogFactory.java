package cn.ibona.commonutil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;

/**
 * Created by Kevin on 2015/8/16.
 */
public class DialogFactory {

    public static final int NO_ICON = 0x00000;//没有图标
    private static AlertDialog mDialog;


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //todo 简单对话框
    public static void newSimpleDialog(Activity activity, int style, String title, String msg, int iconImg, String posi, String negative, String neutral, final DialogSimpleListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, style);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title); //设置标题
        }

        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg); //设置内容
        }

        if (iconImg != NO_ICON) {
            builder.setIcon(iconImg);//设置图标，图片id即可
        }

        if (!TextUtils.isEmpty(posi)) {
            builder.setPositiveButton(posi, new DialogInterface.OnClickListener() { //设置确定按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //关闭dialog
                    listener.clickPosition();
                }
            });
        }


        if (!TextUtils.isEmpty(negative)) {
            if (negative != null) {
                builder.setNegativeButton(negative, new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.clickNegative();
                    }
                });
            }

        }


        if (!TextUtils.isEmpty(neutral)) {
            builder.setNeutralButton(neutral, new DialogInterface.OnClickListener() {//设置忽略按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    listener.clickNeutral();
                }
            });
        }


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }


    public interface DialogSimpleListener {
        void clickPosition();

        void clickNegative();

        void clickNeutral();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//todo 单选
    public static void newSingleChooseDialog(Activity activity, int style, String title, String msg, int iconImg, int defaulChoose, String[] items, final DialogSingleListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, style);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title); //设置标题
        }

        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg); //设置内容
        }

        if (iconImg != NO_ICON) {
            builder.setIcon(iconImg);//设置图标，图片id即可
        }

        builder.setSingleChoiceItems(items, defaulChoose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.clickWhick(which);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public interface DialogSingleListener {
        void clickWhick(int which);
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //todo 多选
    public static void newMultiChooseDialog(Activity activity, int style, String title, String msg,String posi,String negative, int iconImg, boolean[] defaultChoose, String[] items, final MultiChooseListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, style);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title); //设置标题
        }

        if (iconImg != NO_ICON) {
            builder.setIcon(iconImg);//设置图标，图片id即可
        }

        builder.setMultiChoiceItems(items, defaultChoose,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton,
                                        boolean isChecked) {

                        listener.clickWhick(whichButton, isChecked);

                    }
                });

        if (!TextUtils.isEmpty(posi)) {
            builder.setPositiveButton(posi, new DialogInterface.OnClickListener() { //设置确定按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //关闭dialog
                    listener.clickPostive();
                }
            });
        }


        if (!TextUtils.isEmpty(negative)) {
            if (negative != null) {
                builder.setNegativeButton(negative, new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        listener.clickNegative();
                    }
                });
            }

        }
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public interface MultiChooseListener {
        void clickWhick(int which, boolean isCheck);
        void clickPostive();
        void clickNegative();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
