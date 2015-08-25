package cn.ibona.commonutil.volley;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import java.util.HashMap;
import java.util.Map;

import cn.ibona.commonutil.utils.W;

public class BitmapCache implements ImageCache
{

	//public LruCache<String, Bitmap>	cache;
	private String tag="BitmapCache";



	private Map<String,Bitmap> cache;

/*	public Bitmap getCacheBitmap(String url) {
		return cache.get(url);
	}
	public void putCacheBitmap(Bitmap bitmap,String url) {
		  cache.put(url,bitmap);
	}*/
	/*public LruCache<String, Bitmap> getCache() {
		return cache;
	}

	public void setCache(LruCache<String, Bitmap> cache) {
		this.cache = cache;
	}*/

	//public int						max	= 10 * 1024 * 1024;

	public BitmapCache()
	{
		cache=new HashMap<String,Bitmap>();
		/*cache = new LruCache<String, Bitmap>(max)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getByteCount() ;
			}
		};*/
	}




	@Override
	public Bitmap getBitmap(String key)
	{
		W.w(tag,"拿出内存.."+key);
		return cache.get(key);
	}

	@Override
	public void putBitmap(String key, Bitmap value)
	{
		if(value!=null)
		{
			W.w(tag,"存放到内存.value."+value.toString());
			cache.put(key, value);
/*
			boolean b = FileUtil.putBitmap2LocalCache(value, key);
			W.w(tag,"存到cache中是否成功"+b);*/
		}

	}

}
