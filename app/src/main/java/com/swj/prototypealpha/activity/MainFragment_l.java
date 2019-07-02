package com.swj.prototypealpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.ImageAdapter;
import com.swj.prototypealpha.swj.LaunchActivity;
import com.swj.prototypealpha.swj.ProjectListActivity;

public class MainFragment_l extends Fragment {

    ImageButton projectList;
    ImageButton policyLable;
    ImageButton start;
    ImageButton notice;

    private void initUI () {
        projectList = getActivity().findViewById(R.id.ibtn_project_information);
        policyLable = getActivity().findViewById(R.id.ibtn_policy);
        start = getActivity().findViewById(R.id.ibtn_start);
        notice = getActivity().findViewById(R.id.ibtn_notice);
    }

    private void setOnclickLisener () {
        projectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getActivity(), ProjectListActivity.class);
                startActivity(intent);
            }
        });
        policyLable.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v) {
                Intent intent=new Intent(getActivity(),PolicyLableActivity.class);
                startActivity(intent);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LaunchActivity.class);
                startActivity(intent);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NoticeListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        setOnclickLisener();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_fragment_l, container, false);
    }


}
