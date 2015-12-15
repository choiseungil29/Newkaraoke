package com.vpang.clicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vpang.clicker.wifi_direct.WiFiDirectActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @Bind(R.id.btn_arrow)
    Button btnArrow;
    @Bind(R.id.btn_num)
    Button btnNum;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.btn_recode)
    Button btnRecord;
    @Bind(R.id.btn_wifi_direct_connect)
    Button btnWifiDirectConnect;
    @Bind(R.id.btn_wifi_direct_send)
    Button btnWifiDirectSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ArrowActivity.class));
            }
        });
        btnNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NumberActivity.class));
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
            }
        });
        btnWifiDirectConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WiFiDirectActivity.class));
            }
        });
        btnWifiDirectSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DataSendActivity.class));
            }
        });

    }


}
