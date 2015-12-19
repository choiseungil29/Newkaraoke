package com.vpang.clicker;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class RecordActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView surfaceView;
    private boolean is_holder_created;

    Button button;

    private SurfaceHolder holder;
    private MediaRecorder recorder;
    boolean is_recording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinearLayout rl = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(1, 1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rl.setLayoutParams(params);


        rl.setOrientation(LinearLayout.VERTICAL);
        button = new Button(this);
        button.setLayoutParams(params2);
        surfaceView = new SurfaceView(this);
        surfaceView.setLayoutParams(params1);
        surfaceView.setBackgroundColor(Color.BLACK);
        rl.addView(button);
        rl.addView(surfaceView);

        setContentView(rl);

        is_holder_created = false;
        is_recording = false;

        holder = surfaceView.getHolder();
        holder.addCallback(this);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (!is_recording) {
            startRecord();
            is_recording = true;
            button.setText("녹화중지");
        } else {
            stopRecord();
            is_recording = false;
            button.setText("녹화시작");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecord();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        is_holder_created = true;
        Toast.makeText(this, "이제 녹화를 시작할 수 있습니다.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        is_holder_created = false;
    }

    public void stopRecord() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
            } catch (Exception e) {
            }
            recorder = null;
        }
    }

    public void startRecord() {
        try {
            recorder = new MediaRecorder();
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 녹음될 사운드의 출처
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 사운드의 저장 형식
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 사운드 코덱 지정
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/vpang/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = File.createTempFile(getNewFileName(), ".mp4", dir);

            recorder.setOutputFile(file.getAbsolutePath());

            /** 미리보기 연결 */
            recorder.setPreviewDisplay(holder.getSurface());

            // 녹음의 시작
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "영상 녹화에 실패했습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public String getNewFileName() {
        Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mi = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);

        return String.format(Locale.getDefault(), "%04d-%02d-%02d %02d;%02d;%02d", yy, mm, dd, hh, mi, ss);

    }
}
