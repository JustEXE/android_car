/*************************************************************************************************
 * 版权所有 (C)2015,  成都市商联汇通技术有限公司
 * 
 * 文件名称：MyService.java
 * 内容摘要：
 * 当前版本：
 * 作         者： 翟彬
 * 完成日期：2016年4月15日上午11:07:29
 * 修改记录：
 * 修改日期：
 * 版   本  号：
 * 修   改  人：
 * 修改内容：
 ************************************************************************************************/
package com.zb.mytest.aidl;

import com.zb.mytest.aidl.MyAIDLService;
import com.zb.mytest.aidl.MyAIDLService.Stub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

/**
 * @ClassName: MyService
 * @Description: TODO
 * @author 翟彬
 * @date 2016年4月15日 上午11:07:29
 */
public class MyAIDL1Service extends Service {
	private static final String TAG = "MyService";

	/*
	 * (非 Javadoc) <p>Title: onCreate</p> <p>Description: </p>
	 * 
	 * @see android.app.Service#onCreate()
	 */

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
	}

	/*
	 * (非 Javadoc) <p>Title: onStartCommand</p> <p>Description: </p>
	 * 
	 * @param intent
	 * 
	 * @param flags
	 * 
	 * @param startId
	 * 
	 * @return
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * (非 Javadoc) <p>Title: onDestroy</p> <p>Description: </p>
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	private MyAIDLService.Stub mBind = new Stub() {
		private boolean isStop = true;
		private int a = 0;

		@Override
		public String toUpperCase(String str) throws RemoteException {
			return str.toUpperCase();
		}

		@Override
		public long getCurtime() throws RemoteException {
			return System.currentTimeMillis();
		}

		@Override
		public void startWork() throws RemoteException {
			if (!isStop) {
				Log.d(TAG, "前线程不停止！");
				return;
			}
			isStop = false;
			new Thread() {
				public void run() {
					while (!isStop) {
						Log.d(TAG, "" + a);
						a++;
						try {
							sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			}.start();
		}

		@Override
		public void stopWork() throws RemoteException {
			isStop = true;
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBind;
	}

}
