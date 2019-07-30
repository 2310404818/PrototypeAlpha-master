package com.swj.prototypealpha.oyjz;

/**
 * 录音实体类
 * 录音名称
 * 录音路径
 */
public class Record {
    private String name;
    private String path;
    private String time;
    public Record(String name,String path){
        this.name=name;
        this.path=path;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getName(){
        return name;
    }
    public  String getPath(){
        return path;
    }

    public String getTime() {
        return time;
    }
}