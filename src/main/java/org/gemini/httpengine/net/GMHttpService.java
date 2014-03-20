package org.gemini.httpengine.net;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.gemini.httpengine.listener.OnResponseListener;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class GMHttpService {

	private static final int CORE_POOL_SIZE = 3;
	private static final int MAXIMUM_POOL_SIZE = 128;
	private static final int KEEP_ALIVE = 1;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
			10);

	public static final String TAG = "GMHttpService";
	public static final String VERSION = "1.2";

	private static GMHttpService sInstance;
	private final WeakHashMap<Context, List<GMHttpRequest>> requestMap;

	/***
	 * per thread has a {@link GMHttpEngine}
	 */
	private final ThreadLocal<GMHttpEngine> sHttpEnginePool = new ThreadLocal<GMHttpEngine>();

	private final Executor mService;

	private final Handler.Callback mResponseCallBack;

	public static void enableCache(Context context) throws IOException {
		File httpCacheDir = new File(context.getCacheDir(), "http");
		long httpCacheSize = 10 * 1024 * 1024; // 10 MB
		HttpResponseCache.install(httpCacheDir, httpCacheSize);
	}

	public static void flushCache() {
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	/**
	 * default thread that handler run on
	 */
	private final HandlerThread mHandlerThread;
	private Handler mCallbackHandler;

	public GMHttpService() {
		mService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
				KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);// Executors.newCachedThreadPool();
		mResponseCallBack = new ResponseDataCallback();
		requestMap = new WeakHashMap<Context, List<GMHttpRequest>>();

		mHandlerThread = new HandlerThread(TAG + "-HandlerThread");
		mHandlerThread.start();
		mCallbackHandler = new Handler(mHandlerThread.getLooper(),
				mResponseCallBack);
	}

	/***
	 * Set the callback looper for response. Just set it to parse response on
	 * the right thread
	 * 
	 * @param looper
	 */
	public void setCallbackLooper(Looper looper) {
		this.mCallbackHandler = new Handler(looper, mResponseCallBack);
	}

	/***
	 * Single Instance
	 * 
	 * @return
	 */
	public static synchronized GMHttpService getInstance() {
		if (sInstance == null) {
			makeInstance();
		}
		return sInstance;
	}

	public static synchronized void makeInstance() {
		sInstance = new GMHttpService();
	}

	/**
	 * receive the data of the runnable and response to the listener
	 * 
	 * @author GeminiWen
	 * 
	 */
	private static class ResponseDataCallback implements Handler.Callback {
		@Override
		public boolean handleMessage(Message msg) {
			ResponseUpdater runnable = (ResponseUpdater) msg.obj;
			runnable.updateResponse();
			return true;
		}
	}

	interface ResponseUpdater {
		public void updateResponse();
	}

	private class HttpRunnable implements Runnable, ResponseUpdater {

		private final GMHttpRequest mHttpRequest;
		private GMHttpResponse mHttpResponse;

		public HttpRunnable(GMHttpRequest httpRequest) {
			mHttpRequest = httpRequest;
		}

		@Override
		public void run() {
			if (mHttpRequest.isCancel()) {
				requestMap.remove(mHttpRequest.getContext());
				return;
			}
			GMHttpEngine httpEngine = sHttpEnginePool.get();
			if (httpEngine == null) {
				httpEngine = new GMHttpEngine();
				sHttpEnginePool.set(httpEngine);
			}

			// execute request and get response
			byte[] resultData = httpEngine.openUrl(mHttpRequest);
			mHttpResponse = new GMHttpResponse(resultData);

			Message msg = mCallbackHandler.obtainMessage(0, this);
			msg.sendToTarget();
		}

		@Override
		public void updateResponse() {
			requestMap.remove(mHttpRequest.getContext());
			if (mHttpRequest.isCancel()) {
				return;
			}
			OnResponseListener l = mHttpRequest.getResponseListener();
			if (null != l) {
				l.onResponse(mHttpResponse, mHttpRequest);
			}
		}
	}

	/***
	 * Execute http request asynchronous and get the response
	 * 
	 * @param httpRequest
	 *            the http request object
	 */
	public void executeHttpMethod(GMHttpRequest httpRequest) {
		Runnable runnable = new HttpRunnable(httpRequest);
		List<GMHttpRequest> httpRequestList = requestMap.get(httpRequest
				.getContext());
		if (null == httpRequestList) {
			httpRequestList = new ArrayList<GMHttpRequest>();
			httpRequestList.add(httpRequest);
			requestMap.put(httpRequest.getContext(), httpRequestList);
		} else {
			httpRequestList.add(httpRequest);
		}
		mService.execute(runnable);	
	}

	/***
	 * Cancel request
	 * 
	 * @param httpRequest
	 *            the request to cancel
	 */
	public void cancelRequest(GMHttpRequest httpRequest) {
		cancelRequest(httpRequest.getContext());
	}

	public void cancelRequest(Context context) {
		List<GMHttpRequest> requestList = requestMap.remove(context);
		if (null != requestList) {
			for (GMHttpRequest request : requestList) {
				request.cancel();
			}
		}

	}

}
