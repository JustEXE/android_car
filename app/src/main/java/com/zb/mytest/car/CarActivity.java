/*************************************************************************************************
 * 版权所有 (C)2015,  成都市商联汇通技术有限公司
 * 
 * 文件名称：CarActivity.java
 * 内容摘要：
 * 当前版本：
 * 作         者： 翟彬
 * 完成日期：2016年5月11日上午9:30:42
 * 修改记录：
 * 修改日期：
 * 版   本  号：
 * 修   改  人：
 * 修改内容：
 ************************************************************************************************/
package com.zb.mytest.car;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.zb.mytest.BaseActivity;
import com.zb.mytest.R;
import com.zb.mytest.bluetooth.BluetoothActivity;
import com.zb.mytest.bluetooth.BluetoothUtils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @ClassName: CarActivity
 * @Description: 小车遥控项目
 * @author 翟彬
 * @date 2016年5月11日 上午9:30:42
 */
public class CarActivity extends BaseActivity {
	private static final String TAG = "CarActivity";
	private static final int SERVICE_NO = 1;
	private static final int SERVICE_OK = 2;
	private static final int SERVICE_FAILE = 3;
	private static final int SERVICE_ING = 4;
	private int cur_server_status = SERVICE_NO;
	private static final String STRPIN = "000000";
	private static final int MSG_SETPIN_FAIL = 0;
	private static final int MSG_READ = 1;
	private static final int MSG_RESTART_SERVER_THREAD = 3;
	private static final int MSG_SEND = 4;
	private static final int MSG_STARTSERVER = 5;

	private static final UUID MY_UUID_SECURE = UUID.fromString("FA87C0D0-AFAC-11DE-8A39-0800200C9A66");
	private Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt10;
	private ListView lv1;
	private ListView lv2;
	private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private ArrayAdapter<String> myLv2Adapter;

	@Override
	protected int getContentView() {
		return R.layout.activity_car;
	}

