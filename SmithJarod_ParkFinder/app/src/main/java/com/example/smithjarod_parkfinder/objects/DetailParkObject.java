package com.example.smithjarod_parkfinder.objects;

import java.util.ArrayList;

public class DetailParkObject {

    String parkNameText;
    String parkUrlText;
    String descriptionDetailText;
    String hoursDetailText;
    String feesDetailText;
    String activitiesDetailText;
    String parkId;
    ArrayList<AddressObject> addresses;

    ArrayList<String> imageArray;

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

    String getParkNameText(){
        return parkNameText;
    }
    String getParkUrlText(){
        return parkUrlText;
    }
    String getDescriptionDetailText(){
        return descriptionDetailText;
    }
    String getHoursDetailText(){
        return hoursDetailText;
    }
    String getFeesDetailText(){
        return feesDetailText;
    }
    String getActivitiesDetailText(){
        return activitiesDetailText;
    }
    String getParkId(){
        return parkId;
    }
    ArrayList<AddressObject> getAddresses(){
        return addresses;
    }
    ArrayList<String> getImageArray(){
        return imageArray;
    }
}
