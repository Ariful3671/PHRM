package com.troubleshooters.diu.phrm;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.troubleshooters.diu.phrm.Adapter.ManualReportAdapter;
import com.troubleshooters.diu.phrm.Adapter.ModelReport;
import com.troubleshooters.diu.phrm.Adapter.ModelReportInfo;
import com.troubleshooters.diu.phrm.Adapter.ReportAdapter;
import com.troubleshooters.diu.phrm.Helper.RecyclerItemTouchHelper;
import com.troubleshooters.diu.phrm.Helper.RecyclerTouchHelperListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity implements RecyclerTouchHelperListener {


    NetworkChecker networkChecker;//checking internet connection
    TextView loading, noReports;
    ProgressBar progressBar;
    //ListView listView;
    List<ModelReport> reportList;
    //ReportAdapter adapter;
    JSONObject jsonObject;
    JSONArray jsonArray;


    String hospitalName;
    String reportType;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    List<ModelReportInfo> reportInfo;
    RecyclerView recyclerView;
    RelativeLayout rootLayout;
    int loadingTime;
    public static Activity report;
    ManualReportAdapter adapter;
    int flag;
    public  static Activity reportActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Reports");
        loadingTime=getIntent().getIntExtra("loadingTime",3000);

        report = this;



        //........................................This for online report collection...................................................

        /* //Creating instance
        networkChecker=new NetworkChecker(ReportActivity.this);
        loading=(TextView)findViewById(R.id.loading_textview_report);
        noReports=(TextView)findViewById(R.id.textview_report);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar_report);
        listView=(ListView)findViewById(R.id.listview_report);
        reportList=new ArrayList<>();
        //





        //Checking internet connection
        if(networkChecker.isConnected())
        {

            BackgroundTask backgroundTask=new BackgroundTask(this);
            backgroundTask.execute();
        }
        else {
            Toast.makeText(ReportActivity.this,"NO or Bad internet connection", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences=getSharedPreferences("json", Context.MODE_PRIVATE);
        String json=sharedPreferences.getString("json","");
        if(json.equals(""))
        {
            loading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            noReports.setVisibility(View.VISIBLE);

        }
        else{
            loading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            noReports.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            try {
                jsonObject=new JSONObject(json);
                jsonArray=jsonObject.getJSONArray("server_response");
                int count=0;
                String hospital,id,userName,appointment,comment,received,reportName;

                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    hospital=JO.getString("hospital");
                    id=JO.getString("id");
                    userName=JO.getString("username");
                    appointment=JO.getString("appointment");
                    comment=JO.getString("comment");
                    received=JO.getString("sendingdate");
                    reportName=JO.getString("reportname");
                    ModelReport report=new ModelReport(hospital,id,userName,appointment,comment,received,reportName);
                    reportList.add(report);
                    adapter=new ReportAdapter(this,reportList);
                    listView.setAdapter(adapter);
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v7.app.AlertDialog.Builder builder;
                        builder = new android.support.v7.app.AlertDialog.Builder(ReportActivity.this, R.style.CustomDialogTheme);
                        builder.setMessage("Hospital: "+reportList.get(position).getHospital()+"\n\n"
                                          +"Received Date: "+reportList.get(position).getReceived()+"\n"
                                          +"Appointment Date: "+reportList.get(position).getAppointment()+"\n\n"
                                          +"Comment: "+reportList.get(position).getComment()+"\n\n"
                                          +"File: "+reportList.get(position).getReportName()+"\n")
                                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                })
                                .setNegativeButton("View", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {



                                            }
                                        }).show();
                    }
                }
        );*/
        //............................................End online report collection.................................................................

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_report);
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        reportActivity=this;
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_report);
        noReports = (TextView) findViewById(R.id.textview_report);
        loading = (TextView) findViewById(R.id.loading_textview_report);
        SharedPreferences sharedPreferences = getSharedPreferences("profileinfo", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("userid", "");
        isStoragePermissionGranted();




        final File f = new File(Environment.getExternalStorageDirectory() + "/PHRM/" + user);


        if (!f.isDirectory()) {
            noReports.setVisibility(View.VISIBLE);
        } else {

            //Toast.makeText(report, "in else", Toast.LENGTH_SHORT).show();
            File[] contents = f.listFiles();
            if (contents == null) {
                //Toast.makeText(report, "in null", Toast.LENGTH_SHORT).show();
            } else if (contents.length == 0) {

                noReports.setVisibility(View.VISIBLE);
                //Toast.makeText(report, "in 0", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(report, "in else else", Toast.LENGTH_SHORT).show();
                noReports.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                loading.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        reportInfo = new ArrayList<ModelReportInfo>();
                        for (File file : f.listFiles()) {
                            if (file.isFile()) {
                                String name = file.getName();
                                String[] nameArray = name.split("_");
                                reportInfo.add(new ModelReportInfo(nameArray[0], nameArray[1], nameArray[2], file));
                                adapter = new ManualReportAdapter(ReportActivity.this, reportInfo);
                                recyclerView.setAdapter(adapter);

                            }
                        }

                        File fil=new File(Environment.getExternalStorageDirectory(),"PHRM/temp");
                        if(fil.exists())
                        {
                            String[] children=fil.list();
                            for(int i=0;i<children.length;i++)
                            {
                                new File(fil,children[i]).delete();
                            }
                            fil.delete();
                        }
                    }

                }, loadingTime);
            }


            //progressBar.setVisibility(View.GONE);




        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
    }

    //.........................................OnCreate method (close)...................................


    //..................................Creating menu item(Start)........................................
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.storereport_menu, menu);
        return true;

    }


    //...........................................Menu item click listener(Start).....................................
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.item_storeReport) {
            final AlertDialog.Builder builder;
            View mview;
            Button done;
            final EditText HN, RT;
            builder = new AlertDialog.Builder(ReportActivity.this, R.style.CustomDialogreportInfo);
            final AlertDialog dialog = builder.create();
            mview = getLayoutInflater().inflate(R.layout.dialog_report_info, null);
            done = (Button) mview.findViewById(R.id.buttonDone);
            HN = (EditText) mview.findViewById(R.id.etHospitalName);
            RT = (EditText) mview.findViewById(R.id.ettestReportType);

            dialog.setView(mview);
            dialog.show();

            done.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!HN.getText().toString().equals("") || !RT.getText().toString().equals("")) {
                                hospitalName = HN.getText().toString();
                                reportType = RT.getText().toString();
                                String[] options = {"Open gallery","Take Pictures"};
                                AlertDialog.Builder builder1;
                                builder1 = new AlertDialog.Builder(ReportActivity.this, R.style.CustomDialogTheme);
                                final AlertDialog dialog1 = builder1.create();
                                View nview;
                                Button start, cancel;
                                nview = getLayoutInflater().inflate(R.layout.dialog_start_camera_store_report, null);
                                cancel = (Button) nview.findViewById(R.id.cancle_button);
                                listView = (ListView) nview.findViewById(R.id.listViewOptionns);
                                arrayAdapter = new ArrayAdapter<String>(ReportActivity.this, android.R.layout.simple_list_item_1, options);
                                listView.setAdapter(arrayAdapter);
                                listView.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                if (position == 0) {
                                                    pickFromGallery();
                                                    dialog1.cancel();
                                                }
                                                if (position == 1) {
                                                    Intent intent=new Intent(ReportActivity.this,CustomCamera.class);
                                                    intent.putExtra("HN",hospitalName);
                                                    intent.putExtra("RT",reportType);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                );

                                dialog.cancel();
                                dialog1.setView(nview);
                                dialog1.show();
                                cancel.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog1.cancel();
                                            }
                                        }
                                );
                            }

                        }
                    }
            );
        }
        return false;
    }

    //...........................................Menu item click listener(Close).....................................


    //..........................Image picker/Intent(Start)......................................

    public void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    //..........................Image picker/Intent(End)......................................


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                List<Uri> uriArray = new ArrayList<>();
                if (data.getData() != null) {
                    Uri uri = data.getData();
                    uriArray.add(uri);
                    new ImageLoader(uriArray, this, hospitalName, reportType).execute();
                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();

                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                Uri uri = item.getUri();
                                uriArray.add(uri);
                            }
                        }
                        new ImageLoader(uriArray, this, hospitalName, reportType).execute();
                    }
                }

                progressBar.setVisibility(View.VISIBLE);
                loading.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                noReports.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }

                }, 3000);

            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    /*public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }*/


