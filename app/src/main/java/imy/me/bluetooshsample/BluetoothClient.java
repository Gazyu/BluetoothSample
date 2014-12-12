package imy.me.bluetooshsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by kuwata on 14/12/12.
 */
public class BluetoothClient {
    public static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket bluetoothSocket = null;
    private BluetoothDevice bluetoothDevice = null;

    private InputStream in;
    private OutputStream out;

    private Context mContext;
    static BluetoothAdapter myClientAdapter;

    public BluetoothClient(Context context, String pcName) {
        mContext = context;
        myClientAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = myClientAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals(pcName)) {
                bluetoothDevice = device;
                break;
            }
        }
        if (bluetoothDevice == null) {
            return;
        }

        BluetoothSocket tmpSock = null;
        try {
            tmpSock = bluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothSocket = tmpSock;
    }

    public void connect() {
        try {
            bluetoothSocket.connect();
            in = bluetoothSocket.getInputStream();
            out = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) {
                e.printStackTrace();
            }
            return;
        }

    }
    public void write(byte[] buf){
        try {
            out.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}