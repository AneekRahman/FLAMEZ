package com.flamez.app;

import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.daasuu.mp4compose.FillMode;
import com.daasuu.mp4compose.Rotation;
import com.daasuu.mp4compose.composer.Mp4Composer;
import com.daasuu.mp4compose.filter.GlBilateralFilter;
import com.daasuu.mp4compose.filter.GlBoxBlurFilter;
import com.daasuu.mp4compose.filter.GlBulgeDistortionFilter;
import com.daasuu.mp4compose.filter.GlFilter;
import com.daasuu.mp4compose.filter.GlInvertFilter;
import com.daasuu.mp4compose.filter.GlSepiaFilter;
import com.daasuu.mp4compose.filter.GlVignetteFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraPostViewActivity extends AppCompatActivity {

    //---------------------------------------------------------------------------------------------

    VideoView videoPostViewVV;
    Button saveBtn;
    ImageButton redoBtn;
    String previewPath;

    String APP_NAME;

    //---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_post_view);

        //---------------------------------------------------------------------------------------------

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        previewPath = bundle.getString("path");

        videoPostViewVV = (VideoView) findViewById(R.id.video_postview_VV);
        saveBtn = (Button) findViewById(R.id.save_btn);
        redoBtn = (ImageButton) findViewById(R.id.redo_btn);

        APP_NAME = getString(R.string.app_name);

        //---------------------------------------------------------------------------------------------

        startPreviewVideo();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreviewVideo();
            }
        });

        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //---------------------------------------FilTER TEST---------------------------------------

        Button filterBtn = (Button) findViewById(R.id.filter);
        final String TAG = "FILTERING";

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                videoPostViewVV.setOnCompletionListener(null);

                new Mp4Composer(previewPath, previewPath)
                        .fillMode(FillMode.PRESERVE_ASPECT_FIT)
                        .filter(new GlBulgeDistortionFilter())
                        .listener(new Mp4Composer.Listener() {
                            @Override
                            public void onProgress(double progress) {
                                Log.d(TAG, "onProgress : " + progress);
                            }

                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "onCompleted()");
                                CameraPostViewActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        startPreviewVideo();
                                        Toast.makeText(getApplicationContext(), "Codec completed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCanceled() {
                                Log.d(TAG, "onCanceled");
                            }

                            @Override
                            public void onFailed(Exception exception) {
                                Log.e(TAG, "onFailed()", exception);
                            }

                        })
                        .start();

            }
        });

    }

    //---------------------------------------------------------------------------------------------

    public void startPreviewVideo(){

       if(videoPostViewVV.isPlaying()) videoPostViewVV.stopPlayback();
        videoPostViewVV.setVideoPath(previewPath);
        videoPostViewVV.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoPostViewVV.start();
            }
        });
        videoPostViewVV.start();

    }

    public void savePreviewVideo(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_NAME);
        File internalStorageDir = new File(getFilesDir(), APP_NAME);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            mediaStorageDir = internalStorageDir;
        }else if(!internalStorageDir.exists() && !internalStorageDir.mkdirs()){
            Log.d("CameraActivity", "Can't create directory to save image.");
            Toast.makeText(this, "Can't create directory to save image.", Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String videoFile = "VID_"  + date + ".mp4";
        String savePath = mediaStorageDir.getPath() + File.separator + videoFile;

        try {

            copyFile(previewPath, savePath);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(new File(savePath));
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
            Toast.makeText(getApplicationContext(), "Video saved!", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void copyFile(String sourceFilePath, String destFilePath ) throws IOException {

        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);

        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    //---------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        super.onPause();
        videoPostViewVV.stopPlayback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPostViewVV.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean deletedCachedPreviewFile = (new File(previewPath)).delete();
    }

    //---------------------------------------------------------------------------------------------

}
