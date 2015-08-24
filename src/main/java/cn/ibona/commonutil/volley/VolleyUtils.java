package cn.ibona.commonutil.volley;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class VolleyUtils
{
	private static RequestQueue queues;
	private static Context mContext;
	
	private VolleyUtils(){}
	public static void initVolley(Context context)
	{
		mContext=context;
		queues=Volley.newRequestQueue(mContext);
	}

	/**
	 * get请求
	 * @param url 地址
	 * @param tag 线程tag
	 * @param vif 接口
	 */
	public static void stringGetRequest(String url,String tag,VolleyInterface vif)
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
	public static void stringPostRequest(String url,String tag,final Map<String,String> map,VolleyInterface vif)
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
	public static <T> void GsonGetRequest(String url,String tag,final Map<String,String> map,VolleyInterface vif,Class mClass)
	{
		queues.cancelAll(tag);
		/*JsonRequest<T> request=new JsonRequest<T>() {
			@Override
			protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
				return null;
			}
		};*/




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
	 * 用于展示图片
	 * @param iv imageView传入的控件
	 * @param url 传入的地址
	 * @param defImg 默认图片
	 * @param errorImg 默认失败图片
	 */
	public static void display(ImageView iv,String url,int defImg,int errorImg)
	{
		ImageLoader loader=new ImageLoader(queues,new BitmapCache());
		ImageLoader.ImageListener listener = loader.getImageListener(iv, defImg, errorImg);
		loader.get(url, listener);
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
