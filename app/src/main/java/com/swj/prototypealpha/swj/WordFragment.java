package com.swj.prototypealpha.swj;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swj.prototypealpha.R;


public class WordFragment extends Fragment {

    public static TextView text_word_foundation;

    public static TextView text_word_record;

    public static String word_foundation;

    public static String word_question;

    EditText edit_word_foundation;

    EditText edit_word_question;

    FloatingActionButton  fab_foundation_submit;

    FloatingActionButton fab_record_submit;

    LinearLayout linearLayout;

    @SuppressLint("RestrictedApi")
    private void initUI()
    {
        linearLayout = getActivity().findViewById(R.id.linearLayout_focus);
        text_word_foundation = getActivity().findViewById(R.id.word_foundation_lable);
        text_word_record = getActivity().findViewById(R.id.word_question_record_lable);

        edit_word_foundation = getActivity().findViewById(R.id.word_doundation_edit);
        edit_word_foundation.setText("现场情况符合要求");
        edit_word_foundation.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        edit_word_foundation.setGravity(Gravity.TOP);
        edit_word_foundation.setSingleLine(false);
        edit_word_foundation.setHorizontallyScrolling(false);

        edit_word_question = getActivity().findViewById(R.id.word_question_record_edit);
        edit_word_question.setText("要求1：XXXXXXXXXXXXXXXXXXXX"+
                "要求2：XXXXXXXXXXXXXXXXX");

        edit_word_question.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        edit_word_question.setGravity(Gravity.TOP);
        //改变默认的单行模式
        edit_word_question.setSingleLine(false);
        //水平滚动设置为False
        edit_word_question.setHorizontallyScrolling(false);

        fab_record_submit = getActivity().findViewById(R.id.flb_word_question_record_submit);

        fab_foundation_submit = getActivity().findViewById(R.id.flb_word_foundation_submit);


    }

    private void setOnClickListener()
    {
        fab_foundation_submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                String foundation = edit_word_foundation.getText().toString()+'\n';
                String old_foundation = text_word_foundation.getText().toString();

                word_foundation = foundation;

                text_word_foundation.setText(old_foundation+foundation);
                linearLayout.setFocusableInTouchMode(true);
                edit_word_foundation.clearFocus();
                fab_foundation_submit.setVisibility(View.GONE);
                fab_record_submit.setVisibility(View.GONE);

            }
        });
        fab_record_submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                String record = edit_word_question.getText().toString()+'\n';
                String old_rocord = text_word_record.getText().toString();
                text_word_record.setText(old_rocord+record);

                word_question = record;

                linearLayout.setFocusableInTouchMode(true);
                edit_word_question.clearFocus();
                fab_record_submit.setVisibility(View.GONE);
                fab_foundation_submit.setVisibility(View.GONE);
            }
        });

        edit_word_foundation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    fab_foundation_submit.setVisibility(View.VISIBLE);

                }
                else {
                    fab_foundation_submit.setVisibility(View.GONE);

                }
            }
        });

        edit_word_question.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    fab_record_submit.setVisibility(View.VISIBLE);

                } else {
                    fab_record_submit.setVisibility(View.GONE);


                }
            }
            });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        setOnClickListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word, container, false);
    }


}
