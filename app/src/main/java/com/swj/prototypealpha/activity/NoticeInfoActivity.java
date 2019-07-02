package com.swj.prototypealpha.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.swj.prototypealpha.Enity.NoticeEntity;
import com.swj.prototypealpha.R;

public class NoticeInfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_info);
        Intent intent =getIntent();
        NoticeEntity entity =(NoticeEntity) intent.getSerializableExtra("noticeinfo");
        if (entity != null)
        {
            TextView time = findViewById(R.id.tv_noticeinfo_time);
            TextView title = findViewById(R.id.tv_noticeinfo_title);
            TextView sender = findViewById(R.id.tv_noticeinfo_sender);
            TextView fromer = findViewById(R.id.tv_noticeinfo_fromer);
            TextView content = findViewById(R.id.tv_noticeinfo_context);
            time.setText("时间："+entity.getTime());
            title.setText(entity.getTitle());
            sender.setText(entity.getSender());
            fromer.setText(entity.getFromer());
            content.setText(entity.getText() );
            time.setMovementMethod(ScrollingMovementMethod.getInstance());
            title.setMovementMethod(ScrollingMovementMethod.getInstance());
            sender.setMovementMethod(ScrollingMovementMethod.getInstance());
            fromer.setMovementMethod(ScrollingMovementMethod.getInstance());
            content.setMovementMethod(ScrollingMovementMethod.getInstance());

        }


    }
}
