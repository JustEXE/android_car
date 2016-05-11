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
package com.zb.mytest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @ClassName: MyService
 * @Description: TODO
 * @author �Ա�
 * @date 2016��4��15�� ����11:07:29
 */
public class MyService extends Service {
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

	private IBinder ibinder = new MyIBinder();

	@Override
	public IBinder onBind(Intent intent) {
		Toast.makeText(this, intent.getStringExtra("i_data"), Toast.LENGTH_SHORT).show();
		return ibinder;
	}

	public class MyIBinder extends Binder {
		private boolean isStop = false;
		private int a = 0;
		public void start() {
			isStop = false;
			new Thread(){
				public void run() {
					while (!isStop) {
						Log.d(TAG, "a=start"+a);
						try {
							sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						a++;
					}
					
				};
			}.start();
		}
		public void Stop() {
			Log.d(TAG, "a=Stop"+a);
			isStop = true;
		}
	}
}
