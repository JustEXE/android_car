/*************************************************************************************************
 * 版权所有 (C)2015,  成都市商联汇通技术有限公司
 * 
 * 文件名称：BluetoothActivity.java
 * 内容摘要：
 * 当前版本：
 * 作         者： 翟彬
 * 完成日期：2016年5月6日上午9:00:49
 * 修改记录：
 * 修改日期：
 * 版   本  号：
 * 修   改  人：
 * 修改内容：
 ************************************************************************************************/
package com.zb.mytest.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.zb.mytest.R;
import com.zb.mytest.ToastUtil;
import com.zb.mytest.R.id;
import com.zb.mytest.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @ClassName: BluetoothActivity
 * @Description: TODO
 * @author 翟彬
 * @date 2016年5月6日 上午9:00:49
 */
public class BluetoothActivity extends Activity implements OnClickListener {
	private static final String TAG = "BluetoothActivity";
	private BluetoothAdapter bluetoothAdapter;
	private TextView textview;
	private String textString;
	private ListView listview;
	private BluetoothReceiver bluetoothReceiver;
	private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private BlueAdapter blueadater;
	private BluetoothLeScanner leScanner;
	private int type;
	// Unique UUID for this application
	private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
	private ConnectThead conThead;
	private BluetoothDevice currDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.button4).setOnClickListener(this);
		findViewById(R.id.button5).setOnClickListener(this);
		findViewById(R.id.button6).setOnClickListener(this);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		textview = (TextView) findViewById(R.id.text1);
		listview = (ListView) findViewById(R.id.listview1);
		blueadater = new BlueAdapter(devices);
		listview.setAdapter(blueadater);
		// leScanner = bluetoothAdapter.getBluetoothLeScanner();

		// IntentFilter它是一个过滤器,只有符合过滤器的Intent才会被我们的BluetoothReceiver所接收
		IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// 创建一个BluetoothReceiver对象
		bluetoothReceiver = new BluetoothReceiver();
		// 注册广播接收器 注册完后每次发送广播后，BluetoothReceiver就可以接收到这个广播了
		registerReceiver(bluetoothReceiver, intentFilter);

		alrede();
		upStstus();
		listview.setOnItemClickListener(new OnItemClickListener() {
			private Builder builder;

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				if (position < devices.size()) {
					final BluetoothDevice device = devices.get(position);
					switch (type) {
					case 1:
						makeToast(device.getName() + "\n" + device.getAddress());
						if (builder == null) {
							builder = new AlertDialog.Builder(BluetoothActivity.this).setCancelable(true).setTitle("消息")
									.setMessage("是否取消配对").setNegativeButton("是", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											try {
												BluetoothDevice dBluetoothDevice = devices.get(position);
												BluetoothUtils.removeBond(BluetoothDevice.class, dBluetoothDevice);
												makeToast("取消成功！");
											} catch (Exception e) {
												e.printStackTrace();
												makeToast("取消失败！");
											}
										}
									}).setPositiveButton("否", null);
						}
						builder.create().show();
						break;
					case 2:
						if (conThead != null) {
							conThead.setSend(false);
							conThead = null;
						}
						conThead = new ConnectThead(device);
						currDevice = device;
						conThead.start();
						break;

					default:
						break;
					}
				}
			}
		});
	}

	private void alrede() {
		type = 1;
		bluetoothAdapter.cancelDiscovery();
		devices.clear();
		blueadater.notifyDataSetChanged();
		if (bluetoothAdapter != null) {
			if (bluetoothAdapter.isEnabled()) {
				Set<BluetoothDevice> devices1 = bluetoothAdapter.getBondedDevices();
				for (Iterator<BluetoothDevice> iterator = devices1.iterator(); iterator.hasNext();) {
					BluetoothDevice bluetoothDevice = (BluetoothDevice) iterator.next();
					devices.add(bluetoothDevice);
				}
				blueadater.notifyDataSetChanged();
			} else {
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivity(intent);
			}
		} else {
			makeToast("没有蓝牙");
		}
	}

	private void scanBluetooth() {
		makeToast("开始扫描");
		type = 2;
		devices.clear();
		blueadater.notifyDataSetChanged();
		bluetoothAdapter.cancelDiscovery();
		bluetoothAdapter.startDiscovery();
	}

	private void endSend() {
		conThead.setSend(false);
	}

	private void startSend() {
		if (currDevice == null) {
			makeToast("未选择蓝牙设备！");
			return;
		}
		if (conThead != null) {
			conThead.setSend(false);
			conThead = null;
		}
		conThead = new ConnectThead(currDevice);
		conThead.start();
	}

	private class ConnectThead extends Thread {
		BluetoothSocket bluetoothSocket = null;
		private InputStream mmInStream;
		private OutputStream mmOutStream;
		private BluetoothDevice device;
		private boolean isSend = true;

		public ConnectThead(BluetoothDevice device) {
			this.device = device;
		}

		public void run() {
			makeToast("开始链接");

			try {
				// if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
				bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
				// } else {
				//
				// }
				bluetoothSocket.connect();
			} catch (IOException e) {
				bluetoothSocket = null;
				e.printStackTrace();
			}
			if (bluetoothSocket != null) {
				makeToast("链接成功！");
				try {
					mmInStream = bluetoothSocket.getInputStream();
					mmOutStream = bluetoothSocket.getOutputStream();
					int i = 0;
					while (isSend) {
						byte[] buffer = ("发送消息次数：" + ++i).getBytes();
						makeToast("发送消息次数：" + i);
						try {
							mmOutStream.write(buffer);
							mmOutStream.flush();
						} catch (IOException e) {
							makeToast("链接丢失");
							break;
						}
						sleep(3000);
						makeToast("链接结束");
					}
					bluetoothSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				makeToast("链接失败！");
			}
		};

		public void setSend(boolean isSend) {
			this.isSend = isSend;
		}
	};

	class BlueAdapter extends BaseAdapter {
		private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

		public BlueAdapter(ArrayList<BluetoothDevice> devices) {
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
			convertView = LayoutInflater.from(BluetoothActivity.this).inflate(android.R.layout.simple_list_item_2,
					null);
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

	private void upStstus() {
		textString = "蓝牙名称：" + bluetoothAdapter.getName() + "\n蓝牙地址：" + bluetoothAdapter.getAddress() + "\n蓝牙状态："
				+ bluetoothAdapter.isEnabled();
		textview.setText(textString);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:// 开启蓝牙
			bluetoothAdapter.enable();
			break;
		case R.id.button2:// 关闭蓝牙
			bluetoothAdapter.disable();
			break;
		case R.id.button3:// 已匹配
			alrede();
			break;
		case R.id.button4:// 扫描
			scanBluetooth();
			break;
		case R.id.button5:// 取消发送
			endSend();
			break;
		case R.id.button6:// 开始
			startSend();
			break;

		default:
			break;
		}
	}

	// 接收广播
	private class BluetoothReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 只要BluetoothReceiver接收到来自于系统的广播,这个广播是什么呢,是我找到了一个远程蓝牙设备
				// Intent代表刚刚发现远程蓝牙设备适配器的对象,可以从收到的Intent对象取出一些信息
				BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Log.d("mytag", bluetoothDevice.getAddress());
				System.out.println(bluetoothDevice.getAddress());
				if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
					devices.add(bluetoothDevice);
					blueadater.notifyDataSetChanged();
				}
			}
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				makeToast("搜索完毕！");
				unregisterReceiver(bluetoothReceiver);
			}
		}
	}

	protected void makeToast(final int stringId) {
		runOnUiThread(new Runnable() {
			public void run() {
				ToastUtil.makeToast(BluetoothActivity.this, stringId);
			}
		});
	}

	protected void makeToast(final String string) {
		runOnUiThread(new Runnable() {
			public void run() {
				ToastUtil.makeToast(BluetoothActivity.this, string);
			}
		});
	}

	protected void makeToast(final int stringId, final int time) {
		runOnUiThread(new Runnable() {
			public void run() {
				ToastUtil.makeToast(BluetoothActivity.this, stringId, time);
			}
		});
	}

	protected void makeToast(final String stringId, final int time) {
		runOnUiThread(new Runnable() {
			public void run() {
				ToastUtil.makeToast(BluetoothActivity.this, stringId, time);
			}
		});
	}

}
