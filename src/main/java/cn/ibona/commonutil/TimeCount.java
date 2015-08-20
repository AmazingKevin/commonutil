package cn.ibona.commonutil;

import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCount extends CountDownTimer {
    private Button btn;
    public TimeCount(Button btn, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.btn = btn;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //计时过程显示
        btn.setEnabled(false);
        btn.setText(millisUntilFinished / 1000 + "秒后重发");
    }

    @Override
    public void onFinish() {
        //计时完毕时触发
        btn.setEnabled(true);
        btn.setText("获取验证码");
    }
}
