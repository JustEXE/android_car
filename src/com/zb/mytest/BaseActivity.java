package com.zb.mytest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentView());
		initView();
		initData();
		// SystemBarTintUtil.initSystemBar(this, ll_main, R.color.color_e12926);
	}

	/**
	 * 设置布局文件
	 */
	protected abstract int getContentView();

	/**
	 * 设置找控�?,并设置头�?,并设置监听事�?,以及获取Intent中的数据
	 */
	protected abstract void initView();

	/**
	 * 初始化数�?
	 */
	protected abstract void initData();

	/**
	 * 设置点击
	 */
	public abstract void onClick(View v);

	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}

	protected TextView findTextView(int id) {
		return (TextView) findViewById(id);
	}

	protected EditText findEditTextView(int id) {
		return (EditText) findViewById(id);
	}

	protected ImageView findImageView(int id) {
		return (ImageView) findViewById(id);
	}

	protected Button findButton(int id) {
		return (Button) findViewById(id);
	}

	protected void setListener(View... v) {
		if (v != null) {
			for (View view : v) {
				view.setOnClickListener(this);
			}
		}
	}

	protected void makeToast(final int stringId) {
		runOnUiThread(new Runnable() {
			public void run() {
				ToastUtil.makeToast(BaseActivity.this, stringId);
			}
		});
	}

	protected void makeToast(final String string) {
		runOnUiThread(new Runnable() {
			public void run() {
				ToastUtil.makeToast(BaseActivity.this, string);
			}
		});
	}

	protected void makeToast(final int stringId, final int time) {
		runOnUiThread(new Runnable() {
			public void run() {
				ToastUtil.makeToast(BaseActivity.this, stringId, time);
			}
		});
	}

	protected void makeToast(final String stringId, final int time) {
		runOnUiThread(new Runnable() {
			public void run() {
				ToastUtil.makeToast(BaseActivity.this, stringId, time);
			}
		});
	}

	protected void setViewEnable(boolean enable, View... views) {
		if (views != null && views.length != 0) {
			for (View view : views) {
				view.setEnabled(enable);
			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent�?
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事�?
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
}
