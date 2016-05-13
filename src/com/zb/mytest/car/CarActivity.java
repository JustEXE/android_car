/*************************************************************************************************
 * ��Ȩ���� (C)2015,  �ɶ���������ͨ�������޹�˾
 * 
 * �ļ����ƣ�CarActivity.java
 * ����ժҪ��
 * ��ǰ�汾��
 * ��         �ߣ� �Ա�
 * ������ڣ�2016��5��11������9:30:42
 * �޸ļ�¼��
 * �޸����ڣ�
 * ��   ��  �ţ�
 * ��   ��  �ˣ�
 * �޸����ݣ�
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
 * @Description: С��ң����Ŀ
 * @author �Ա�
 * @date 2016��5��11�� ����9:30:42
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
				// makeToast("��ʼ��ԡ�����");
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
		case R.id.button1:// ��ǰ
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "1");
			myLv2Adapter.add("��������ǰ");
			break;
		case R.id.button2:// ǰ��
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "2");
			myLv2Adapter.add("������ǰ��");
			break;
		case R.id.button3:// ��ǰ
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "3");
			myLv2Adapter.add("��������ǰ");
			break;
		case R.id.button4:// ��ת
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "4");
			myLv2Adapter.add("��������ת");
			break;
		case R.id.button5:// ����
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "5");
			myLv2Adapter.add("����������");
			break;
		case R.id.button6:// ��ת
			if (!checkCanControl()) {
				return;
			}
			acceptThreadControl(MSG_SEND, "6");
			myLv2Adapter.add("��������ת");
			break;
		case R.id.button7:// ɨ��
			makeToast("��ʼɨ�裡");
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
		case R.id.button8:// ���Զ�
			if (acceptThread == null) {
				makeToast("δ�������񣬿����С�����");
				acceptThread = new AcceptThread();
				acceptThread.start();
				return;
			}
			if (inputStream == null || outputStream == null) {
				makeToast("û���������豸�����Ժ�");
				return;
			}
			if ("�ֶ�".equals(bt8.getText().toString().trim())) {
				acceptThreadControl(MSG_SEND, "5a");
				makeToast("��ʼ�Զ���ʻ�ɹ�");
				myLv2Adapter.add("��������ʼ�Զ���ʻ");
				bt8.setText("�Զ�");
				setViewEnable(false, bt1, bt2, bt3, bt4, bt5, bt6);
			} else {
				acceptThreadControl(MSG_SEND, "5b");
				makeToast("�ر��ֶ���ʻ�ɹ�");
				myLv2Adapter.add("�������ر��ֶ���ʻ");
				bt8.setText("�ֶ�");
				setViewEnable(true, bt1, bt2, bt3, bt4, bt5, bt6);
			}
			break;
		case R.id.button9:// �����
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
		case R.id.button10:// ��������
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
			makeToast("δ�������񣬿����С�����");
			acceptThread = new AcceptThread();
			acceptThread.start();
			return false;
		}
		if (inputStream == null || outputStream == null) {
			makeToast("û���������豸�����Ժ�");
			return false;
		}
		if ("�Զ�".equals(bt8.getText().toString().trim())) {
			makeToast("�Զ���ʻ�У����Ҫ�ֶ������뿪���ֶ���");
			return false;
		}
		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SETPIN_FAIL:
				makeToast("���ʧ�ܣ�");
				break;
			case MSG_READ:
				myLv2Adapter.add("������" + (String) msg.obj);
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
				makeToast("ɨ����ϣ�");
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
					BluetoothUtils.setPin(btDevice.getClass(), btDevice, STRPIN); // �ֻ��������ɼ������
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
			Log.i(TAG, "��ʼ�����߳�");
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
				makeToast("���������ɹ���");
				socket = serverSocket.accept();
				makeToast("�豸��" + socket.getRemoteDevice().getName() + "���ӳɹ���");
				Log.i(TAG, "�豸��" + socket.getRemoteDevice().getName() + "���ӳɹ���");
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
						Log.i(TAG, "���ܵ������ݣ�" + (String) msg.obj);
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
				makeToast("���Ӷ�ʧ");
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
				makeToast("�������ӣ�");
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
				makeToast("���Ӷ�ʧ");
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
			((TextView) convertView.findViewById(android.R.id.text1)).setText("�豸���ƣ�" + device.getName());
			String text2 = "�豸��ַ��" + device.getAddress();
			if (device.getUuids() != null && device.getUuids().length > 0 && device.getUuids()[0] != null) {
				text2 += "\n�豸UUID��" + device.getUuids()[0].getUuid();
			}
			((TextView) convertView.findViewById(android.R.id.text2)).setText(text2);
			return convertView;
		}
	}

}
