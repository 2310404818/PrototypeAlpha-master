package com.swj.prototypealpha;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.swj.prototypealpha.swj.util.Utils;

import java.io.File;

public class MyApplication extends Application
{
    private static MyApplication appContext;
    private File file;
    private String projectName,address;
    private String idcard,name,age,tell,password,workNumber,sex;
    public void PersonInfo(String tell,String name,String password,String idcard,String workNumber,String sex,String age){
        this.name =name;
        this.idcard = idcard;
        this.password = password;
        this.sex = sex;
        this.workNumber = workNumber;
        this.tell = tell;
        this.age = age;

    }
    public void setFile(File file){
        this.file = file;
    }
    public File getFile(){
        return file;
    }
    public void setProject(String projectName,String address){
        this.projectName = projectName;
        this.address =address;
    }
    public String getProjectName(){
        return  projectName;
    }
    public String getAddress(){
        return address;
    }
    public String getName()
    {
        return name;
    }
    public String getIdcard()
    {
        return idcard;
    }
    public String getAge(){
        return age;
    }
    public String getTell(){
        return tell;
    }
    public String getPassword(){
        return password;
    }
    public String getWorkNumber(){
        return workNumber;
    }
    public String getSex(){
        return sex;
    }
    public static MyApplication getInstance()
    {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        Utils.init(this);
        if(BuildConfig.DEBUG)
        {
            Logger.addLogAdapter(new AndroidLogAdapter());
        }
    }
}
