package com.zb.mytest;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示Toast信息，便于管理Toast样式
 * 
 * @author 李加�?
 * @create-time 2015.4.10 14:01
 */

public class ToastUtil {
	private static Toast toast;
	public static void makeToast(Context context, String title) {
		if (context != null) {
			if (!TextUtils.isEmpty(title)) {
				if (toast!=null) {
					toast.cancel();
				}
				toast = android.widget.Toast.makeText(context, title, android.widget.Toast.LENGTH_SHORT);
				TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
				tv.setGravity(Gravity.CENTER_HORIZONTAL);
				toast.show();
			}
		}
	}

	public static void makeToast(Context context, int rid) {
		if (context != null) {
			if (toast!=null) {
				toast.cancel();
			}
			toast = android.widget.Toast.makeText(context, rid, android.widget.Toast.LENGTH_SHORT);
			TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			toast.show();
		}
	}

	public static void makeToast(Context context, int rid, int time) {
		if (context != null) {
			if (toast!=null) {
				toast.cancel();
			}
			toast = android.widget.Toast.makeText(context, rid, android.widget.Toast.LENGTH_SHORT);
			TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			toast.show();
		}
	}
	public static void makeToast(Context context, String rid, int time) {
		if (context != null) {
			if (toast!=null) {
				toast.cancel();
			}
			toast = android.widget.Toast.makeText(context, rid, android.widget.Toast.LENGTH_SHORT);
			TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			toast.show();
		}
	}
}