	@Override
	protected void initView() {
		bt1 = findButton(R.id.button1);
		bt2 = findButton(R.id.button2);
		bt3 = findButton(R.id.button3);
		bt4 = findButton(R.id.button4);
		bt5 = findButton(R.id.button5);
		bt6 = findButton(R.id.button6);
		bt7 = findButton(R.id.button7);
		bt8 = findButton(R.id.button8);
		bt9 = findButton(R.id.button9);
		bt10 = findButton(R.id.button10);
		lv1 = findView(R.id.listview1);
		lv2 = findView(R.id.listview2);
		myBluetoothAdapter = new MyBluetoothAdapter(devices);
		lv1.setAdapter(myBluetoothAdapter);
		lv1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// makeToast("开始配对、连接");
				// startConnectThread(devices.get(position));
				makeToast(devices.get(position).getName() + "\n" + devices.get(position).getAddress());
			}
		});
		myLv2Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		lv2.setAdapter(myLv2Adapter);
		setListener(bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt10);
		setViewEnable(false, bt1, bt2, bt3, bt4, bt5, bt6, bt8);
	}

	@Override
	protected void initData() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:// 左前
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "1");
			myLv2Adapter.add("操作：左前");
			break;
		case R.id.button2:// 前进
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "2");
			myLv2Adapter.add("操作：前进");
			break;
		case R.id.button3:// 右前
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "3");
			myLv2Adapter.add("操作：右前");
			break;
		case R.id.button4:// 左转
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "4");
			myLv2Adapter.add("操作：左转");
			break;
		case R.id.button5:// 后退
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "5");
			myLv2Adapter.add("操作：后退");
			break;
		case R.id.button6:// 右转
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "6");
			myLv2Adapter.add("操作：右转");
			break;
		case R.id.button7:// 扫描
			makeToast("开始扫描！");
			if (lv1.getVisibility() == View.GONE) {
				lv1.setVisibility(View.VISIBLE);
				lv2.setVisibility(View.GONE);
			}
			devices.clear();
			bluetoothAdapter.enable();
			myBluetoothAdapter.notifyDataSetChanged();
			bluetoothAdapter.cancelDiscovery();
			bluetoothAdapter.startDiscovery();
			break;
		case R.id.button8:// 手自动
			if (acceptThread == null) {
				makeToast("未启动服务，开启中···");
				acceptThread = new AcceptThread();
				acceptThread.start();
				return;
			}
			if (inputStream == null || outputStream == null) {
				makeToast("没有连接上设备，请稍后！");
				return;
			}
			if ("手动".equals(bt8.getText().toString().trim())) {
				acceptThreadControl(MSG_SEND, "5a");
				makeToast("开始自动驾驶成功");
				myLv2Adapter.add("操作：开始自动驾驶");
				bt8.setText("自动");
				setViewEnable(false, bt1, bt2, bt3, bt4, bt5, bt6);
			} else {
				acceptThreadControl(MSG_SEND, "5b");
				makeToast("关闭手动驾驶成功");
				myLv2Adapter.add("操作：关闭手动驾驶");
				bt8.setText("手动");
				setViewEnable(true, bt1, bt2, bt3, bt4, bt5, bt6);
			}
			break;
		case R.id.button9:// 已配对
			if (lv1.getVisibility() == View.GONE) {
				lv1.setVisibility(View.VISIBLE);
				lv2.setVisibility(View.GONE);
			}
			devices.clear();
			Set<BluetoothDevice> devices1 = bluetoothAdapter.getBondedDevices();
			for (Iterator iterator = devices1.iterator(); iterator.hasNext();) {
				BluetoothDevice bluetoothDevice = (BluetoothDevice) iterator.next();
				devices.add(bluetoothDevice);
			}
			// myBluetoothAdapter.setDevices(devices);
			myBluetoothAdapter.notifyDataSetChanged();
			break;
		case R.id.button10:// 重新搜索
			myLv2Adapter.clear();
			if (lv2.getVisibility() == View.GONE) {
				lv1.setVisibility(View.GONE);
				lv2.setVisibility(View.VISIBLE);
			}
			if (!bluetoothAdapter.isEnabled()) {
				bluetoothAdapter.enable();
			}
			setViewEnable(false, bt1, bt2, bt3, bt4, bt5, bt6, bt8);
			if (acceptThread != null) {
				acceptThreadControl(MSG_SEND, "end");
			} else {
				acceptThread = new AcceptThread();
				acceptThread.start();
			}
			break;

		default:
			break;
		}
	}

	private void acceptThreadControl(int what, String string) {
		if (acceptThreadHandler != null) {
			Message msg = acceptThreadHandler.obtainMessage();
			msg.what = what;
			msg.obj = string;
			acceptThreadHandler.sendMessage(msg);
		}
	}

	private boolean checkCanControl() {
		if (lv2.getVisibility() == View.GONE) {
			lv1.setVisibility(View.GONE);
			lv2.setVisibility(View.VISIBLE);
		}
		if (acceptThread == null) {
			makeToast("未启动服务，开启中···");
			acceptThread = new AcceptThread();
			acceptThread.start();
			return false;
		}
		if (inputStream == null || outputStream == null) {
			makeToast("没有连接上设备，请稍后！");
			return false;
		}
		if ("自动".equals(bt8.getText().toString().trim())) {
			makeToast("自动驾驶中！如果要手动操作请开启手动！");
			return false;
		}
		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SETPIN_FAIL:
				makeToast("配对失败！");
				break;
			case MSG_READ:
				myLv2Adapter.add("反馈：" + (String) msg.obj);
				// makeToast((String) msg.obj);
				break;
			case MSG_RESTART_SERVER_THREAD:
				if (acceptThread != null) {
					acceptThreadControl(MSG_SEND, "cancle");
				}
				acceptThread = null;
				inputStream = null;
				outputStream = null;
				acceptThread = new AcceptThread();
				acceptThread.start();
				acceptThreadControl(MSG_STARTSERVER, "");
				break;
 
			default:
				break;
			}
		};
	};
	private AcceptThread acceptThread;
	private BluetoothAdapter bluetoothAdapter;
	private MyBluetoothAdapter myBluetoothAdapter;
	private BluetoothFoundReceiver bluetoothFoundReceiver;

	private class BluetoothFoundReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
				makeToast("扫描完毕！");
			} else if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				devices.add(device);
				myBluetoothAdapter.notifyDataSetChanged();
			} else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(intent.getAction())) {
				BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				// byte[] pinBytes = BluetoothDevice.convertPinToBytes("1234");
				// device.setPin(pinBytes);
				Log.i("tag11111", "ddd");
				try {
					BluetoothUtils.setPin(btDevice.getClass(), btDevice, STRPIN); // 手机和蓝牙采集器配对
					BluetoothUtils.createBond(btDevice.getClass(), btDevice);
					BluetoothUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void onStart() {
		if (acceptThread == null) {
			acceptThread = new AcceptThread();
			acceptThread.start();
			acceptThreadControl(MSG_STARTSERVER, "");
		}
		bluetoothFoundReceiver = new BluetoothFoundReceiver();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
		registerReceiver(bluetoothFoundReceiver, filter);
		super.onStart();
	}

	@Override
	protected void onPause() {
		if (bluetoothFoundReceiver != null) {
			unregisterReceiver(bluetoothFoundReceiver);
		}
		super.onPause();
	}

	private InputStream inputStream;
	private OutputStream outputStream;
	private Handler acceptThreadHandler;

	private class AcceptThread extends Thread {
		private BluetoothServerSocket serverSocket;

		private boolean isRead = true;
		private BluetoothSocket socket;

		public AcceptThread() {
			Log.i(TAG, "开始服务线程");
		}

		public void run() {
			Looper.prepare();
			acceptThreadHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case MSG_SEND:
						if ("end".equals((String) msg.obj)) {
							cancle();
						}
						sendMsg((String) msg.obj);
						break;
					case MSG_STARTSERVER:
						startServer();
						break;
					}
				}
			};
			Looper.loop();
		}

		private void startServer() {
			Log.i(TAG, "ServerThread=" + Thread.currentThread().getId());
			bluetoothAdapter.cancelDiscovery();
			try {
				serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("CarService",
						MY_UUID_SECURE);
				makeToast("服务启动成功！");
				socket = serverSocket.accept();
				makeToast("设备：" + socket.getRemoteDevice().getName() + "连接成功！");
				Log.i(TAG, "设备：" + socket.getRemoteDevice().getName() + "连接成功！");
				runOnUiThread(new Runnable() {
					public void run() {
						setViewEnable(true, bt1, bt2, bt3, bt4, bt5, bt6, bt8);
					}
				});
				sleep(1500);
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				byte[] buffer = new byte[1024 * 2];
				while (isRead) {
					int n = inputStream.read(buffer);
					if (n > 0) {
						Message msg = handler.obtainMessage();
						msg.what = MSG_READ;
						msg.obj = new String(buffer, 0, n);
						Log.i(TAG, "接受到的数据：" + (String) msg.obj);
						handler.sendMessage(msg);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				runOnUiThread(new Runnable() {
					public void run() {
						setViewEnable(false, bt1, bt2, bt3, bt4, bt5, bt6, bt8);
					}
				});
				if (serverSocket != null) {
					try {
						serverSocket.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				makeToast("连接丢失");
				acceptThread = null;
				inputStream = null;
				outputStream = null;
				acceptThreadHandler = null;
				if (!isRead) {
					handler.sendEmptyMessage(MSG_RESTART_SERVER_THREAD);
				}
			}
		}

		private void sendMsg(String mString) {
			Log.i(TAG, "WriteThread=" + Thread.currentThread().getId());
			if ("end".equals(mString)) {
				isRead = false;
			}
			if (outputStream == null) {
				makeToast("正在连接！");
				return;
			}
			try {
				outputStream.write(mString.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						setViewEnable(false, bt1, bt2, bt3, bt4, bt5, bt6, bt8);
					}
				});
				if (serverSocket != null) {
					try {
						serverSocket.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				makeToast("连接丢失");
				acceptThread = null;
				inputStream = null;
				outputStream = null;
				acceptThreadHandler = null;
				isRead = false;
				handler.sendEmptyMessage(MSG_RESTART_SERVER_THREAD);
			}
		}

		public void cancle() {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			acceptThread = null;
			inputStream = null;
			outputStream = null;
			isRead = false;
			acceptThreadHandler = null;
		}

	};

	class MyBluetoothAdapter extends BaseAdapter {
		private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

		public MyBluetoothAdapter(ArrayList<BluetoothDevice> devices) {
			this.devices = devices;
		}

		public void setDevices(ArrayList<BluetoothDevice> devices) {
			this.devices = devices;
		}

		@Override
		public int getCount() {
			return devices.size();
		}

		@Override
		public Object getItem(int position) {
			return devices.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(CarActivity.this).inflate(android.R.layout.simple_list_item_2, null);
			BluetoothDevice device = devices.get(position);
			((TextView) convertView.findViewById(android.R.id.text1)).setText("设备名称：" + device.getName());
			String text2 = "设备地址：" + device.getAddress();
			if (device.getUuids() != null && device.getUuids().length > 0 && device.getUuids()[0] != null) {
				text2 += "\n设备UUID：" + device.getUuids()[0].getUuid();
			}
			((TextView) convertView.findViewById(android.R.id.text2)).setText(text2);
			return convertView;
		}
	}

}
