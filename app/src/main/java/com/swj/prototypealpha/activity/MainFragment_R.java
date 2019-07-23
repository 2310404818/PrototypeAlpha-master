package com.swj.prototypealpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.swj.prototypealpha.MyApplication;
import com.swj.prototypealpha.R;
import com.swj.prototypealpha.oyjz.PersonalItemView;
import com.swj.prototypealpha.oyjz.PersonalinfoActivity;
import com.swj.prototypealpha.oyjz.ResetPasswordActivity;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 个人中心界面
 * 查看个人信息
 * 修改个人密码
 * 退出登陆
 */
public class MainFragment_R extends Fragment {

    private PersonalItemView right_name;
    private PersonalItemView right_passwordchange;
    private Button right_logout;
    private ImageView right_top,iv_touxiang;
    private TextView tvr_name,tvr_phone;
    MyApplication myApplication;

    private void initUI () {
        right_name = getActivity().findViewById(R.id.right_name);
        right_passwordchange = getActivity().findViewById(R.id.right_passwordchange);
        right_logout = getActivity().findViewById(R.id.right_logout);
        right_top = getActivity().findViewById(R.id.right_top);
        iv_touxiang = getActivity().findViewById(R.id.iv_touxiang);
        tvr_name = getActivity().findViewById(R.id.tvr_name);
        tvr_phone = getActivity().findViewById(R.id.tvr_phone);
        myApplication = (MyApplication) getActivity().getApplication();
        tvr_phone.setText(myApplication.getTell());
        tvr_name.setText(myApplication.getName());

    }

    private void initData(){
        Log.d("11111111111111111111","111111111111111111111111111");
        Glide.with(getActivity()).load(R.drawable.touxiang)
                    .bitmapTransform(new BlurTransformation(getActivity(),25,3),new CenterCrop(getActivity()))
                    .into(right_top);

        Glide.with(getActivity()).load(R.drawable.touxiang)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(iv_touxiang);

    }
    private void setOnclickLisener () {
        right_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getActivity(), PersonalinfoActivity.class);
                startActivity(intent);
            }
        });
        right_passwordchange.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v) {
                Intent intent=new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        right_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                String log = "NO";
                intent.putExtra("log",log);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        initData();
        setOnclickLisener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_fragment__r, container, false);
    }

}
