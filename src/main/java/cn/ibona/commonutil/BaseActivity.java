package cn.ibona.commonutil;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * 基础activity， 所有的Activity都要继承自这个基类
 */
public abstract class BaseActivity extends FragmentActivity {


	//////////////////////////////////////////////////////////////////////////////// 退出所有activity
	public static String EXIT_BROADCAST_ACTION="exit_all";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(mLoggedOutReceiver, new IntentFilter(
				EXIT_BROADCAST_ACTION));
    }
	private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};

	@Override
	protected void onDestroy() {
		unregisterReceiver(mLoggedOutReceiver);
		super.onDestroy();
	}
	//////////////////////////////////////////////////////////////////////////////// 退出所有activity

	//获得类名
	protected String mTypeName;
	public String getTypeName() {
		return getClass().getSimpleName();
	}
	//////////////////////////////////////////////////////////////////////////////// 获得类名

	/////////////////	/////////////////	/////////////////	/////////////////	/////////////////	/////////////////	/////////////////
	//进度条
	/**
	 * 显示DialogFragment
	 * 
	 * @param dialog
	 *            需要显示的DialogFragment
	 * @param single
	 *            是否只显示一个
	 * @author wzq
	 */
	protected void showDialog(DialogFragment dialog, boolean single) {
		FragmentManager manager = getFragmentManager();
		Fragment prev = manager.findFragmentByTag("dialog");
		if (single && prev != null)
			return;

		FragmentTransaction ft = manager.beginTransaction();
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		dialog.show(ft, "dialog");
	}

	/**
	 * 显示DialogFragment，相当于调用showDialog(dialog, false);
	 * @param dialog
	 *            需要显示的DialogFragment
	 * @author wzq
	 */
	protected void showDialog(DialogFragment dialog) {
		showDialog(dialog, false);
	}

	/**
	 * 结束栈中所有的dialog
	 *
	 * @author wzq
	 */
	protected void finishDialog() {
		getFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	/**
	 * 显示进度条dialog，主要用于进行长时间操作时的提醒，如网络请求<br/>
	 * 
	 * @param cancelable
	 *            是否可取消
	 * @author wzq
	 */
	public void showProgressDialog(boolean cancelable) {
		Fragment prev = getFragmentManager().findFragmentByTag(
				"progress");
		if (prev != null)
			return;

		DialogFragment dialog = ProgressDialogFragment.newInstance(0, 0,cancelable);
		dialog.show(getFragmentManager(), "progress");
	}

	/**
	 * 显示进度条dialog，主要用于进行长时间操作时的提醒，如网络请求<br/>
	 * 相当于调用showProgressDialog(true)
	 * 
	 * @author wzq
	 */
	public void showProgressDialog() {
		showProgressDialog(true);
	}

	/**
	 * 隐藏进度条dialog
	 * @author wzq
	 */
	public void dismissProgressDialog() {
		ProgressDialogFragment dialog = (ProgressDialogFragment) getFragmentManager()
				.findFragmentByTag("progress");
		if (dialog != null)
			dialog.dismiss();
	}

	/////////////////	/////////////////	/////////////////	/////////////////	/////////////////	/////////////////	/////////////////	/////////////////

}
