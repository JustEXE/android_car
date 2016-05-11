package com.zb.mytest;

import com.zb.mytest.MyService.MyIBinder;
import com.zb.mytest.aidl.MyAIDL1Service;
import com.zb.mytest.aidl.MyAIDLService;
import com.zb.mytest.broadcast.MyBroadcastReceiver;
import com.zb.mytest.broadcast.MydtBroadcastReceiver;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
/**
 * @ClassName: MainActivity 
 *  @Description: 各种service示例
 *  @author 翟彬
 *  @date 2016年5月4日 上午11:04:12
 */
public class MainActivity extends ActionBarActivity
		implements ViewFactory, OnItemSelectedListener, OnTouchListener, OnClickListener {
	private final static String TAG = "MainActivity";
	private ImageSwitcher is;
	private Gallery gallery;

	private Integer[] mThumbIds = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, };

	private Integer[] mImageIds = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, };
	private int curPosition = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private MyIBinder service;
	private MyAIDLService aidlservice;
	private Intent i;
	private Intent iRemote;
	private Intent iAidl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		i = new Intent(this, MyService.class);// 普通service
		iRemote = new Intent(this, MyRemoteService.class);// 可以进行耗时操作的
		iAidl = new Intent(this, MyAIDL1Service.class);// aidl跨进程通信
		is = (ImageSwitcher) findViewById(R.id.switcher);
		is.setFactory(this);

		is.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		is.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		is.setHorizontalScrollBarEnabled(true);
		is.setOnTouchListener(this);

		gallery = (Gallery) findViewById(R.id.gallery);

		gallery.setAdapter(new ImageAdapter(this));
		gallery.setOnItemSelectedListener(this);
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.button4).setOnClickListener(this);
		findViewById(R.id.button5).setOnClickListener(this);
		findViewById(R.id.button6).setOnClickListener(this);
		findViewById(R.id.button7).setOnClickListener(this);
		findViewById(R.id.button8).setOnClickListener(this);
		findViewById(R.id.button9).setOnClickListener(this);
		findViewById(R.id.button10).setOnClickListener(this);
		findViewById(R.id.button11).setOnClickListener(this);
		findViewById(R.id.button12).setOnClickListener(this);
		findViewById(R.id.button13).setOnClickListener(this);
		findViewById(R.id.button14).setOnClickListener(this);
		findViewById(R.id.button15).setOnClickListener(this);
		findViewById(R.id.button16).setOnClickListener(this);
		findViewById(R.id.button17).setOnClickListener(this);
		findViewById(R.id.button18).setOnClickListener(this);
		findViewById(R.id.button19).setOnClickListener(this);
		findViewById(R.id.button20).setOnClickListener(this);
		findViewById(R.id.button21).setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			i.putExtra("i_data", "i_data");
			if (service == null) {
				bindService(i, serviceConnection, Service.BIND_AUTO_CREATE);
			} else {
				Toast.makeText(this, "服务已启动！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button2:
			if (service != null) {
				unbindService(serviceConnection);
				service = null;
			} else {
				Toast.makeText(this, "前服务为停止", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button3:
			if (service == null) {
				Toast.makeText(this, "服务为停止", Toast.LENGTH_SHORT).show();
			} else {
				service.Stop();
			}
			break;
		case R.id.button4:
			if (service == null) {
				Toast.makeText(this, "服务为停止", Toast.LENGTH_SHORT).show();
			} else {
				service.start();
			}
			break;
		case R.id.button5:
			startService(iRemote);
			break;
		case R.id.button6:
			stopService(iRemote);
			break;
		case R.id.button7:
			if (aidlservice == null) {
				bindService(iAidl, aidlConnection, Service.BIND_AUTO_CREATE);
			} else {
				Toast.makeText(this, "aidl服务已启动！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button8:
			if (aidlservice != null) {
				unbindService(aidlConnection);
				aidlservice = null;
			} else {
				Toast.makeText(this, "aidl服务为停止", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button9:
			if (aidlservice == null) {
				Toast.makeText(this, "aidl服务为停止", Toast.LENGTH_SHORT).show();
			} else {
				try {
					Log.d(TAG, "aidlservice.getCurtime()=" + aidlservice.getCurtime() + "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.button10:
			if (aidlservice == null) {
				Toast.makeText(this, "aidl服务为停止", Toast.LENGTH_SHORT).show();
			} else {
				try {
					Log.d(TAG, "aidlservice.toUpperCase(\"sdfagF_ddF\")=" + aidlservice.toUpperCase("sdfagF_ddF"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.button11:
			if (aidlservice == null) {
				Toast.makeText(this, "aidl服务为停止", Toast.LENGTH_SHORT).show();
			} else {
				try {
					Log.d(TAG, "aidlservice.startWork()");
					aidlservice.startWork();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.button12:
			if (aidlservice == null) {
				Toast.makeText(this, "aidl服务为停止", Toast.LENGTH_SHORT).show();
			} else {
				try {
					Log.d(TAG, "aidlservice.stopWork()");
					aidlservice.stopWork();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.button13:
			if (isSend) {
				Toast.makeText(this, "正在发送广播！", Toast.LENGTH_SHORT).show();
			} else {
				isSend = true;
				new Thread() {
					@Override
					public void run() {
						int a = 0;
						while (isSend) {
							Intent iSend = new Intent("com.zb.mytest.broadcast.MyBroadcastReceiver");
							iSend.putExtra("tag", "a=" + a);
							sendBroadcast(iSend);
							try {
								sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							a++;
						}
					}
				}.start();
			}
			break;
		case R.id.button14:
			isSend = false;
			break;
		case R.id.button15:
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.zb.mytest.broadcast.MydtBroadcastReceiver");
			filter.setPriority(100);
			registerReceiver(receiver, filter);
			break;
		case R.id.button16:
			unregisterReceiver(receiver);
			break;
		case R.id.button17:
			if (isSend1) {
				Toast.makeText(this, "正在发送动态注册广播！", Toast.LENGTH_SHORT).show();
			} else {
				isSend1 = true;
				new Thread() {
					@Override
					public void run() {
						int a = 0;
						while (isSend1) {
							Intent iSend = new Intent("com.zb.mytest.broadcast.MydtBroadcastReceiver");
							iSend.putExtra("tag", "a=" + a);
							sendBroadcast(iSend);
							try {
								sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							a--;
						}
					}
				}.start();
			}
			break;
		case R.id.button18:
			isSend1 = false;
			break;
		case R.id.button19:
			String i = ((TextView) findViewById(R.id.edittext1)).getText().toString().trim();
			if (!TextUtils.isEmpty(i)) {
				ContentResolver cReceiver = getContentResolver();
				ContentValues values = new ContentValues();
				values.put("user", i);
				Uri r = cReceiver.insert(Uri.parse("content://com.zb.test.contentprovider"), values);
				if (r != null) {
					Toast.makeText(this, "插入成功！", Toast.LENGTH_SHORT).show();
					((TextView) findViewById(R.id.edittext1)).postDelayed(new Runnable() {
						@Override
						public void run() {
							((TextView) findViewById(R.id.edittext1)).setText("");
						}
					}, 300);
				} else {
					Toast.makeText(this, "插入失败！", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "请输入内容！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button20:
			ContentResolver cReceiver = getContentResolver();
			Cursor c = cReceiver.query(Uri.parse("content://com.zb.test.contentprovider"), new String[] { "user" },
					null, null, null);
			StringBuilder sb = new StringBuilder();
			while (c.moveToNext()) {
				sb.append(c.getString(c.getColumnIndex("user")) + ",");
			}
			((TextView) findViewById(R.id.edittext1)).setText(sb.toString());
			break;
		case R.id.button21:
			((TextView) findViewById(R.id.edittext1)).setText("");
			break;
		}
	};

	MydtBroadcastReceiver receiver = new MydtBroadcastReceiver();
	boolean isSend = false;
	boolean isSend1 = false;
	ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Toast.makeText(MainActivity.this, "断开service", Toast.LENGTH_SHORT).show();
			service = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MainActivity.this.service = (MyIBinder) service;
			Toast.makeText(MainActivity.this, "链接service", Toast.LENGTH_SHORT).show();
			MainActivity.this.service.start();
		}
	};

	ServiceConnection aidlConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "aidlConnection.onServiceDisconnected");
			aidlservice = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "aidlConnection.onServiceConnected");
			aidlservice = (MyAIDLService) service;
		}
	};

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mThumbIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);

			i.setImageResource(mThumbIds[position]);
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			i.setBackgroundResource(R.drawable.ic_launcher);
			return i;
		}

		private Context mContext;

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		is.setImageResource(mImageIds[position]);
		curPosition = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	float downX = 0, downY = 0, upX = 0, upY = 0;
	boolean ismove = false;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v == is) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				upX = event.getX();
				upY = event.getY();
				if (ismove) {
					if (downX - upX < 0) {// 右划
						if (gallery.getSelectedItemPosition() == 0) {
							curPosition = mThumbIds.length - 1;
						} else {
							curPosition--;
						}
						gallery.setSelection(curPosition, true);
					} else if (downX - upX > 0) {// 左划
						if (gallery.getSelectedItemPosition() == mThumbIds.length - 1) {
							curPosition = 0;
						} else {
							curPosition++;
						}
						gallery.setSelection(curPosition, true);
					} else {
						Toast.makeText(this, "点击", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(this, "点击", Toast.LENGTH_LONG).show();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				ismove = true;
				break;

			default:
				break;
			}
			return true;
		} else {
			return false;
		}
	}
}
