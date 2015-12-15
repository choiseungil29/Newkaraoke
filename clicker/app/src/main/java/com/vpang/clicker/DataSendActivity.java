package com.vpang.clicker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vpang.clicker.wifi_direct.WiFiDirectActivity;
import com.vpang.clicker.wifi_direct.config.Configuration;
import com.vpang.clicker.wifi_direct.router.AllEncompasingP2PClient;
import com.vpang.clicker.wifi_direct.router.MeshNetworkManager;
import com.vpang.clicker.wifi_direct.router.Packet;
import com.vpang.clicker.wifi_direct.router.Sender;
import com.vpang.clicker.wifi_direct.ui.DeviceListFragment.DeviceActionListener;
import com.vpang.clicker.wifi_direct.wifi.WiFiBroadcastReceiver;
import com.vpang.clicker.wifi_direct.wifi.WiFiDirectBroadcastReceiver;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DataSendActivity extends Activity implements ChannelListener, DeviceActionListener {

    @Bind(R.id.edit_data)
    EditText editText;
    @Bind(R.id.btn_send_data)
    Button btnSend;

    public static final String TAG = "wifidirectdemo";
    private WifiP2pManager manager;
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;

    private final IntentFilter intentFilter = new IntentFilter();
    private final IntentFilter wifiIntentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;

    WifiManager wifiManager;
    WiFiBroadcastReceiver receiverWifi;
    private boolean isWifiConnected;

    public boolean isVisible = true;

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_send);

        ButterKnife.bind(this);

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

        if (Configuration.isDeviceBridgingEnabled) {
            wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

            if (!wifiManager.isWifiEnabled()) {
                Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG)
                        .show();
                wifiManager.setWifiEnabled(true);
            }

            receiverWifi = new WiFiBroadcastReceiver(wifiManager, this, this.isWifiConnected);

            wifiIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            wifiIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

            registerReceiver(receiverWifi, wifiIntentFilter);

            this.connectToAccessPoint("DIRECT-Sq-Android_ca89", "c5umx0mw");
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AllEncompasingP2PClient c : MeshNetworkManager.routingTable.values()) {
                    if (c.getMac().equals(MeshNetworkManager.getSelf().getMac()))
                        continue;
                    Sender.queuePacket(new Packet(Packet.TYPE.MESSAGE, editText.getText().toString().getBytes(), c.getMac(),
                            WiFiDirectBroadcastReceiver.MAC));
                }
            }
        });
    }

    /**
     * register the BroadcastReceiver with the intent values to be matched
     */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
        this.isVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        this.isVisible = false;
    }


    @Override
    public void showDetails(WifiP2pDevice device) {
    }

    @Override
    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(DataSendActivity.this, "Connect failed. Retry.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);

            }

            @Override
            public void onSuccess() {
//                fragment.getView().setVisibility(View.GONE);
            }

        });
    }

    @Override
    public void onChannelDisconnected() {
        // we will try once more
        if (manager != null && !retryChannel) {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } else {
            Toast.makeText(this, "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cancelDisconnect() {

    }

    public void connectToAccessPoint(String ssid, String passphrase) {

        Log.d(WiFiDirectActivity.TAG, "Trying to connect to AP : (" + ssid + "," + passphrase + ")");

        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + ssid + "\"";
        wc.preSharedKey = "\"" + passphrase + "\""; // "\""+passphrase+"\"";
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        // connect to and enable the connection
        int netId = wifiManager.addNetwork(wc);
        wifiManager.enableNetwork(netId, true);
        wifiManager.setWifiEnabled(true);

        Log.d(WiFiDirectActivity.TAG, "Connected? ip = " + wifiManager.getConnectionInfo().getIpAddress());
        Log.d(WiFiDirectActivity.TAG, "Connected? bssid = " + wifiManager.getConnectionInfo().getBSSID());
        Log.d(WiFiDirectActivity.TAG, "Connected? ssid = " + wifiManager.getConnectionInfo().getSSID());

        if (wifiManager.getConnectionInfo().getIpAddress() != 0) {
            this.isWifiConnected = true;
            Toast.makeText(this, "Connected!!! ip = " + wifiManager.getConnectionInfo().getIpAddress(),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    this,
                    "WiFi AP connection failed... ip = " + wifiManager.getConnectionInfo().getIpAddress() + "(" + ssid
                            + "," + passphrase + ")", Toast.LENGTH_LONG).show();
        }
    }

    public void resetData() {
    }
}
