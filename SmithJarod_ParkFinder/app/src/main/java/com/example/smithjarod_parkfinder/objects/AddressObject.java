package com.example.smithjarod_parkfinder.objects;

public class AddressObject {

    final String address;
    final String type;

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
