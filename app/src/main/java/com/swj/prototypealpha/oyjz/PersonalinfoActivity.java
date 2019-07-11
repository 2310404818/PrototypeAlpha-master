package com.swj.prototypealpha.oyjz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.swj.prototypealpha.R;
import com.swj.prototypealpha.activity.LoginActivity;
import com.swj.prototypealpha.activity.MainActivity;

public class PersonalinfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public void setToolBar()
    {
        toolbar = findViewById(R.id.text_ps_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);
        setToolBar();
        Button text_ps_modify = (Button) findViewById(R.id.text_ps_modify);
        text_ps_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalinfoActivity.this,MainActivity.class);
                Toast.makeText(PersonalinfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
