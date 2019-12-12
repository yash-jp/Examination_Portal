package com.example.examination_portal.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.examination_portal.Examee.EGroupActivity;
import com.example.examination_portal.Examee.EResultActivity;
import com.example.examination_portal.LoginActivity;
import com.example.examination_portal.R;
import com.example.examination_portal.model.OResult;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Result;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    PieChart chart;
    BottomNavigationView bottomNavigationView;

    public List<OResult> resultList = new ArrayList<>();
    private String URL_GET_ALL_RESULT = Property.domain+"getAllResults.php";

    //    GRAPH
    public List<PieEntry> entries = new ArrayList<>();

//    SHAREDPREFERENCE
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
//        get shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("UserPref",0);
        getResult();
    }

    private void getResult() {
        resultList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_ALL_RESULT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String testName = jsonObject.getString("test_name");
                            int testScore = jsonObject.getInt("result_score");
                            String userName = jsonObject.getString("result_user_email");
                            resultList.add(new OResult(testName,testScore,userName));
                        }
                        initialization();
                        eventsManagement();
                        initializeGraph();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                ResultActivity.this);
                        builder.setTitle("Message");
                        builder.setMessage("Students have not given test");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent intent = new Intent(ResultActivity.this,GroupActivity.class);
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
        });
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("result_user_email",sharedPreferences.getString(Property.user_email,""));
//                return params;
//            }
//        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void initializeGraph() {
        for (int i=0;i<resultList.size();i++) {
            OResult z=resultList.get(i);
            entries.add(new PieEntry((float)z.getTestScore(),z.getUserName()));
        }
        PieDataSet barDataSet = new PieDataSet(entries,"Tests");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setDrawIcons(true);
        PieData barData = new PieData(barDataSet);
        chart.setData(barData);
//        YAxis y = erachart.getAxisRight();
//        y.setAxisMinimum(0f);
        chart.invalidate();
        chart.animateY(500);
    }

    private void eventsManagement() {
    }

    private void initialization() {
        chart = (PieChart) findViewById(R.id.chart);
        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.profile:{
                        Toast.makeText(ResultActivity.this,"PROFILE COMING SOON",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.groups:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(ResultActivity.this, EGroupActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(ResultActivity.this, GroupActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.result:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(ResultActivity.this,EResultActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(ResultActivity.this, ResultActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.logout:{
                        SharedPreferences preferences = getSharedPreferences("UserPref", 0);
                        preferences.edit().clear().commit();
                        intent = new Intent(ResultActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }
}
