package com.swj.prototypealpha.swj;
import java.io.Serializable;
public class ChargePerson implements Serializable {
    private String name ;

    public ChargePerson(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name =name;
    }
}
