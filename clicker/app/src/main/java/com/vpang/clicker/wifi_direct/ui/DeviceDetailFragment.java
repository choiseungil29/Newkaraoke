package com.vpang.clicker.wifi_direct.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vpang.clicker.R;
import com.vpang.clicker.wifi_direct.WiFiDirectActivity;
import com.vpang.clicker.wifi_direct.router.AllEncompasingP2PClient;
import com.vpang.clicker.wifi_direct.router.MeshNetworkManager;
import com.vpang.clicker.wifi_direct.router.Packet;
import com.vpang.clicker.wifi_direct.router.Sender;
import com.vpang.clicker.wifi_direct.wifi.WiFiDirectBroadcastReceiver;


/**
 * A fragment that manages a particular peer and allows interaction with device
 * i.e. setting up network connection and transferring data.
 * <p/>
 * NOTE: much of this was taken from the Android example on P2P networking
 */
public class DeviceDetailFragment extends Fragment implements ConnectionInfoListener {

    private static View mContentView = null;
    private WifiP2pDevice device;
    ProgressDialog progressDialog = null;

    private EditText editContent;
    public static TextView textContent;

    public static void updateGroupChatMembersMessage() {
        TextView view = (TextView) mContentView.findViewById(R.id.device_address);
        if (view != null) {
            String s = "Currently in the network chatting: \n";
            for (AllEncompasingP2PClient c : MeshNetworkManager.routingTable.values()) {
                s += c.getMac() + "\n";
            }
            view.setText(s);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContentView = inflater.inflate(R.layout.device_detail, null);
//        editContent = (EditText) mContentView.findViewById(R.id.edit_send_data);
//        textContent = (TextView) mContentView.findViewById(R.id.text_content);
//        mContentView.findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                WifiP2pConfig config = new WifiP2pConfig();
//                config.deviceAddress = device.deviceAddress;
//                config.wps.setup = WpsInfo.PBC;
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//                progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel", "Connecting to :"
//                        + device.deviceAddress, true, true);
//                ((DeviceActionListener) getActivity()).connect(config);
//
//            }
//        });
//
//        mContentView.findViewById(R.id.btn_disconnect).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                ((DeviceActionListener) getActivity()).disconnect();
//            }
//        });
//
//        editContent.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                for (AllEncompasingP2PClient c : MeshNetworkManager.routingTable.values()) {
//                    if (c.getMac().equals(MeshNetworkManager.getSelf().getMac()))
//                        continue;
//                    Sender.queuePacket(new Packet(Packet.TYPE.MESSAGE, s.toString().getBytes(), c.getMac(),
//                            WiFiDirectBroadcastReceiver.MAC));
//                }
//            }
//        });
        return mContentView;
    }

    public static void setDataTextView(String data) {
        textContent.setText(data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = data.getData();
        TextView statusText = (TextView) mContentView.findViewById(R.id.status_text);
        statusText.setText("Sending: " + uri);
        Log.d(WiFiDirectActivity.TAG, "Intent----------- " + uri);
    }

    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        this.getView().setVisibility(View.VISIBLE);

        if (!info.isGroupOwner) {
            Sender.queuePacket(new Packet(Packet.TYPE.HELLO, new byte[0], null, WiFiDirectBroadcastReceiver.MAC));
        }

        // hide the connect button
        mContentView.findViewById(R.id.btn_connect).setVisibility(View.GONE);
    }

    public void showDetails(WifiP2pDevice device) {
        this.device = device;
        this.getView().setVisibility(View.VISIBLE);
        TextView view = (TextView) mContentView.findViewById(R.id.device_address);
        String s = "Currently in the network chatting: \n";
        for (AllEncompasingP2PClient c : MeshNetworkManager.routingTable.values()) {
            s += c.getMac() + "\n";
        }
        view.setText(s);
    }

    public void resetViews() {
        mContentView.findViewById(R.id.btn_connect).setVisibility(View.VISIBLE);
        TextView view = (TextView) mContentView.findViewById(R.id.device_address);
        view.setText("");
        view = (TextView) mContentView.findViewById(R.id.device_info);
        view.setText("");
        view = (TextView) mContentView.findViewById(R.id.group_owner);
        view.setText("");
        view = (TextView) mContentView.findViewById(R.id.status_text);
        view.setText("");
        this.getView().setVisibility(View.GONE);
    }

}
