package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Arif on 30-03-18.
 */

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback{

    Camera camera;
    //Context context;
    SurfaceHolder holder;

    public ShowCamera(Context context,Camera camera) {
        super(context);
        //this.context=context;
        this.camera=camera;
        holder=getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Camera.Parameters parameters=camera.getParameters();

        List<Camera.Size> sizes=parameters.getSupportedPictureSizes();
        Camera.Size picture_size=sizes.get(0);
        for(Camera.Size size:sizes)
        {
            if(size.width>picture_size.width)
            {
                picture_size=size;
            }

        }

        if(this.getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE)
        {
            parameters.set("orientation","portrait");
            camera.setDisplayOrientation(90);
            parameters.setRotation(90);

        }
        else{
            parameters.set("orientation","landscape");
            camera.setDisplayOrientation(0);
            parameters.setRotation(0);
        }
        parameters.setPictureSize(picture_size.width,picture_size.height);
        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

         camera.stopPreview();
         camera.release();

    }
}
