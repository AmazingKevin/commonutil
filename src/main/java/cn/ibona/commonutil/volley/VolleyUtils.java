package cn.ibona.commonutil.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import cn.ibona.commonutil.utils.FileUtil;
import cn.ibona.commonutil.utils.W;

public class VolleyUtils
{
	private static VolleyUtils instance=new VolleyUtils();
	private static RequestQueue queues;
	private static Context mContext;

	private static String tag="VolleyUtils";

//	 private static Map<String,Bitmap> mLocalCache;

	private static LruCache<String,Bitmap> mLocalCache;

 	private static	ImageLoader loader;
 	private static	BitmapCache bitmapCache;


	private VolleyUtils(){}


	public static void initVolley(Context context)
	{
		mContext=context;
		queues=Volley.newRequestQueue(mContext);

		//imageLoader
		 bitmapCache = new BitmapCache();
		 loader = new ImageLoader(queues,bitmapCache);

		//本地缓存
		//mLocalCache=new HashMap<String,Bitmap>();
		mLocalCache=new LruCache<String,Bitmap>(1024*1024*30)
		{
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};

		queues.start();
	}

	/**
	 *
	 * @return
	 */
	public static VolleyUtils getInstance() {
		return instance;
	}
	/**
	 * get请求
	 * @param url 地址
	 * @param tag 线程tag
	 * @param vif 接口
	 */
	public  void simpleGet4String(String url,String tag,VolleyInterface vif)
	{

		queues.cancelAll(tag);
		StringRequest request=	new StringRequest(Method.GET, url, vif.loadListener(), vif.loadErrorListener());
		request.setTag(tag);
		queues.add(request);

	}

	/**
	 * post请求
	 * @param url 地址
	 * @param tag 线程tag
	 * @param map 参数映射
	 * @param vif 接口
	 */
	public  void simplePost4String(String url,String tag,final Map<String,String> map,VolleyInterface vif)
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
		//queues.start();
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
	public  <T> void simpleGet4Gson(String url,String tag,Class clazz,Response.Listener<T> listener,Response.ErrorListener errorListener)
	{
		queues.cancelAll(tag);
		GsonRequest<T> gsonRequest = new GsonRequest<>(url, clazz, listener, errorListener);
		gsonRequest.setTag(tag);
		queues.add(gsonRequest);
		 
	}

	/**
	 * 用于展示图片
	 * @param iv imageView传入的控件
	 * @param url 传入的地址
	 * @param errorImg 默认失败图片
	 */
	public  void simpleGet4Image( final NetworkImageView iv,final String url, final int errorImg)
	{

		W.w(tag, "图片url:" + url);
		iv.setDefaultImageResId(errorImg);

		//todo 网络中加载数据
		iv.setImageUrl(url,loader);

	}


	/**
	 * 可以缩放的加载
	 * @param iv 展示的imageView
	 * @param url url地址(会保存到缓存中)
	 * @param errorImg (默认图片)
	 * @param wid (宽度 单位是dp)
	 * @param height (高度 单位是dp)
	 */
	public  void simpleGet4Image(final ImageView iv,final  String url,final   int errorImg,final int wid, final int height)
	{

		W.w(tag, "url:" + url);
		//乱闪,放入内

		iv.setImageResource(errorImg);

		Bitmap bitmap=null;
		//假如内存中存在 直接加载内存中的数据
		//	bitmap = mCache.get(url);
		bitmap=mLocalCache.get(url);
		if(bitmap!=null)
		{
			W.w(tag, "有内存:" + bitmap);
			//空的 设定tag,表示已经加载过数据
			iv.setImageBitmap(bitmap);
			return;
		}


		//todo 假如硬盘中存在 加载硬盘中的数据
		bitmap=FileUtil.getBitmapFromLocalCache(url);
		if(bitmap!=null)
		{
			W.w(tag,"从硬盘缓存中获取到了数据");
			mLocalCache.put(url, bitmap);
			iv.setImageBitmap(bitmap);
			return;
		}


		//到网络中请求
		ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap response) {

				//内存
				mLocalCache.put(url, response);

				//硬盘
				FileUtil.putBitmap2LocalCache(response,url);

				//递归
				simpleGet4Image(iv, url, errorImg, wid, height);


			}
		}, W.dip2px(wid), W.dip2px(height), Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//提示
				W.w(tag,"网络失败原因:"+error.getMessage());
				iv.setImageResource(errorImg);
			}
		});

	    queues.add(imageRequest);

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
