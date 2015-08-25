package cn.ibona.commonutil.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import cn.ibona.commonutil.utils.W;

/**
 * Created by Kevin on 2015/8/24.
 * 改造一个request,将gson集成进来,将获得的数据解析成java对象
 */
public class GsonRequest<T> extends Request<T> {

    private String TAG="GsonRequest";

    private   Class<T> mClazz;
    private  Response.Listener<T> mListener;
    private  Response.ErrorListener mErrorListener;

    public GsonRequest(String url, Class clazz,Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(Method.GET, url,errorListener );

        mListener=listener;
        mErrorListener=errorListener;
        mClazz = clazz;

    }

    /**
     * 解析数据
     * @param response Response from the network
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        try {
            String json = new String(response.data, "utf-8");
            W.w(TAG,"解析到数据:"+json);
            return  Response.success(new Gson().fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }

    }

    /**
     * 传递未成功的数据
     * @param error Error details
     */
    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);

        if (mErrorListener != null) {
            mErrorListener.onErrorResponse(error);
        } else {
            W.w(TAG, "获得错误数据" + error.getMessage());
        }
    }

    /**
     * 传递成功解析的数据
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }

    }
}
