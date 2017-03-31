/*************************************************************************************************
 * ��Ȩ���� (C)2015,  �ɶ���������ͨ�������޹�˾
 * 
 * �ļ����ƣ�BluetoothActivity.java
 * ����ժҪ��
 * ��ǰ�汾��
 * ��         �ߣ� �Ա�
 * ������ڣ�2016��5��6������9:00:49
 * �޸ļ�¼��
 * �޸����ڣ�
 * ��   ��  �ţ�
 * ��   ��  �ˣ�
 * �޸����ݣ�
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
 * @author �Ա�
 * @date 2016��5��6�� ����9:00:49
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

		// IntentFilter����һ��������,ֻ�з��Ϲ�������Intent�Żᱻ���ǵ�BluetoothReceiver������
		IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// ����һ��BluetoothReceiver����
		bluetoothReceiver = new BluetoothReceiver();
		// ע��㲥������ ע�����ÿ�η��͹㲥��BluetoothReceiver�Ϳ��Խ��յ�����㲥��
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
							builder = new AlertDialog.Builder(BluetoothActivity.this).setCancelable(true).setTitle("��Ϣ")
									.setMessage("�Ƿ�ȡ�����").setNegativeButton("��", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											try {
												BluetoothDevice dBluetoothDevice = devices.get(position);
												BluetoothUtils.removeBond(BluetoothDevice.class, dBluetoothDevice);
												makeToast("ȡ���ɹ���");
											} catch (Exception e) {
												e.printStackTrace();
												makeToast("ȡ��ʧ�ܣ�");
											}
										}
									}).setPositiveButton("��", null);
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
			makeToast("û������");
		}
	}

	private void scanBluetooth() {
		makeToast("��ʼɨ��");
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
			makeToast("δѡ�������豸��");
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
			makeToast("��ʼ����");

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
				makeToast("���ӳɹ���");
				try {
					mmInStream = bluetoothSocket.getInputStream();
					mmOutStream = bluetoothSocket.getOutputStream();
					int i = 0;
					while (isSend) {
						byte[] buffer = ("������Ϣ������" + ++i).getBytes();
						makeToast("������Ϣ������" + i);
						try {
							mmOutStream.write(buffer);
							mmOutStream.flush();
						} catch (IOException e) {
							makeToast("���Ӷ�ʧ");
							break;
						}
						sleep(3000);
						makeToast("���ӽ���");
					}
					bluetoothSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				makeToast("����ʧ�ܣ�");
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
			((TextView) convertView.findViewById(android.R.id.text1)).setText("�豸���ƣ�" + device.getName());

			String text2 = "�豸��ַ��" + device.getAddress();
			if (device.getUuids() != null && device.getUuids().length > 0 && device.getUuids()[0] != null) {
				text2 += "\n�豸UUID��" + device.getUuids()[0].getUuid();
			}

			((TextView) convertView.findViewById(android.R.id.text2)).setText(text2);
			return convertView;
		}

	}

	private void upStstus() {
		textString = "�������ƣ�" + bluetoothAdapter.getName() + "\n������ַ��" + bluetoothAdapter.getAddress() + "\n����״̬��"
				+ bluetoothAdapter.isEnabled();
		textview.setText(textString);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:// ��������
			bluetoothAdapter.enable();
			break;
		case R.id.button2:// �ر�����
			bluetoothAdapter.disable();
			break;
		case R.id.button3:// ��ƥ��
			alrede();
			break;
		case R.id.button4:// ɨ��
			scanBluetooth();
			break;
		case R.id.button5:// ȡ������
			endSend();
			break;
		case R.id.button6:// ��ʼ
			startSend();
			break;

		default:
			break;
		}
	}

	// ���չ㲥
	private class BluetoothReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// ֻҪBluetoothReceiver���յ�������ϵͳ�Ĺ㲥,����㲥��ʲô��,�����ҵ���һ��Զ�������豸
				// Intent����ոշ���Զ�������豸�������Ķ���,���Դ��յ���Intent����ȡ��һЩ��Ϣ
				BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Log.d("mytag", bluetoothDevice.getAddress());
				System.out.println(bluetoothDevice.getAddress());
				if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
					devices.add(bluetoothDevice);
					blueadater.notifyDataSetChanged();
				}
			}
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				makeToast("������ϣ�");
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
