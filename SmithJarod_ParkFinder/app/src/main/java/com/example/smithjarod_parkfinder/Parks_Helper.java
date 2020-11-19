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
    ArrayList<ParkObject> tempParkArray = new ArrayList<>();
    ArrayList<ParkObject> tempCampArray = new ArrayList<>();
    ArrayList<DetailParkObject> tempDetailParkArray = new ArrayList<>();

    public ArrayList<ParkObject> parkObjects(Boolean isPark, Context context, String parkId) {

        _isPark = isPark;
        _parkId = parkId;
        if (isPark == true){
            jsonURL = parksURL;
            tempParkArray = getList(context);

        } else {
            jsonURL = campsURL;
            tempCampArray = getList(context);
            Log.d(TAG, "campObjects: "+tempCampArray.size());
        }


        if (isPark) {
            Collections.sort(tempParkArray, new Comparator<ParkObject>(){
                @Override
                public int compare(ParkObject o1, ParkObject o2) {
                    return o1.getState().compareToIgnoreCase(o2.getState());
                }
            });
            return tempParkArray;
        } else{
            Collections.sort(tempCampArray, new Comparator<ParkObject>(){
                @Override
                public int compare(ParkObject o1, ParkObject o2) {
                    return o1.getState().compareToIgnoreCase(o2.getState());
                }
            });
            return tempCampArray;
        }
    }

    public ArrayList<DetailParkObject> detailParkObjects(Boolean isPark, Context context, String parkId){
        _isPark = isPark;
        _parkId = parkId;
        if (isPark == true){
            Log.d(TAG, "detailParkObjects: ");
            jsonURL = parksURL+"&id="+parkId;
            Log.d(TAG, "detailParkObjects: "+jsonURL);
            tempDetailParkArray = getDetailParkList(context);

        } else {
            //TODO: finish similar to isPark true
            jsonURL = campsURL+"&id="+parkId;
            Log.d(TAG, "detailParkObjects: "+jsonURL);
            tempDetailParkArray = getDetailParkList(context);
        }
        Log.d(TAG, "detailParkObjects: "+tempDetailParkArray.size());
        Log.d(TAG, "detailParkObjects: "+jsonURL);
        return tempDetailParkArray;
    }



    public ArrayList<ParkObject>getList(Context context){
        getConnection(context);
        if(_isPark) {
            return tempParkArray;
        } else{
            return tempCampArray;
        }
    }
    public ArrayList<DetailParkObject>getDetailParkList(Context context){
        getConnection(context);
        Log.d(TAG, "getDetailParkList: "+tempDetailParkArray.size());
        return tempDetailParkArray;
    }

    void getConnection(Context context){
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr !=null){
            NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
            if (networkInfo !=null){
                boolean isConnected = networkInfo.isConnected();
                if(isConnected){
                    DataTask task = new DataTask();
//                    task.execute();
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
            Log.d(TAG, "doInBackground: "+_parkId);
            if(_isPark){
                if (_parkId.contains("ALL")){
                    Log.d(TAG, "doInBackground: true");
                    getParkJSON();
                } else {
                    Log.d(TAG, "doInBackground: false");
                    getParkDetailJSON();
                }
            } else {
                if (_parkId.contains("ALL")) {
                    Log.d(TAG, "doInBackground: true");
                    getCampJSON();
                }else {
                    Log.d(TAG, "doInBackground: false");
                    getCampDetailJSON();
                }
            }
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
    private String fixName(String name){
        name = name.replace("&#241;", "ñ");
        name = name.replace("&#257;", "ā");
        name = name.replace("&#333;", "ō");
        return name;
    }

    private void getCampJSON() {

        //TODO: setup similar to getParkJSON

        try {
            JSONObject outerObject = new JSONObject(json);
            JSONArray data = outerObject.getJSONArray("data");
            for (int i =0;i<data.length();i++){
                JSONObject obj = data.getJSONObject(i);
                String parkName = obj.getString("name"); //good
                parkName = fixName(parkName);
                String parkId = obj.getString("id"); // good
                String latitude = obj.getString("latitude"); // good
                String longitude = obj.getString("longitude"); // good

                Log.d(TAG, "getCampJSON: "+parkName);

                JSONArray addresses = obj.getJSONArray("addresses");
                for (int j = 0; j<addresses.length();j++){
                    Log.d(TAG, "getCampJSON: "+j);
                    JSONObject obj2 = addresses.getJSONObject(j);
                    String stateCode = obj2.getString("stateCode");
                    String type = obj2.getString("type");
                    Log.d(TAG, "getCampJSON: type: "+type);
                    if (type.equals("Physical")){
                        ParkObject parkObject = new ParkObject(parkName,stateCode,parkId,latitude,longitude);
                        tempCampArray.add(parkObject);
                        Log.d(TAG, "getCampJSON: added"+parkName);
                    }
                }


                //TODO: GET STATES
            }
        }catch ( JSONException e){
            e.printStackTrace();
        }
        Log.d(TAG, "getCampJSON: DONE SIZE: "+tempCampArray.size());
    }

    private void getParkJSON() {
        Log.d(TAG, "getParkJSON: ");
        try {
            JSONObject outerObject = new JSONObject(json);
            JSONArray data = outerObject.getJSONArray("data");
            for (int i =0;i<data.length();i++){
                JSONObject obj = data.getJSONObject(i);
                String parkName = obj.getString("fullName");
                parkName = fixName(parkName);
                String tempStates = obj.getString("states");
                String[] parkStates = tempStates.split(",");
                String parkId = obj.getString("id");
                String latitude = obj.getString("latitude");
                String longitude = obj.getString("longitude");

                Log.d(TAG, "getParkJSON: "+parkName);
                for(String code:parkStates){
                    String parkStateCode = code;
                    ParkObject parkObject = new ParkObject(parkName,parkStateCode,parkId,latitude,longitude);
                    tempParkArray.add(parkObject);
                }
            }
        }catch ( JSONException e){
            e.printStackTrace();
            Log.d(TAG, "getParkJSON: "+e);
        }
    }


    private void getParkDetailJSON() {
        Log.d(TAG, "getParkDetailJSON: ");
        try {
            JSONObject outerObject = new JSONObject(json);
            JSONArray data = outerObject.getJSONArray("data");
            for (int i =0;i<data.length();i++){
                JSONObject obj = data.getJSONObject(i);
                String parkName = obj.getString("fullName");
                parkName = fixName(parkName);
                String parkUrl = obj.getString("url");
                String description = obj.getString("description");
                JSONArray hoursArray = obj.getJSONArray("operatingHours");
                String hours="";
                for (int k =0;k<hoursArray.length();k++){
                    JSONObject obj2 = hoursArray.getJSONObject(k);
                    if (k!=0){
                        hours=hours+"\n";
                    }
                    hours = obj2.getString("name")+"\n";
                    JSONObject standardHoursObj = obj2.getJSONObject("standardHours");
                    hours = hours+"Monday: "+standardHoursObj.getString("monday")+"\n";
                    hours = hours+"Tuesday: "+standardHoursObj.getString("tuesday")+"\n";
                    hours = hours+"Wednesday: "+standardHoursObj.getString("wednesday")+"\n";
                    hours = hours+"Thursday: "+standardHoursObj.getString("thursday")+"\n";
                    hours = hours+"Friday: "+standardHoursObj.getString("friday")+"\n";
                    hours = hours+"Saturday: "+standardHoursObj.getString("saturday")+"\n";
                    hours = hours+"Sunday: "+standardHoursObj.getString("sunday");
                }
                JSONArray feesArray = obj.getJSONArray("entranceFees");
                String fees = "";
                for (int p=0;p<feesArray.length();p++){
                    JSONObject obj3 = feesArray.getJSONObject(p);
                    if (p!=0){
                        fees = fees+"\n";
                    }
                    fees  = "Title: "+obj3.getString("title")+"\n";
                    fees = fees+"Cost: $"+obj3.getString("cost")+"\n";
                    fees = fees+"Description: "+obj3.getString("description");
                }
                JSONArray activitiesArray = obj.getJSONArray("activities");
                String activities = "";
                for (int q=0; q<activitiesArray.length();q++){
                    JSONObject obj4 = activitiesArray.getJSONObject(q);
                    activities = activities+obj4.getString("name")+", ";
                }
                String parkId = obj.getString("id");
                JSONArray addressArray = obj.getJSONArray("addresses");
                ArrayList<AddressObject> addressObjects = new ArrayList<>();
                for (int w =0; w<addressArray.length();w++){
                    JSONObject obj5 = addressArray.getJSONObject(w);
                    String type = obj5.getString("type");
                    String address ="";
                    if (!obj5.getString("line1").contains("PO B") && !obj5.getString("line1").isEmpty()){
                        Log.d(TAG, "getParkDetailJSON: ");
                        address = address+obj5.getString("line1")+", \n";
                    }
                    if (!obj5.getString("line2").contains("PO B") && !obj5.getString("line2").isEmpty()) {
                        Log.d(TAG, "getParkDetailJSON: ");
                        address = address + obj5.getString("line2") + ", \n";
                    }
                    if (!obj5.getString("line3").contains("PO B") && !obj5.getString("line3").isEmpty()) {
                        Log.d(TAG, "getParkDetailJSON: ");
                        address = address + obj5.getString("line3") + ", \n";
                    }
                    address = address + obj5.getString("city") + ", ";
                    address = address + obj5.getString("stateCode") + ", ";
                    address = address+obj5.getString("postalCode");
                    addressObjects.add(new AddressObject(address,type));
                }

                JSONArray imageArray = obj.getJSONArray("images");
                ArrayList<String> imageUrls = new ArrayList<>();
                for (int e =0;e<imageArray.length();e++){
                    JSONObject obj6 = imageArray.getJSONObject(e);
                    imageUrls.add(obj6.getString("url"));
                }
                Log.d(TAG, "getParkDetailJSON: "+imageUrls.size());



                DetailParkObject parkObject = new DetailParkObject(parkName,parkUrl,description,hours,fees,activities,parkId,addressObjects,imageUrls);
                Log.d(TAG, "getParkDetailJSON: done");
                tempDetailParkArray.add(parkObject);
            }
        }catch ( JSONException e){
            e.printStackTrace();
            Log.d(TAG, "getParkDetailJSON: "+e);
        }
    }

    private void getCampDetailJSON() {
        Log.d(TAG, "getParkDetailJSON: ");
        try {
            JSONObject outerObject = new JSONObject(json);
            JSONArray data = outerObject.getJSONArray("data");
            for (int i =0;i<data.length();i++){
                JSONObject obj = data.getJSONObject(i);
                String parkName = obj.getString("name");
                parkName = fixName(parkName);
                String parkUrl = obj.getString("url");
                String description = obj.getString("description");
                JSONArray hoursArray = obj.getJSONArray("operatingHours");
                String hours="";
                for (int k =0;k<hoursArray.length();k++){
                    JSONObject obj2 = hoursArray.getJSONObject(k);
                    if (k!=0){
                        hours=hours+"\n";
                    }
                    hours = obj2.getString("name")+"\n";
                    JSONObject standardHoursObj = obj2.getJSONObject("standardHours");
                    hours = hours+"Monday: "+standardHoursObj.getString("monday")+"\n";
                    hours = hours+"Tuesday: "+standardHoursObj.getString("tuesday")+"\n";
                    hours = hours+"Wednesday: "+standardHoursObj.getString("wednesday")+"\n";
                    hours = hours+"Thursday: "+standardHoursObj.getString("thursday")+"\n";
                    hours = hours+"Friday: "+standardHoursObj.getString("friday")+"\n";
                    hours = hours+"Saturday: "+standardHoursObj.getString("saturday")+"\n";
                    hours = hours+"Sunday: "+standardHoursObj.getString("sunday");
                }
                JSONArray feesArray = obj.getJSONArray("fees");
                String fees = "";
                for (int p=0;p<feesArray.length();p++){
                    JSONObject obj3 = feesArray.getJSONObject(p);
                    if (p!=0){
                        fees = fees+"\n";
                    }
                    fees  = "Title: "+obj3.getString("title")+"\n";
                    fees = fees+"Cost: $"+obj3.getString("cost")+"\n";
                    fees = fees+"Description: "+obj3.getString("description");
                }
                String reservationInfo = obj.getString("reservationInfo");
                String parkId = obj.getString("id");
                JSONArray addressArray = obj.getJSONArray("addresses");
                ArrayList<AddressObject> addressObjects = new ArrayList<>();
                for (int w =0; w<addressArray.length();w++){
                    JSONObject obj5 = addressArray.getJSONObject(w);
                    String type = obj5.getString("type");
                    String address ="";
                    if (!obj5.getString("line1").contains("PO B") && !obj5.getString("line1").isEmpty()){
                        address = address+obj5.getString("line1")+", \n";
                    }
                    if (!obj5.getString("line2").contains("PO B") && !obj5.getString("line2").isEmpty()) {
                        address = address + obj5.getString("line2") + ", \n";
                    }
                    if (!obj5.getString("line3").contains("PO B") && !obj5.getString("line3").isEmpty()) {
                        address = address + obj5.getString("line3") + ", \n";
                    }
                    address = address + obj5.getString("city") + ", ";
                    address = address + obj5.getString("stateCode") + ", ";
                    address = address+obj5.getString("postalCode");
                    addressObjects.add(new AddressObject(address,type));
                }
                //Log.d(TAG, "getParkDetailJSON: "+addressObjects.size());

                JSONArray imageArray = obj.getJSONArray("images");
                ArrayList<String> imageUrls = new ArrayList<>();
                for (int e =0;e<imageArray.length();e++){
                    JSONObject obj6 = imageArray.getJSONObject(e);
                    imageUrls.add(obj6.getString("url"));
                }
                Log.d(TAG, "getParkDetailJSON: "+imageUrls.size());



                DetailParkObject parkObject = new DetailParkObject(parkName,parkUrl,description,hours,fees,reservationInfo,parkId,addressObjects,imageUrls);
                Log.d(TAG, "getParkDetailJSON: done");
                tempDetailParkArray.add(parkObject);
            }
        }catch ( JSONException e){
            e.printStackTrace();
            Log.d(TAG, "getParkDetailJSON: "+e);
        }
    }

    private String getNetworkData(){
        String data = "";
        try {
            Log.d(TAG, "getNetworkData: "+jsonURL);
            URL url = new URL(jsonURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            data = IOUtils.toString(is);
            is.close();
            connection.disconnect();

        }catch (IOException e){
            e.printStackTrace();
            Log.d(TAG, "getNetworkData: "+e);
        }
        return data;
    }






}




