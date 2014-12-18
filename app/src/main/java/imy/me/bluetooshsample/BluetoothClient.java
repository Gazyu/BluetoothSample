package imy.me.bluetooshsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by k.k.base on 14/12/12.
 */
public class BluetoothClient implements BluetoothMethods {
    public static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket mBluetoothSocket = null;
    private BluetoothDevice mBluetoothDevice = null;

    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothServer.BluetoothEvent mBluetoothEvent;

    public BluetoothClient(Context context, String pcName, BluetoothServer.BluetoothEvent bluetoothEvent) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals(pcName)) {
                mBluetoothDevice = device;
                break;
            }
        }
        if (mBluetoothDevice == null) {
            return;
        }

        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            return;
        }
        mBluetoothEvent = bluetoothEvent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mBluetoothSocket.connect();
                    mBluetoothEvent.onConnected(mBluetoothSocket);
                } catch (IOException e) {
                }
            }
        }).start();
    }

    @Override
    public void disconnect() {
        try {
            mBluetoothSocket.close();
        } catch (IOException e) {
        }
    }
}