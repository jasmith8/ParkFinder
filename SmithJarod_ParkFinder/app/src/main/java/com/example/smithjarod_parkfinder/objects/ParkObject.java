package com.example.smithjarod_parkfinder.objects;

import java.io.Serializable;

public class ParkObject implements Serializable {

    final String parkName;
    final String parkStates;
    final String parkId;
    final String latitude;
    final String longitude;
    //String address;

    public ParkObject(String parkName, String parkStates, String parkId, String latitude, String longitude) {
        this.parkName = parkName;
        this.parkStates = parkStates;
        this.parkId = parkId;
        this.latitude = latitude;
        this.longitude = longitude;
//        this.address = address;
    }

    public String getName(){
        return parkName;
    }
    public String getState(){ return parkStates; }
    public String getParkId(){ return parkId; }
    public String getLatitude(){ return latitude; }
    public String getLongitude(){return longitude; }

//    public LatLng getLatLng(Context context, String address){
//        Geocoder geocoder = new Geocoder(context);
//        List<Address> addressList = null;
//        LatLng latLng = null;
//        try {
//            addressList = geocoder.getFromLocationName(address,1);
//            if (addressList == null){
//                return null;
//            }
//            Address getResult = addressList.get(0);
//            latLng = new LatLng(getResult.getLatitude(),getResult.getLongitude());
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("TAG.ParkObject", "getLatLng: "+e);
//        }
//        return latLng;
//    }


}
