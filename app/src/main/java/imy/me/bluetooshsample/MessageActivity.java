package imy.me.bluetooshsample;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class MessageActivity extends ActionBarActivity implements BluetoothServer.BluetoothEvent {

    public static final String RECEIVE_KEY_MODE = "RECEIVE_KEY_MODE";
    public static final String RECEIVE_KEY_SERVER_NAME = "RECEIVE_KEY_SERVER_NAME";
    public static final int MODE_SERVER = 0;
    public static final int MODE_CLIENT = 1;

    ArrayAdapter<String> mAdapter;
    public static InputStream in;
    public static OutputStream out;

    private boolean waitFlag = false;

    EditText mEditText;
    Button mButton;

    BluetoothMethods mBluetoothMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mEditText = (EditText) findViewById(R.id.send_text);
        mButton = (Button) findViewById(R.id.send_button);

        mEditText.setEnabled(false);
        mButton.setEnabled(false);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendText = mEditText.getText().toString();
                if (!TextUtils.isEmpty(sendText)) {
                    write(sendText);
                    mEditText.setText("");
                }
            }
        });
        ListView listView = (ListView) findViewById(R.id.list);

        ArrayList<String> strings = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(mAdapter);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getInt(RECEIVE_KEY_MODE) == MODE_SERVER) {
            mBluetoothMethods = new BluetoothServer(getApplicationContext(), MessageActivity.this);
        } else {
            mBluetoothMethods = new BluetoothClient(getApplicationContext(), bundle.getString(RECEIVE_KEY_SERVER_NAME), MessageActivity.this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopReadThread();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mBluetoothMethods.disconnect();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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

    @Override
    public void onConnected(BluetoothSocket receivedSocket) {
        try {
            in = receivedSocket.getInputStream();
            out = receivedSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startReadThread();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setEnabled(true);
                mButton.setEnabled(true);
            }
        });
    }

    private void write(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (out == null) {
                    return;
                }
                try {
                    out.write(message.getBytes("UTF-8"));
                    out.flush();
                } catch (IOException e) {
                }
            }
        }).start();
    }

    private void startReadThread() {
        waitFlag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buf = new byte[1024];
                int tmpBuf = 0;
                while (waitFlag) {
                    try {
                        tmpBuf = in.read(buf);
                    } catch (IOException e) {
                        continue;
                    }
                    if (tmpBuf != 0) {
                        addComment(new String(buf));
                        tmpBuf = 0;
                    }

                }
            }
        }).start();
    }

    private void stopReadThread() {
        waitFlag = false;
    }

    synchronized void addComment(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.add(message);
            }
        });
    }

}
