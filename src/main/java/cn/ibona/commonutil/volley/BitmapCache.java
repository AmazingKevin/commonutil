package cn.ibona.commonutil.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache
{

	public LruCache<String, Bitmap>	cache;
	public int						max	= 10 * 1024 * 1024;

	public BitmapCache()
	{
		cache = new LruCache<String, Bitmap>(max)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				return value.getRowBytes()*value.getHeight() ;
			}

		};
	}

	@Override
	public Bitmap getBitmap(String key)
	{
		return cache.get(key);
	}

	@Override
	public void putBitmap(String key, Bitmap value)
	{
		cache.put(key, value);
	}

}
