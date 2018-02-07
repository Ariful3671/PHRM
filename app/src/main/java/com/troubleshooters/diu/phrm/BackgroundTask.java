package com.troubleshooters.diu.phrm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Arif on 31-01-18.
 */

public class BackgroundTask extends AsyncTask<Void, Void, String>
{

    String json_url;
    String JSON_STRING;
    Context context;


    public BackgroundTask(Context context) {
        this.context=context;
    }


    @Override
    protected void onPreExecute() {
        json_url="http://phrmweb.ml/PHRM/json_get_data.php";
    }


    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url=new URL(json_url);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder=new StringBuilder();
            while ((JSON_STRING=bufferedReader.readLine())!=null)
            {
               stringBuilder.append(JSON_STRING+"\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(String result) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("json",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("json",result);
        editor.commit();
    }



}
