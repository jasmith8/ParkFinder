package com.example.smithjarod_parkfinder.objects;

public class AddressObject {

    String address;
    String type;

    public AddressObject(String address, String type) {
        this.address = address;
        this.type = type;
    }

    public String getAddress() {
        return address;

    }
    public String getType(){
        return type;
    }

}
