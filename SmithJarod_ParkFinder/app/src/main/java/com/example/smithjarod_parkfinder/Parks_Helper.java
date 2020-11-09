package com.example.smithjarod_parkfinder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class Parks_Helper {
    public static final String TAG = "TAG.Parks_Helper";
    String jsonURL;
    String json;
    boolean _isPark;
    String _stateCode;
    String parksURL = "https://developer.nps.gov/api/v1/parks?api_key=ZASYXugeyaioSMOW67WfUMn9hOf0X1nzdIFbUAwZ&limit=1000&sort=fullName";
    String campsURL = "https://developer.nps.gov/api/v1/campgrounds?api_key=ZASYXugeyaioSMOW67WfUMn9hOf0X1nzdIFbUAwZ&limit=1000&sort=name";
    ArrayList<ParkObject> tempArray = new ArrayList<>();

    public ArrayList<ParkObject> parkObjects(Boolean isPark, Context context, String stateCode) {

        _isPark = isPark;
        _stateCode = stateCode;
        if (isPark == true){
            if (_stateCode=="ALL"){
                jsonURL = parksURL;
            } else {
                jsonURL = parksURL+"&stateCode="+stateCode;
            }
            tempArray = getList(context);

        } else {
            //TODO: finish similar to isPark true
            jsonURL = campsURL;
            tempArray = getList(context);
        }

        Collections.sort(tempArray, new Comparator<ParkObject>(){

            @Override
            public int compare(ParkObject o1, ParkObject o2) {
                return o1.getState().compareToIgnoreCase(o2.getState());
            }
        });

        return tempArray;
    }

    public ArrayList<ParkObject>getList(Context context){
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr !=null){
            NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
            if (networkInfo !=null){
                boolean isConnected = networkInfo.isConnected();
                if(isConnected){
                    DataTask task = new DataTask();
                    //task.execute();
                    try {

                        Object wait = task.execute().get();
                    }catch (InterruptedException | ExecutionException e){
                        e.printStackTrace();
                    }
                }
                
            }

        } else {
            //TODO: TOAST
        }

        return tempArray;
    }

    private String grabData(){
        String data = "";
        data = getNetworkData();
        json = data;
        return data;
    }

    private class DataTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            data = getNetworkData();
            json = data;
            if(_isPark){
                getParkJSON();
            } else {
                getCampJSON();
            }
            return data;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    private void getCampJSON() {

        //TODO: setup similar to getParkJSON

        try {
            JSONObject outerObject = new JSONObject(json);
            JSONArray data = outerObject.getJSONArray("data");
            for (int i =0;i<data.length();i++){
                JSONObject obj = data.getJSONObject(i);
                String parkName = obj.getString("name");
                String tempStates = obj.getString("states");
                String[] parkStates = tempStates.split(",");
                String parkCode = obj.getString("parkCode");
                String latitude = obj.getString("latitude");
                String longitude = obj.getString("longitude");
                parkName = parkName.replace("&#241;", "ñ");
                Log.d(TAG, "getParkJSON: "+parkName);
                for(String code:parkStates){
                    String parkStateCode = code;

                    ParkObject parkObject = new ParkObject(parkName,parkStateCode,parkCode,latitude,longitude);
                    tempArray.add(parkObject);
                }
            }
        }catch ( JSONException e){
            e.printStackTrace();
        }
    }

    private void getParkJSON() {
        try {
            JSONObject outerObject = new JSONObject(json);
            JSONArray data = outerObject.getJSONArray("data");
            for (int i =0;i<data.length();i++){
                JSONObject obj = data.getJSONObject(i);
                String parkName = obj.getString("fullName");
                String tempStates = obj.getString("states");
                String[] parkStates = tempStates.split(",");
                String parkCode = obj.getString("parkCode");
                String latitude = obj.getString("latitude");
                String longitude = obj.getString("longitude");
                parkName = parkName.replace("&#241;", "ñ");
                Log.d(TAG, "getParkJSON: "+parkName);
                for(String code:parkStates){
                    String parkStateCode = code;

                    ParkObject parkObject = new ParkObject(parkName,parkStateCode,parkCode,latitude,longitude);
                    tempArray.add(parkObject);
                }
            }
        }catch ( JSONException e){
            e.printStackTrace();
        }
    }

    private String getNetworkData(){
        String data = "";
        try {
            URL url = new URL(parksURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            data = IOUtils.toString(is);
            is.close();
            connection.disconnect();

        }catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }






}




