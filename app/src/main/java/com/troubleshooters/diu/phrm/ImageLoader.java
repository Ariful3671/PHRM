package com.troubleshooters.diu.phrm;

        import android.*;
        import android.app.Activity;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.pdf.PdfDocument;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Environment;
        import android.support.v4.app.ActivityCompat;
        import android.widget.Toast;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.net.URI;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

/**
 * Created by Arif on 26-03-18.
 */

public class ImageLoader extends AsyncTask<Void,Void,Void> {

    List<Uri> uriArray;
    Context context;
    String hospitalName;
    String reportType;

    public ImageLoader(List<Uri> uriArray, Context context, String hospitalName, String reportType) {
        this.uriArray = uriArray;
        this.context=context;
        this.hospitalName=hospitalName;
        this.reportType=reportType;
    }



    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Bitmap> bitmapArray=new ArrayList<Bitmap>();
        InputStream in = null;
        for(int j=0;j<uriArray.size();j++)
        {
            try{
                BitmapFactory.Options options = new BitmapFactory.Options();
                in = context.getContentResolver().openInputStream(uriArray.get(j));
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(in,null, options);
                options.inSampleSize=calculateInSampleSize(options,320,680);
                options.inJustDecodeBounds = false;
                in = context.getContentResolver().openInputStream(uriArray.get(j));
                Bitmap bitmap=BitmapFactory.decodeStream(in,null, options);
                bitmapArray.add(bitmap);
            }
            catch (OutOfMemoryError e)
            {

            }
            catch (FileNotFoundException f) {
                f.printStackTrace();
            }
        }
        isStoragePermissionGranted();
        SharedPreferences sharedPreferences=context.getSharedPreferences("profileinfo",Context.MODE_PRIVATE);
        String user=sharedPreferences.getString("userid","");
        File root = new File(Environment.getExternalStorageDirectory(), "PHRM/"+user);
        if (!root.exists()) {
            root.mkdir();
        }

        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss a");
        String DateToStr = format.format(curDate);
        File file = new File(root, hospitalName+"_"+reportType+"_"+DateToStr + ".pdf");


        PdfDocument pdfDocument = new PdfDocument();

        for (int k = 0; k < bitmapArray.size(); k++) {
            Bitmap tempBitmap = bitmapArray.get(k);
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(tempBitmap.getWidth(), tempBitmap.getHeight(), k + 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);
            Bitmap tempBitmap2 = Bitmap.createScaledBitmap(tempBitmap, tempBitmap.getWidth(), tempBitmap.getHeight(), true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(tempBitmap2, 0, 0, null);
            pdfDocument.finishPage(page);
        }


        try {
            OutputStream fileOutputStream = new FileOutputStream(file);
            pdfDocument.writeTo(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
        return null;
    }
}
