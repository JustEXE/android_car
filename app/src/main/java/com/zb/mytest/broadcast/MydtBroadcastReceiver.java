/*************************************************************************************************
 * ��Ȩ���� (C)2015,  �ɶ���������ͨ�������޹�˾
 * 
 * �ļ����ƣ�MyBroadcastReceiver.java
 * ����ժҪ��
 * ��ǰ�汾��
 * ��         �ߣ� �Ա�
 * ������ڣ�2016��4��15������2:46:59
 * �޸ļ�¼��
 * �޸����ڣ�
 * ��   ��  �ţ�
 * ��   ��  �ˣ�
 * �޸����ݣ�
 ************************************************************************************************/
package com.zb.mytest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/** @ClassName: MyBroadcastReceiver 
 *  @Description: TODO
 *  @author �Ա�
 *  @date 2016��4��15�� ����2:46:59 
 */
public class MydtBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "MydtBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, intent.getStringExtra("tag"));
		Log.d(TAG, intent.getAction());
//		Toast.makeText(context, intent.getStringExtra("tag"), Toast.LENGTH_SHORT).show();
	}
}

