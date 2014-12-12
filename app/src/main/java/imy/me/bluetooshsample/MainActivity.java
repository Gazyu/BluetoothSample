package imy.me.bluetooshsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    private BluetoothClient mBluetoothClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Bluetoothが使えるかどうか
        if (bluetoothAdapter.equals(null) || !bluetoothAdapter.isEnabled()) {
            return;
        }
        final ArrayList<String> bluetoothDeviceNames = new ArrayList<>();
        final Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bluetoothDevice : bluetoothDevices) {
            bluetoothDeviceNames.add(bluetoothDevice.getName());
        }
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bluetoothDeviceNames);
        ListView listView = (ListView) findViewById(R.id.bluetooth_list);
        listView.setAdapter(stringArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deviceName = bluetoothDeviceNames.get(position);
                BluetoothClient bluetoothClient = new BluetoothClient(getApplicationContext(), deviceName);
                bluetoothClient.connect();
            }
        });
    }

    private void connectServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
