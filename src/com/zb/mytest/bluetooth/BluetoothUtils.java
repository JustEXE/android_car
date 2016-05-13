/*************************************************************************************************
 * ��Ȩ���� (C)2015,  �ɶ���������ͨ�������޹�˾
 * 
 * �ļ����ƣ�ClsUtils.java
 * ����ժҪ��
 * ��ǰ�汾��
 * ��         �ߣ� �Ա�
 * ������ڣ�2016��5��6������2:34:38
 * �޸ļ�¼��
 * �޸����ڣ�
 * ��   ��  �ţ�
 * ��   ��  �ˣ�
 * �޸����ݣ�
 ************************************************************************************************/
package com.zb.mytest.bluetooth;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

/**
 * @ClassName: ClsUtils
 * @Description: TODO
 * @author �Ա�
 * @date 2016��5��6�� ����2:34:38
 */
public class BluetoothUtils {
	private static final String TAG = "BluetoothUtils";

	/**
	 * ���豸��� �ο�Դ�룺platform/packages/apps/Settings.git
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	public static boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
		Method createBondMethod = btClass.getMethod("createBond");
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	/**
	 * ���豸������ �ο�Դ�룺platform/packages/apps/Settings.git
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	static public boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
		Method removeBondMethod = btClass.getMethod("removeBond");
		Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	static public boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
		try {
			Method removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[] { byte[].class });
			Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice, new Object[] { str.getBytes() });
			Log.e("returnValue", "" + returnValue);
		} catch (SecurityException e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	// ȡ���û�����
	static public boolean cancelPairingUserInput(Class btClass, BluetoothDevice device)

			throws Exception {
		Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
		// cancelBondProcess()
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}

	// ȡ�����
	static public boolean cancelBondProcess(Class btClass, BluetoothDevice device)

			throws Exception {
		Method createBondMethod = btClass.getMethod("cancelBondProcess");
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}

	/**
	 * Construct a BluetoothSocket.
	 * 
	 * @param type
	 *            type of socket
	 * @param fd
	 *            fd to use for connected socket, or -1 for a new socket
	 * @param auth
	 *            require the remote device to be authenticated
	 * @param encrypt
	 *            require the connection to be encrypted
	 * @param device
	 *            remote device that this socket can connect to
	 * @param port
	 *            remote port
	 * @param uuid
	 *            SDP uuid
	 */
	public static BluetoothSocket createBluetoothSocket(BluetoothDevice device, int port, String uuid) {
		Class[] d = new Class[] { int.class, int.class, boolean.class, boolean.class, BluetoothDevice.class, int.class,
				ParcelUuid.class };
		try {
			Constructor anim = BluetoothSocket.class.getDeclaredConstructor(d);
			anim.setAccessible(true);
			return (BluetoothSocket) anim.newInstance(1, -1, false, false, device, port,
					new ParcelUuid(UUID.fromString(uuid)));
			// return new BluetoothSocket(1, -1, false, false, device, -1, new
			// ParcelUuid(uuid));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param clsShow
	 */
	static public void printAllInform(Class clsShow) {
		try {
			// ȡ�����з���
			Method[] hideMethod = clsShow.getMethods();
			int i = 0;
			for (; i < hideMethod.length; i++) {
				Log.e("method name", hideMethod[i].getName() + ";and the i is:" + i);
			}
			// ȡ�����г���
			Field[] allFields = clsShow.getFields();
			for (i = 0; i < allFields.length; i++) {
				Log.e("Field name", allFields[i].getName());
			}
		} catch (SecurityException e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static public BluetoothDevice pair(String strAddr, String strPsw) {
		BluetoothDevice remoteDevice = null;
		// boolean result = false;
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		bluetoothAdapter.cancelDiscovery();

		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
		}

		if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // ���������ַ�Ƿ���Ч

			Log.d("mylog", "devAdd un effient!");
		}

		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);

		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			try {
				Log.d("mylog", "NOT BOND_BONDED");
				BluetoothUtils.setPin(device.getClass(), device, strPsw); // �ֻ��������ɼ������
				BluetoothUtils.createBond(device.getClass(), device);
				remoteDevice = device; // �����ϾͰ�����豸���󴫸�ȫ�ֵ�remoteDevice
				// result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block

				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			} //

		} else {
			Log.d("mylog", "HAS BOND_BONDED");
			try {
				BluetoothUtils.createBond(device.getClass(), device);
				BluetoothUtils.setPin(device.getClass(), device, strPsw); // �ֻ��������ɼ������
				BluetoothUtils.createBond(device.getClass(), device);
				remoteDevice = device; // ����󶨳ɹ�����ֱ�Ӱ�����豸���󴫸�ȫ�ֵ�remoteDevice
				// result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			}
		}
		return remoteDevice;
	}

	public static final int TYPE_RFCOMM = 1;
	public static final int TYPE_SCO = 2;
	public static final int TYPE_L2CAP = 3;

	// address must use upper case
	public static final BluetoothSocket createBluetoothSocket(int type, int fd, boolean auth, boolean encrypt,
			String address, int port) throws IOException {
		BluetoothSocket socket = null;
		try {
			Constructor<BluetoothSocket> cs = BluetoothSocket.class.getDeclaredConstructor(int.class, int.class,
					boolean.class, boolean.class, String.class, int.class);
			if (!cs.isAccessible()) {
				cs.setAccessible(true);
			}
			socket = cs.newInstance(type, fd, auth, encrypt, address, port);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return socket;
	}
}
