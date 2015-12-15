package com.vpang.clicker.recode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.vpang.clicker.R;


public class SettingsDialog extends Activity {

    private Spinner prSpinnerEncodingFormat;
    private Spinner prSpinnerConainterFormat;
    private Spinner prSpinnerResolution;
    private Button prBtnOk;
    private Button prBtnCancel;

    public static final int cpuH264 = 2;

    //	public static final int cpu3GP = 0;
    public static final int cpuMP4 = 1;

    public static final int cpuRes176 = 0;
    public static final int cpuRes320 = 1;
    public static final int cpuRes480 = 2;
    public static final int cpuRes640 = 3;
    public static final int cpuRes720 = 4;
    public static final int cpuRes7202 = 5;
    public static final int cpuRes1024 = 6;
    public static final int cpuRes1280 = 7;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_settings_dialog);

        prSpinnerResolution = (Spinner) findViewById(R.id.dialog_settings_resolution_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.dialog_settings_resolution_array,
                android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prSpinnerResolution.setAdapter(adapter3);
        prSpinnerResolution.setSelection(DirUtils.puResolutionChoice);
        prSpinnerResolution
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int rowSelected = (int) arg3;
                        DirUtils.puResolutionChoice = rowSelected;
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // do nothing
                    }
                });
        prBtnOk = (Button) findViewById(R.id.dialog_settings_btn1);
        prBtnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // export picture to specified location with specified name
                setResult(RESULT_OK);
                finish();
            }
        });
        prBtnCancel = (Button) findViewById(R.id.dialog_settings_btn2);
        prBtnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // cancel export, just finish the activity
                SettingsDialog.this.finish();
            }
        });
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
