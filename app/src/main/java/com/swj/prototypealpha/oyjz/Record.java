package com.swj.prototypealpha.oyjz;

public class Record {
    private String name;
    private String path;
    public Record(String name,String path){
        this.name=name;
        this.path=path;
    }
    public String getName(){
        return name;
    }
    public  String getPath(){
        return path;
    }
}