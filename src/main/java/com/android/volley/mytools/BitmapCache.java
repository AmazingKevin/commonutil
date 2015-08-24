package com.android.volley.mytools;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import cn.ibona.commonutil.utils.W;

public class BitmapCache implements ImageCache
{

	public LruCache<String, Bitmap>	cache;
	private String tag="BitmapCache";

	/**
	 * 返回缓存类
	 * @return
	 */
	public LruCache<String, Bitmap> getCache() {
		return cache;
	}

	public void setCache(LruCache<String, Bitmap> cache) {
		this.cache = cache;
	}

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
		W.w(tag,"存放到内存.."+value.toString());
		cache.put(key, value);
	}

}
