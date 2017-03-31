/*************************************************************************************************
 * ��Ȩ���� (C)2015,  �ɶ���������ͨ�������޹�˾
 * 
 * �ļ����ƣ�MyService.java
 * ����ժҪ��
 * ��ǰ�汾��
 * ��         �ߣ� �Ա�
 * ������ڣ�2016��4��15������11:07:29
 * �޸ļ�¼��
 * �޸����ڣ�
 * ��   ��  �ţ�
 * ��   ��  �ˣ�
 * �޸����ݣ�
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
 * @author �Ա�
 * @date 2016��4��15�� ����11:07:29
 */
public class MyAIDL1Service extends Service {
	private static final String TAG = "MyService";

	/*
	 * (�� Javadoc) <p>Title: onCreate</p> <p>Description: </p>
	 * 
	 * @see android.app.Service#onCreate()
	 */

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
	}

	/*
	 * (�� Javadoc) <p>Title: onStartCommand</p> <p>Description: </p>
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
	 * (�� Javadoc) <p>Title: onDestroy</p> <p>Description: </p>
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
				Log.d(TAG, "ǰ�̲߳�ֹͣ��");
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
