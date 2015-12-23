package com.midisheetmusic.camera;

import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.midisheetmusic.SheetMusicActivity;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Context mContext;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    SheetMusicActivity sheetMusicActivity;

    public CameraPreview(SheetMusicActivity sheetMusicActivity, Context context, Camera camera) {
        super(context);
        this.sheetMusicActivity = sheetMusicActivity;
        mContext = context;
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            camera.enableShutterSound(false);
        }
    }

    private final Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };
    final transient private Camera.PictureCallback onPicTaken = new Camera.PictureCallback() {
        /**
         * After taking picture, onPictureTaken() will be called where image
         * will be saved.
         */
        @Override
        public void onPictureTaken(final byte[] data, final Camera camera) {
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Log.d(VIEW_LOG_TAG, "1============== ");
            if (mCamera == null) {
                Log.d(VIEW_LOG_TAG, "2============== ");
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void refreshCamera(Camera camera) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        refreshCamera(mCamera);
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        mCamera = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // mCamera.release();

    }
}