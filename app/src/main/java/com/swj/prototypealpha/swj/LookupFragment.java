package com.swj.prototypealpha.swj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.ChooseCheckPerson;
import com.swj.prototypealpha.swj.util.ItemBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 审阅界面
 */

public class LookupFragment extends Fragment {
    TextView             text_look_proj;
    TextView             text_look_time;
    TextView             text_look_addr;
    TextView             text_bulid;
    TextView             text_check;
    TextView             text_look_foundation;
    TextView             text_look_rocord;
    TextView             text_checkpeople;
    TextView             text_writepeople;
    TextView             text_donepeople;
    Button               button;
    Button               button_complete;
    RecyclerView         re_picture;
    public static SignAdapter signadapter;
    private static List<ChargePerson> nameList = new ArrayList<>();
    public static   List<Picture> signList    = new ArrayList<>();


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_lookup, container, false);
    }
    public void initData(){
        Log.d(String.valueOf(ChooseCheckPerson.flag),"重大降低哦按实际地啊降低哦阿三降低哦啊");
        if (ChooseCheckPerson.flag!=0){
            nameList.clear();
            signList.clear();
            for (int i=0;i<ChooseCheckPerson.flag;i++)
            {
                nameList.add(ChooseCheckPerson.name[i]);
                signList.add(ChooseCheckPerson.picture[i]);
            }
            signadapter.notifyDataSetChanged();
        }
/*        if (ChooseCheckPerson.flag==1)
        {
        //    Log.d("和大伙都","你打好嗲花大沙嗲收到货");
            nameList.clear();
            signList.clear();
            nameList.add(ChooseCheckPerson.name[0]);
            signList.add(ChooseCheckPerson.picture[0]);
            nameList.add(ChooseCheckPerson.name[1]);
            signList.add(ChooseCheckPerson.picture[1]);
            nameList.add(ChooseCheckPerson.name[2]);
            signList.add(ChooseCheckPerson.picture[2]);
            nameList.add(ChooseCheckPerson.name[3]);
            signList.add(ChooseCheckPerson.picture[3]);
            nameList.add(ChooseCheckPerson.name[4]);
            signList.add(ChooseCheckPerson.picture[4]);
            signadapter.notifyDataSetChanged();
        }
*/
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        text_look_foundation = getActivity().findViewById(R.id.text_look_foundation);
        text_look_rocord = getActivity().findViewById(R.id.text_lookup_record);

        text_bulid = getActivity().findViewById(R.id.text_buildeproj);
        text_bulid.setText("长沙市工务局");
        text_check = getActivity().findViewById(R.id.text_checkproj);
        text_check.setText("城规监理");
        text_look_addr = getActivity().findViewById(R.id.text_addrproj);
        text_look_addr.setText("岳麓区XX街道XX路");
        text_look_time = getActivity().findViewById(R.id.text_currenttime4);
        Date newTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        text_look_time.setText(sdf.format(newTime));
        text_look_proj = getActivity().findViewById(R.id.text_projchenck);
        text_look_proj.setText("橘子洲大桥提质改造工程");
        text_checkpeople = getActivity().findViewById(R.id.text_checkpeople);
        text_checkpeople.setText("张三");
        text_writepeople = getActivity().findViewById(R.id.text_writepeople);
        text_writepeople.setText("李四");
        text_donepeople = getActivity().findViewById(R.id.text_donepeople);
        text_donepeople.setText("王五");
        button = getActivity().findViewById(R.id.button);
        button_complete=getActivity().findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),ChooseCheckPerson.class);
           //     startOnActivity(intent);
                startActivityForResult(intent, 202);
            }
        });
        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CheckPerson.class);
                startActivity(intent);
            }
        });

        re_picture =getActivity().findViewById(R.id.re_picture);
        re_picture.setLayoutManager(new GridLayoutManager(getActivity(),2));
        signadapter= new SignAdapter(signList,nameList);
        re_picture.setAdapter(signadapter);

/*
        signList.clear();
        nameList.clear();
        ChargePerson name = new ChargePerson("执法人员");
        ChargePerson name1 = new ChargePerson("执法人员1");
        ChargePerson name2 = new ChargePerson("执法人员2");
       // nameList.add(name);
        Picture picture = new Picture(BitmapFactory.decodeResource(getResources(),R.mipmap.sign1));
        Picture picture1 = new Picture(BitmapFactory.decodeResource(getResources(),R.mipmap.sign2));
        Picture picture2 = new Picture(BitmapFactory.decodeResource(getResources(),R.mipmap.sign3));
        signList.add(picture);
        nameList.add(name);
        signList.add(picture1);
        nameList.add(name1);
        signList.add(picture2);
        nameList.add(name2);
        int len = signList.size();
        signadapter.notifyDataSetChanged();
        */



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202 && resultCode == 110) {
            for (int i = 0; i < 5; i++) {
                initData();
            }
        }
    }

}
