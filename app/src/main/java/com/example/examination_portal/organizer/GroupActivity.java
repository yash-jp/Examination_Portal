package com.example.examination_portal.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examination_portal.Examee.EGroupActivity;
import com.example.examination_portal.Examee.EResultActivity;
import com.example.examination_portal.LoginActivity;
import com.example.examination_portal.R;
import com.example.examination_portal.adapter.GroupAdapter;
import com.example.examination_portal.model.Group;
import com.example.examination_portal.model.Property;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText gaetGroupName;
    Button gabtnAdd;
    RecyclerView garv;
    GroupAdapter groupAdapter;
    private List<Group> groupList = new ArrayList<>();
    private String URL_GROUPLISTING = Property.domain+"showGroups.php";
    private String URL_ADDGROUP = Property.domain+"addGroup.php";

//    SHAREDPREFERENCE
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

//        get shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("UserPref",0);

        hideActionBar();
        getAllGroups();


    }

    private void getAllGroups() {
        groupList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GROUPLISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e("groupActivity",jsonObject.toString());
                        int groupID = jsonObject.getInt("group_id");
                        String groupName = jsonObject.getString("group_name");
                        groupList.add(new Group(groupID,groupName));
                    }
                    initialization();
                    eventsManagement();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("loginActivity", error.getMessage().toString());
//                loading.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("group_email",sharedPreferences.getString(Property.user_email,null));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void eventsManagement() {
        gabtnAdd.setOnClickListener(this);
    }

    private void initialization() {
        gaetGroupName = findViewById(R.id.gaetGroupName);
        gabtnAdd = findViewById(R.id.gabtnAdd);
        garv = findViewById(R.id.garv);
//        groupList = new ArrayList<>();
//        this will fetch all the data from api
//        getAllGroups();
//        setup of recyclerview
//        loadData();
        Log.e("GroupActivity","size - "+this.groupList.size());
        groupAdapter = new GroupAdapter(groupList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        garv.setLayoutManager(mLayoutManager);
        garv.setItemAnimator(new DefaultItemAnimator());
        garv.setAdapter(groupAdapter);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.profile:{
                        Toast.makeText(GroupActivity.this,"PROFILE COMING SOON",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.groups:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(GroupActivity.this,EGroupActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(GroupActivity.this, GroupActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.result:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(GroupActivity.this, EResultActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(GroupActivity.this, ResultActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.logout:{
                        SharedPreferences preferences = getSharedPreferences("UserPref", 0);
                        preferences.edit().clear().commit();
                        intent = new Intent(GroupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    private void loadData() {
        for(int i=0;i<=10;i++){
            groupList.add(new Group(1,"demo"));
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
        final String group_name = gaetGroupName.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDGROUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                     JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
//                    loading.setVisibility(View.GONE);
//                    Log.e("groupActivity", "onResponse: "+success);

//                    on success update the recyclerview
                    getAllGroups();
                    groupAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("groupActivity", error.getMessage().toString());
//                loading.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("group_email",sharedPreferences.getString(Property.user_email,null));
                params.put("group_name",group_name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
