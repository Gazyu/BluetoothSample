package imy.me.bluetooshsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by k.k.base on 14/12/12.
 */
public class BluetoothServer implements BluetoothMethods{


    public interface BluetoothEvent {
        public void onConnected(BluetoothSocket receivedSocket);
    }

    public static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothServerSocket mBluetoothServerSocket;
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;

    private BluetoothEvent mBluetoothEvent;

    public BluetoothServer(Context context, BluetoothEvent bluetoothEvent) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            mBluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothSample", SPP_UUID);
        } catch (IOException e) {
            return;
        }
        mBluetoothEvent = bluetoothEvent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothSocket bluetoothSocket = null;
                while (true) {
                    try {
                        bluetoothSocket = mBluetoothServerSocket.accept();
                    } catch (IOException e) {
                        break;
                    }

                    if (bluetoothSocket != null) {
                        mBluetoothEvent.onConnected(bluetoothSocket);
                        try {
                            mBluetoothServerSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }).start();
    }

    @Override
    public void disconnect() {
        try {
            mBluetoothServerSocket.close();
        } catch (IOException e) {
        }
    }
}
