package cn.ibona.commonutil;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * @author 吴志群
 */
public class ProgressDialogFragment extends DialogFragment {

	int requestCoded;
	String title;
	String msg;
	boolean cancelable;
	
	public static ProgressDialogFragment newInstance(int requestCode, int style, String title, String msg, boolean cancelable) {
		ProgressDialogFragment dialog = new ProgressDialogFragment();
		Bundle args = new Bundle();
		args.putInt("requestCode", requestCode);
		args.putInt("style", style);
		args.putString("title", title);
		args.putString("msg", msg);
		args.putBoolean("cancelable", cancelable);
		dialog.setArguments(args);
		return dialog;
	}
	
	public static ProgressDialogFragment newInstance(int requestCode, int style, boolean cancelable) {
		return newInstance(requestCode, style, "提醒", "努力加载中...", cancelable);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestCoded = getArguments().getInt("requestCode");
		int mNum = getArguments().getInt("style");
		title = getArguments().getString("title");
		msg = getArguments().getString("msg");
		cancelable = getArguments().getBoolean("cancelable");

		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		switch (mNum%4) {
		case 0: style = DialogFragment.STYLE_NO_TITLE; break;
		case 1: style = DialogFragment.STYLE_NO_FRAME; break;
		case 2: style = DialogFragment.STYLE_NO_INPUT; break;
		case 3: style = DialogFragment.STYLE_NORMAL; break;
		}
		theme = R.style.Dialog_Transparent; 
		setStyle(style, theme);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        setCancelable(cancelable);
		return mProgressDialog;
	}
}

