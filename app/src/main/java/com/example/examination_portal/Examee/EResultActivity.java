package com.example.examination_portal.Examee;

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
import com.example.examination_portal.adapter.EResultAdapter;
import com.example.examination_portal.adapter.GroupAdapter;
import com.example.examination_portal.model.Group;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Result;
import com.example.examination_portal.model.ResultEntry;
import com.example.examination_portal.organizer.GroupActivity;
import com.example.examination_portal.organizer.ResultActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EResultActivity extends AppCompatActivity {
    RecyclerView erarv;
    BarChart erachart;

//    SHAREDPREFERENCE
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EResultAdapter eResultAdapter;
    public List<Result> resultList = new ArrayList<>();
    private String URL_GETRESULT = Property.domain+"getResults.php";

//    GRAPH
    public List<BarEntry> entries = new ArrayList<>();

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eresult);

//        get shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("UserPref",0);

        getResult();
    }

    private void eventsManagement() {
    }

    private void initialization() {
        erarv = findViewById(R.id.erarv);
        eResultAdapter= new EResultAdapter(resultList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        erarv.setLayoutManager(mLayoutManager);
        erarv.setItemAnimator(new DefaultItemAnimator());
        erarv.setAdapter(eResultAdapter);

        erachart = (BarChart) findViewById(R.id.erachart);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.profile:{
                        Toast.makeText(EResultActivity.this,"PROFILE COMING SOON",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.groups:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(EResultActivity.this,EGroupActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(EResultActivity.this, GroupActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.result:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(EResultActivity.this,EResultActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(EResultActivity.this, ResultActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.logout:{
                        SharedPreferences preferences = getSharedPreferences("UserPref", 0);
                        preferences.edit().clear().commit();
                        intent = new Intent(EResultActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        
        initializeGraph();
    }

    private void initializeGraph() {
        for (int i=0;i<resultList.size();i++) {
            Result z=resultList.get(i);
            entries.add(new BarEntry(i,(float)z.getTestScore()));
        }
        BarDataSet barDataSet = new BarDataSet(entries,"Score");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setDrawIcons(true);
        BarData barData = new BarData(barDataSet);
        erachart.setData(barData);
        YAxis y = erachart.getAxisRight();
        y.setAxisMinimum(0f);
        erachart.invalidate();
        erachart.animateY(2500);
    }

    private void getResult() {
        resultList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETRESULT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String testName = jsonObject.getString("test_name");
                            int testScore = jsonObject.getInt("result_score");
                            resultList.add(new Result(testName,testScore));
                        }
                        initialization();
                        eventsManagement();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                EResultActivity.this);
                        builder.setTitle("Message");
                        builder.setMessage("You have not given any test!");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent intent = new Intent(EResultActivity.this,EGroupActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        builder.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("loginActivity", error.getMessage().toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("result_user_email",sharedPreferences.getString(Property.user_email,""));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
