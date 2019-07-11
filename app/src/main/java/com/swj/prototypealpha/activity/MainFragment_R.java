package com.swj.prototypealpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.oyjz.PersonalItemView;
import com.swj.prototypealpha.oyjz.PersonalinfoActivity;
import com.swj.prototypealpha.oyjz.ResetPasswordActivity;
import com.swj.prototypealpha.swj.LaunchActivity;
import com.swj.prototypealpha.swj.ProjectListActivity;


public class MainFragment_R extends Fragment {

    PersonalItemView right_name;
    PersonalItemView right_passwordchange;
    Button right_logout;

    private void initUI () {
        right_name = getActivity().findViewById(R.id.right_name);
        right_passwordchange = getActivity().findViewById(R.id.right_passwordchange);
        right_logout = getActivity().findViewById(R.id.right_logout);
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
        setOnclickLisener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_fragment__r, container, false);
    }

}
