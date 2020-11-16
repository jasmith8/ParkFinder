package com.example.smithjarod_parkfinder.objects;

public class FavoriteObject {

    String _name;
    boolean _isPark;
    String _idCode;

    public FavoriteObject(){

    }
    public FavoriteObject(String name, boolean isPark, String idCode) {
        this._name = name;
        this._isPark = isPark;
        this._idCode = idCode;
    }

    public String name(){
        return _name;
    }
    public boolean isPark(){
        return _isPark;
    }
    public String idCode(){
        return _idCode;
    }

}
