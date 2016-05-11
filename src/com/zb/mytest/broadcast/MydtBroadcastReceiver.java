/*************************************************************************************************
 * 版权所有 (C)2015,  成都市商联汇通技术有限公司
 * 
 * 文件名称：MyBroadcastReceiver.java
 * 内容摘要：
 * 当前版本：
 * 作         者： 翟彬
 * 完成日期：2016年4月15日下午2:46:59
 * 修改记录：
 * 修改日期：
 * 版   本  号：
 * 修   改  人：
 * 修改内容：
 ************************************************************************************************/
package com.zb.mytest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/** @ClassName: MyBroadcastReceiver 
 *  @Description: TODO
 *  @author 翟彬
 *  @date 2016年4月15日 下午2:46:59 
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

