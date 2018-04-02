package com.troubleshooters.diu.phrm;

import android.*;
import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomCamera extends AppCompatActivity {


    public android.hardware.Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    Button shutter,done;
    String hospitalName,reportType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);

        frameLayout=(FrameLayout)findViewById(R.id.frameLayout_customCamera);
        shutter=(Button)findViewById(R.id.shutter_customCamera);
        done=(Button)findViewById(R.id.button_done);
        getSupportActionBar().hide();





        hospitalName=getIntent().getStringExtra("HN");
        reportType=getIntent().getStringExtra("RT");


        isCameraPermissionGranted();

        camera= android.hardware.Camera.open();




        showCamera=new ShowCamera(this,camera);
        frameLayout.addView(showCamera);

        done.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Uri> uriList=new ArrayList<Uri>();
                        File f=new File(Environment.getExternalStorageDirectory(),"PHRM/temp");
                        if(f.exists())
                        {
                            String[] children=f.list();
                            for(int i=0;i<children.length;i++)
                            {
                                Uri uri=Uri.fromFile(new File(f,children[i]));
                                uriList.add(uri);
                            }
                        }
                        new ImageLoader(uriList,CustomCamera.this,hospitalName,reportType).execute();
                        ReportActivity.reportActivity.finish();
                        Intent intent=new Intent(CustomCamera.this,ReportActivity.class);
                        intent.putExtra("loadingTime",5000);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        shutter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(camera!=null)
                        {
                         camera.takePicture(null,null,pictureCallback);
                        }
                    }
                }
        );

    }

    android.hardware.Camera.PictureCallback pictureCallback=new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {

            File picture_file=getOutputMediaFile();

            if(picture_file==null)
            {
                return;
            }
            else{
                try {
                    FileOutputStream outputStream=new FileOutputStream(picture_file);
                    outputStream.write(data);
                    outputStream.close();
                    camera.startPreview();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private File getOutputMediaFile() {

        String state= Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED))
        {
            return null;
        }
        else{

            File root = new File(Environment.getExternalStorageDirectory(), "PHRM/"+"temp");
            if (!root.exists()) {
                root.mkdirs();
            }
            Date curDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss a");
            String DateToStr = format.format(curDate);
            File file = new File(root, "PHRM"+DateToStr + ".jpg");
            return file;
        }
    }


    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(CustomCamera.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        File f=new File(Environment.getExternalStorageDirectory(),"PHRM/temp");
        if(f.exists())
        {
            String[] children=f.list();
            for(int i=0;i<children.length;i++)
            {
                new File(f,children[i]).delete();
            }
            f.delete();
        }

    }
}
