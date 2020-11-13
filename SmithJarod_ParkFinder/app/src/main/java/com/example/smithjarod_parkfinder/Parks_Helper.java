package com.example.smithjarod_parkfinder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.smithjarod_parkfinder.objects.AddressObject;
import com.example.smithjarod_parkfinder.objects.DetailParkObject;
import com.example.smithjarod_parkfinder.objects.ParkObject;

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
    String _parkId;
    String parksURL = "https://developer.nps.gov/api/v1/parks?api_key=ZASYXugeyaioSMOW67WfUMn9hOf0X1nzdIFbUAwZ&limit=1000&sort=fullName";
    String campsURL = "https://developer.nps.gov/api/v1/campgrounds?api_key=ZASYXugeyaioSMOW67WfUMn9hOf0X1nzdIFbUAwZ&limit=1000&sort=name";
    ArrayList<ParkObject> tempArray = new ArrayList<>();
    ArrayList<DetailParkObject> tempDetailParkArray = new ArrayList<>();

    public ArrayList<ParkObject> parkObjects(Boolean isPark, Context context, String parkId) {

        _isPark = isPark;
        _parkId = parkId;
        if (isPark == true){
            jsonURL = parksURL;
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

    public ArrayList<DetailParkObject> detailParkObjects(Boolean isPark, Context context, String _parkId){
        return tempDetailParkArray;
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

    private class DataTask extends AsyncTask<String,Integer,String>{

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
            Log.d(TAG, "doInBackground: ");
            return data;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: "+values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: done");
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
                parkName = parkName.replace("&#241;", "ñ");
                parkName = parkName.replace("&#257;", "ā");
                parkName = parkName.replace("&#333;", "ō");
                String tempStates = obj.getString("states");
                String[] parkStates = tempStates.split(",");
                String parkId = obj.getString("id");
                String latitude = obj.getString("latitude");
                String longitude = obj.getString("longitude");

                Log.d(TAG, "getParkJSON: "+parkName);
                for(String code:parkStates){
                    String parkStateCode = code;

                    ParkObject parkObject = new ParkObject(parkName,parkStateCode,parkId,latitude,longitude);
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
                parkName = parkName.replace("&#241;", "ñ");
                parkName = parkName.replace("&#257;", "ā");
                parkName = parkName.replace("&#333;", "ō");
                String tempStates = obj.getString("states");
                String[] parkStates = tempStates.split(",");
                String parkId = obj.getString("id");
                String latitude = obj.getString("latitude");
                String longitude = obj.getString("longitude");

                Log.d(TAG, "getParkJSON: "+parkName);
                for(String code:parkStates){
                    String parkStateCode = code;
                    ParkObject parkObject = new ParkObject(parkName,parkStateCode,parkId,latitude,longitude);
                    tempArray.add(parkObject);
                }
            }
        }catch ( JSONException e){
            e.printStackTrace();
        }
    }
    private void getParkDetailJSON() {
        try {
            JSONObject outerObject = new JSONObject(json);
            JSONArray data = outerObject.getJSONArray("data");
            for (int i =0;i<data.length();i++){
                JSONObject obj = data.getJSONObject(i);

                String parkName = obj.getString("fullName");
                parkName = parkName.replace("&#241;", "ñ");
                parkName = parkName.replace("&#257;", "ā");
                parkName = parkName.replace("&#333;", "ō");

                String parkUrl = obj.getString("url");
                String description = obj.getString("description");

                JSONArray hoursArray = obj.getJSONArray("operatingHours");
                String hours="";
                for (int k =0;k<hoursArray.length();k++){
                    JSONObject obj2 = hoursArray.getJSONObject(k);
                    hours = "NAME: "+obj2.getString("name")+"\n";
                    JSONObject standardHoursObj = obj2.getJSONObject("standardHours");
                    hours = hours+"Monday: "+standardHoursObj.getString("monday")+"\n";
                    hours = hours+"Tuesday: "+standardHoursObj.getString("tuesday")+"\n";
                    hours = hours+"Wednesday: "+standardHoursObj.getString("wednesday")+"\n";
                    hours = hours+"Thursday: "+standardHoursObj.getString("thursday")+"\n";
                    hours = hours+"Friday: "+standardHoursObj.getString("friday")+"\n";
                    hours = hours+"Saturday: "+standardHoursObj.getString("saturday")+"\n";
                    hours = hours+"Sunday: "+standardHoursObj.getString("sunday")+"\n\n";
                }

                JSONArray feesArray = obj.getJSONArray("entranceFees");
                String fees = "";
                for (int p=0;p<feesArray.length();p++){
                    JSONObject obj3 = feesArray.getJSONObject(p);
                    fees  = "Title: "+obj3.getString("title")+"\n";
                    fees = fees+"Cost: $"+obj3.getString("cost")+"\n";
                    fees = fees+"Description: "+obj3.getString(description)+"\n\n";
                }

                JSONArray activitiesArray = obj.getJSONArray("activities");
                String activities = "";
                for (int q=0; q<activitiesArray.length();q++){
                    JSONObject obj4 = activitiesArray.getJSONObject(q);
                    activities = activities+" "+obj4.getString("name")+", ";
                }

                String parkId = obj.getString("id");

                JSONArray addressArray = obj.getJSONArray("addresses");
                ArrayList<AddressObject> addressObjects = new ArrayList<>();
                for (int w =0; w<addressArray.length();w++){
                    JSONObject obj5 = addressArray.getJSONObject(w);
                    String type = obj5.getString("type");
                    String address ="";
                    address = address+obj5.getString("addresses")+", ";
                    address = address+obj5.getString("line1")+", ";
                    address = address+obj5.getString("line2")+", ";
                    address = address+obj5.getString("line3")+", ";
                    address = address+obj5.getString("city")+", ";
                    address = address+obj5.getString("stateCode")+", ";
                    address = address+obj5.getString("postalCode");
                    addressObjects.add(new AddressObject(address,type));

                }

                JSONArray imageArray = obj.getJSONArray("images");
                ArrayList<String> imageUrls = new ArrayList<>();
                for (int e =0;e<imageArray.length();e++){
                    JSONObject obj6 = imageArray.getJSONObject(e);
                    imageUrls.add(obj6.getString("url"));
                }




                DetailParkObject parkObject = new DetailParkObject(parkName,parkUrl,description,hours,fees,activities,parkId,addressObjects,imageUrls);
                tempDetailParkArray.add(parkObject);
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




