/*************************************************************************************************
 * ��Ȩ���� (C)2015,  �ɶ���������ͨ�������޹�˾
 * 
 * �ļ����ƣ�GrayService.java
 * ����ժҪ��
 * ��ǰ�汾��
 * ��         �ߣ� �Ա�
 * ������ڣ�2016��5��4������4:31:50
 * �޸ļ�¼��
 * �޸����ڣ�
 * ��   ��  �ţ�
 * ��   ��  �ˣ�
 * �޸����ݣ�
 ************************************************************************************************/
package com.zb.mytest;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

/** @ClassName: GrayService 
 *  @Description: TODO
 *  @author �Ա�
 *  @date 2016��5��4�� ����4:31:50 
 */
public class GrayService extends Service {
	 
    private final static int GRAY_SERVICE_ID = 1001;
 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ���˷�������Ч����Notification�ϵ�ͼ��
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
 
        return super.onStartCommand(intent, flags, startId);
    }
 
    /**
     * �� API >= 18 ��ƽ̨���õĻ�ɫ�����ֶ�
     */
    public static class GrayInnerService extends Service {
 
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
 
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}

