package com.swj.prototypealpha.swj;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.swj.util.OnItemClickListener;
import com.swj.prototypealpha.swj.util.SignItemRecyclerView;
import com.venusic.handwrite.view.HandWriteView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.swj.prototypealpha.swj.PictureFragment.pictureList;
import static com.swj.prototypealpha.swj.WordFragment.text_word_foundation;
import static com.swj.prototypealpha.swj.WordFragment.text_word_record;
import static com.swj.prototypealpha.swj.WordFragment.word_foundation;
import static com.swj.prototypealpha.swj.WordFragment.word_question;


public class LookupFragment extends Fragment {
    public static ImageAdapter  adapter;
    public static SignAdapter   signadapter;
    public static List<Picture> signList    = new ArrayList<>();
    public static List<Bitmap>  signbitmaps = new ArrayList<>();
    FloatingActionButton fabtn_lookup;
    HandWriteView        handWriteView;
    RecyclerView         recv_photo;
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

    private SignItemRecyclerView recv_sign;

    private void setBitmap () {
        signbitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sign1));
        signbitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sign2));
        signbitmaps.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sign3));
    }

    @Override
    public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged(hidden);
        if(text_look_foundation!= null)
        {
            String foundation = text_word_foundation.getText().toString();
            text_look_foundation.setText(foundation);
            text_look_foundation.setText(word_foundation);
        }

        if(text_word_record != null)
        {
            String record = text_word_record.getText().toString();
            text_look_rocord.setText(record);
            text_look_rocord.setText(word_question);
        }
        if(pictureList != null)
        {
            if (adapter == null) {
                adapter = new ImageAdapter(getContext(), pictureList, null);
                recv_photo.setAdapter(adapter);
            }
            int len = pictureList.size();
            adapter.notifyItemChanged(0, len);
        }





    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lookup, container, false);
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setBitmap();
        fabtn_lookup = getActivity().findViewById(R.id.fabtn_clear);
        handWriteView = getActivity().findViewById(R.id.handw_view);
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

        recv_photo = getActivity().findViewById(R.id.recv_lookup_picture);
//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recv_photo.setLayoutManager(linearLayoutManager);

        recv_sign = getActivity().findViewById(R.id.recv_lookup_sign);
        GridLayoutManager signManager = new GridLayoutManager(getActivity(), 2);
        recv_sign.setLayoutManager(signManager);

        signadapter = new SignAdapter(signList);

        recv_sign.setAdapter(signadapter);

        recv_sign.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick (int position) {

            }

            @Override
            public void onDeleteClick (int position) {
                final int pos = position;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("照片");
                dialog.setMessage("确认删除吗？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        signadapter.removeItem(pos);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        fabtn_lookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                handWriteView.clear();

                Random random = new Random();
                Picture picture = new Picture(signbitmaps.get(random.nextInt(2)));
                signList.add(picture);
                int len = signList.size();
                signadapter.notifyItemChanged(len - 1);
                signadapter.notifyItemChanged(0, len);
            }
        });
    }
}
