package com.example.examination_portal.Examee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.example.examination_portal.LoginActivity;
import com.example.examination_portal.R;
import com.example.examination_portal.adapter.TestAdapter;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Test;
import com.example.examination_portal.organizer.GroupActivity;
import com.example.examination_portal.organizer.ResultActivity;
import com.example.examination_portal.organizer.TestActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ETestActivity extends AppCompatActivity {
    RecyclerView etarv;
    private TestAdapter testAdapter;


    private List<Test> testList = new ArrayList<>();
    private String URL_SHOWTEST = Property.domain+"showTests.php";

    Intent intent;

    //    SHAREDPREFERENCE
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etest);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserPref",0);
        getAllTests();

        intent = getIntent();
        Log.e("TestActivity",String.valueOf(intent.getIntExtra("group_id",0)));

    }

    private void eventsManagement() {
    }

    private void initialization() {
        etarv = findViewById(R.id.etarv);
        testAdapter = new TestAdapter(testList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        etarv.setLayoutManager(mLayoutManager);
        etarv.setItemAnimator(new DefaultItemAnimator());
        etarv.setAdapter(testAdapter);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.profile:{
                        Toast.makeText(ETestActivity.this,"PROFILE COMING SOON",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.groups:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(ETestActivity.this, EGroupActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(ETestActivity.this, GroupActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.result:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(ETestActivity.this, EResultActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(ETestActivity.this, ResultActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.logout:{
                        SharedPreferences preferences = getSharedPreferences("UserPref", 0);
                        preferences.edit().clear().commit();
                        intent = new Intent(ETestActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    private void getAllTests() {
        testList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOWTEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e("TestActivity",jsonObject.toString());
                        int testID = jsonObject.getInt("test_id");
                        String testName = jsonObject.getString("test_name");
                        Log.e("TestActivity",testName);
                        testList.add(new Test(testID,testName));
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
                Log.e("TestActivity", error.getMessage().toString());
//                loading.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("test_group_id",String.valueOf(intent.getIntExtra(Property.group_id,0)));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
