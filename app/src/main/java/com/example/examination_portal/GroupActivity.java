package com.example.examination_portal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.RecoverySystem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.examination_portal.adapter.GroupAdapter;
import com.example.examination_portal.model.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText gaetGroupName;
    Button gabtnAdd;
    RecyclerView garv;
    GroupAdapter groupAdapter;
    private List<Group> groupList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        hideActionBar();
        initialization();
        eventsManagement();
        getAllGroups();
    }

    private void getAllGroups() {
        
    }

    private void eventsManagement() {
        gabtnAdd.setOnClickListener(this);
    }

    private void initialization() {
        gaetGroupName = findViewById(R.id.gaetGroupName);
        gabtnAdd = findViewById(R.id.gabtnAdd);
        garv = findViewById(R.id.garv);

//        setup of recyclerview
        loadData();
        groupAdapter = new GroupAdapter(groupList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        garv.setLayoutManager(mLayoutManager);
        garv.setItemAnimator(new DefaultItemAnimator());
        garv.setAdapter(groupAdapter);
    }

    private void loadData() {
        for(int i=0;i<=10;i++){
            groupList.add(new Group("demo"));
        }
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gabtnAdd:{
                addGroup();
                break;
            }
        }
    }

    private void addGroup() {
    }
}
