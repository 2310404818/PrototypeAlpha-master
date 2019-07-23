package com.swj.prototypealpha.swj;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.ChooseCheckPerson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 审阅界面
 * 检查项目、检查地点、检查时间、检查人、记录人、被检查人、建设单位
 * 监理单位、基本情况、执法措施和要求、签名
 * 开始签名、记录提交
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
        if (ChooseCheckPerson.flag!=0){
            nameList.clear();
            signList.clear();
     //       Log.d(String.valueOf(ChooseCheckPerson.flag),"的哈的话送降低哦啊降低哦啊降低哦按实际");
            for (int i=0;i<ChooseCheckPerson.flag;i++)
            {
                nameList.add(ChooseCheckPerson.name[i]);
                signList.add(ChooseCheckPerson.picture[i]);
            }
            signadapter.notifyDataSetChanged();
        }

    }

    /**
     * 点击发起签名弹出框
     * 点击提交弹出框
     *
     */
    public void DialogFor(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("确定发起签名？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent =new Intent(getActivity(),ChooseCheckPerson.class);
                        startActivityForResult(intent, 202);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.show();
    }
    public void Dialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("确定提交检查记录？提交以后不可修改")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"记录已提交",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),CheckPerson.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.show();
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
                    DialogFor();
         //       Intent intent =new Intent(getActivity(),ChooseCheckPerson.class);
           //     startActivityForResult(intent, 202);
            }
        });
        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent intent = new Intent(getActivity(),CheckPerson.class);
          //      startActivity(intent);
                Dialog();
            }
        });

        re_picture =getActivity().findViewById(R.id.re_picture);
        re_picture.setLayoutManager(new GridLayoutManager(getActivity(),2));
        signadapter= new SignAdapter(signList,nameList);
        re_picture.setAdapter(signadapter);


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
