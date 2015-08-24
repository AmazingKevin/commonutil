package com.android.volley.mytools;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public abstract class VolleyInterface
{

	private Listener<String>	mListener;
	private ErrorListener		mErrorListener;

	public VolleyInterface()
	{
	}

	public Listener<String> loadListener()
	{
		mListener = new Listener<String>() {
			@Override
			public void onResponse(String resutl)
			{
				onSuccess(resutl);
			}
		};
		return mListener;
	}

	public ErrorListener loadErrorListener()
	{
		mErrorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error)
			{
				onFailed(error);
			}
		};
		return mErrorListener;
	}

	public abstract void onSuccess(String resutl);

	public abstract void onFailed(VolleyError error);

}