//.....................................Is sample size calculation (start)................................

    /*public static int calculateInSampleSize(
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
    }*/

    //.....................................Is sample size calculation (end)................................


    //....................................Permission for external storage writing(Start)......................................

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

//....................................Permission for external storage writing(Close)......................................


//.....................................Comvert URI to Path(Start)..............................................

    /*public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }*/

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    /*public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }*/


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    /*public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }*/

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    /*public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }*/

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    /*public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }*/

    /**
     * @param //uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    /*public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }*/

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {


        if(viewHolder instanceof ManualReportAdapter.ManualReportAdapterViewHolder)
        {
            flag=1;
            String name=reportInfo.get(viewHolder.getAdapterPosition()).getHn();
            final ModelReportInfo deletedItem=reportInfo.get(viewHolder.getAdapterPosition());
            final int deleteIndex=viewHolder.getAdapterPosition();
            adapter.removeItem(position);
            Snackbar snackbar=Snackbar.make(rootLayout,"1 report removed",Snackbar.LENGTH_SHORT);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag=0;
                    adapter.restoreItem(deletedItem,deleteIndex);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (flag==1)
                    {
                        File f=deletedItem.getfIle();
                        f.delete();
                    }
                }

            }, 3000);


        }

    }


    //.......................................Convert uri to path (close)................................................


}//......................................Main class closing........................................

