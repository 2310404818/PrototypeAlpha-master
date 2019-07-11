package com.swj.prototypealpha.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.swj.prototypealpha.R;

public class PolicyinfoActivity extends AppCompatActivity {
    private Toolbar toolbarPolicyinfo;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policyinfo);
        initUI();
    }
    private void initUI()
    {
        toolbarPolicyinfo = findViewById(R.id.toolbarPolicyinfo);
        setSupportActionBar(toolbarPolicyinfo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
