package com.example.smithjarod_parkfinder.objects;

import java.util.ArrayList;

public class DetailParkObject {

    final String parkNameText;
    final String parkUrlText;
    final String descriptionDetailText;
    final String hoursDetailText;
    final String feesDetailText;
    final String activitiesDetailText;
    final String parkId;
    final ArrayList<AddressObject> addresses;

    final ArrayList<String> imageArray;

    public DetailParkObject(String parkNameText,
                            String parkUrlText,
                            String descriptionDetailText,
                            String hoursDetailText,
                            String feesDetailText,
                            String activitiesDetailText,
                            String parkId,
                            ArrayList<AddressObject> addresses,
                            ArrayList<String> imageArray) {
        this.parkNameText = parkNameText;
        this.parkUrlText = parkUrlText;
        this.descriptionDetailText = descriptionDetailText;
        this.hoursDetailText = hoursDetailText;
        this.feesDetailText = feesDetailText;
        this.activitiesDetailText = activitiesDetailText;
        this.parkId = parkId;
        this.addresses = addresses;
        this.imageArray = imageArray;
    }

    public String getParkNameText(){
        return parkNameText;
    }
// --Commented out by Inspection START (11/22/20, 3:01 PM):
//    public String getParkUrlText(){
//        return parkUrlText;
//    }
// --Commented out by Inspection STOP (11/22/20, 3:01 PM)
    public String getDescriptionDetailText(){
        return descriptionDetailText;
    }
    public String getHoursDetailText(){
        return hoursDetailText;
    }
    public String getFeesDetailText(){
        return feesDetailText;
    }
    public String getActivitiesDetailText(){
        return activitiesDetailText;
    }
    public String getParkId(){
        return parkId;
    }
    public ArrayList<AddressObject> getAddresses(){
        return addresses;
    }
    public ArrayList<String> getImageArray(){
        return imageArray;
    }
}
