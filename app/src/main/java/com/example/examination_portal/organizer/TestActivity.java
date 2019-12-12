package com.example.examination_portal.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.examination_portal.adapter.TestAdapter;
import com.example.examination_portal.model.Group;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Test;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView tarv;
    private TestAdapter testAdapter;
    private EditText taetTestName;
    private Button tabtnTestAdd;

    private List<Test> testList = new ArrayList<>();
    private String URL_SHOWTEST =Property.domain+"showTests.php";
    private String URL_ADDTEST = Property.domain+"addTest.php";
    Intent intent;

//    SHAREDPREFERENCE
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        get shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("UserPref",0);
        getAllTests();

        intent = getIntent();
        Log.e("TestActivity",String.valueOf(intent.getIntExtra("group_id",0)));
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

    private void eventsManagement() {
        tabtnTestAdd.setOnClickListener(this);
    }

    private void initialization() {
        tarv = findViewById(R.id.tarv);
        tabtnTestAdd = findViewById(R.id.tabtnTestAdd);
        taetTestName = findViewById(R.id.taetTestName);

        Log.e("TestActivity","size - "+this.testList.size());
        testAdapter = new TestAdapter(testList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        tarv.setLayoutManager(mLayoutManager);
        tarv.setItemAnimator(new DefaultItemAnimator());
        tarv.setAdapter(testAdapter);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.profile:{
                        Toast.makeText(TestActivity.this,"PROFILE COMING SOON",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.groups:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(TestActivity.this, EGroupActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(TestActivity.this, GroupActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.result:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(TestActivity.this, EResultActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(TestActivity.this, ResultActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.logout:{
                        SharedPreferences preferences = getSharedPreferences("UserPref", 0);
                        preferences.edit().clear().commit();
                        intent = new Intent(TestActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tabtnTestAdd:{
                if(doValidation()){
                    addTest();
                }
            }
        }
    }

    private void addTest() {
        final String test_name = taetTestName.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDTEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
//                    loading.setVisibility(View.GONE);
//                    Log.e("groupActivity", "onResponse: "+success);

//                    on success update the recyclerview
                    if(success.equals("1")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    TestActivity.this);
                            builder.setTitle("Message");
                            builder.setMessage("Test Added!");
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.show();
                    }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        TestActivity.this);
                                builder.setTitle("Message");
                                builder.setMessage("Please try again!");
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                builder.show();
                    }
                    getAllTests();
                    testAdapter.notifyDataSetChanged();

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
                params.put("test_group_id",String.valueOf(intent.getIntExtra("group_id",0)));
                params.put("test_name",test_name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean doValidation() {
        if(taetTestName.getText().toString().equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    TestActivity.this);
            builder.setTitle("Message");
            builder.setMessage("Please enter test name!");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return false;
        }else{
            return true;
        }
    }
}
