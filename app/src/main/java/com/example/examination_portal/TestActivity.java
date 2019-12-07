package com.example.examination_portal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examination_portal.adapter.GroupAdapter;
import com.example.examination_portal.adapter.TestAdapter;
import com.example.examination_portal.model.Group;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity {
    private RecyclerView tarv;
    private TestAdapter testAdapter;

    private List<Test> testList = new ArrayList<>();
//    private String URL_ADDTEST = "http://192.168.0.13/android_scripts/showGroups.php";
    private String URL_SHOWTEST = "http://192.168.0.13/android_scripts/showTests.php";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

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
    }

    private void initialization() {
        tarv = findViewById(R.id.tarr);

        Log.e("TestActivity","size - "+this.testList.size());
        testAdapter = new TestAdapter(testList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        tarv.setLayoutManager(mLayoutManager);
        tarv.setItemAnimator(new DefaultItemAnimator());
        tarv.setAdapter(testAdapter);
    }
}
