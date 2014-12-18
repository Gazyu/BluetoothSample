package imy.me.bluetooshsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;


public class SelectConnectBluetoothDeviceActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_connect_bluetooth_device);


        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
                Intent intent = new Intent(SelectConnectBluetoothDeviceActivity.this, MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MessageActivity.RECEIVE_KEY_MODE, MessageActivity.MODE_CLIENT);
                bundle.putString(MessageActivity.RECEIVE_KEY_SERVER_NAME, deviceName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_connect_bluetooth_device, menu);
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
