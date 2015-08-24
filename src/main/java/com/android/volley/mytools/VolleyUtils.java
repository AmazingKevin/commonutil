package com.android.volley.mytools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.ibona.commonutil.utils.W;

public class VolleyUtils
{
	private static RequestQueue queues;
	private static Context mContext;

	private static String tag="VolleyUtils";
	private static Map<String,Bitmap> mCache;
	//private static Map<Integer,String> mUrls;

//	private static LruCache<String,Bitmap> mCache;
//private static	ImageLoader loader;
//private static	BitmapCache bitmapCache;
	private VolleyUtils(){}


	public static void initVolley(Context context)
	{
		mContext=context;
		queues=Volley.newRequestQueue(mContext);
		mCache=new HashMap<String,Bitmap>();
		//mUrls=new HashMap<Integer,String>();

		//bitmapCache = new BitmapCache();
		//loader = new ImageLoader(queues,bitmapCache);
	}

	/**
	 * get请求
	 * @param url 地址
	 * @param tag 线程tag
	 * @param vif 接口
	 */
	public static void simpleGet4String(String url,String tag,VolleyInterface vif)
	{
		queues.cancelAll(tag);
		StringRequest request=	new StringRequest(Method.GET, url, vif.loadListener(), vif.loadErrorListener());
		request.setTag(tag);
		queues.add(request);
		queues.start();
	}

	/**
	 * post请求
	 * @param url 地址
	 * @param tag 线程tag
	 * @param map 参数映射
	 * @param vif 接口
	 */
	public static void simplePost4String(String url,String tag,final Map<String,String> map,VolleyInterface vif)
	{
		queues.cancelAll(tag);
		StringRequest request=	new StringRequest( url, vif.loadListener(), vif.loadErrorListener())
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				return map;
			}

		};
		request.setTag(tag);
		queues.add(request);
		queues.start();
	}

	/**
	 * 使用Gson封装成对象返回来的
	 * @param url 地址
	 * @param tag 标记
	 * @param clazz clazz
	 * @param listener 成功的监听
	 * @param errorListener 失败的监听
	 * @param <T> 泛型
	 */
	public static <T> void simpleGet4Gson(String url,String tag,Class clazz,Response.Listener<T> listener,Response.ErrorListener errorListener)
	{
		queues.cancelAll(tag);
		GsonRequest<T> gsonRequest = new GsonRequest<>(url, clazz, listener, errorListener);
		gsonRequest.setTag(tag);
		queues.add(gsonRequest);
		queues.start();
	}

	/**
	 * 用于展示图片
	 * @param iv imageView传入的控件
	 * @param url 传入的地址
	 * @param errorImg 默认失败图片
	 */
	public static void simpleGet4Image(final int position,final ImageView iv,final String url, final int errorImg)
	{
		//乱闪,放入内
		iv.setImageResource(errorImg);
		//mUrls.put(position, url);

		Bitmap bitmap=null;
		//假如内存中存在 直接加载内存中的数据
		bitmap = mCache.get(url);
		if(bitmap!=null)
		{
			W.w(tag,"有内存:"+bitmap);
			iv.setImageBitmap(bitmap);
			//不再需要加载了
			return;
			 /*String tagUrl = mUrls.get(position);
			if(!TextUtils.isEmpty(tagUrl) && tagUrl.equals(url))
			{
				iv.setImageBitmap(bitmap);
				//不再需要加载了
				return;
			}*/

		}

		//假如硬盘中存在 加载硬盘中的数据

		//到网络中请求
		ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap response) {
				mCache.put(url,response);
				//递归
				simpleGet4Image(position ,iv , url, errorImg);
			}
		}, 300, 200, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//提示
				W.w(tag,"网络失败原因:"+error.getMessage());
				iv.setImageResource(errorImg);
			}
		});

		queues.add(imageRequest);
		queues.start();

		//最后访问网络数据
		//ImageLoader.ImageListener listener = loader.getImageListener(iv, defImg, errorImg);
		//loader.get(url, listener);
	}


	/**
	 * 需要与activity或者fragment的生命周期进行绑定,不然会报错
	 * @param tag
	 */
	public static void cancelDownloadByTag(String tag)
	{
		queues.cancelAll(tag);
	}



}
