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
import android.widget.Button;
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
import com.example.examination_portal.adapter.EQuestionAdapter;
import com.example.examination_portal.adapter.QuestionAdapter;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Question;
import com.example.examination_portal.organizer.GroupActivity;
import com.example.examination_portal.organizer.ResultActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EQuestionActivity extends AppCompatActivity {
    RecyclerView eqarv;
//    Button eqabtnSubmit;

    EQuestionAdapter eQuestionAdapter;

    private List<Question> questionList = new ArrayList<>();
    private String URL_CHECK_TEST_GIVEN = Property.domain+"checkTestGiven.php";
    private String URL_SHOW_QUESTIONS = Property.domain+"showQuestions.php";
    Intent intent;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equestion);
        sharedPreferences = this.getApplicationContext().getSharedPreferences("UserPref",0);
        intent = getIntent();
        checkTestGiven();

    }

    private void checkTestGiven() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_TEST_GIVEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if(success.equals("0")){
                        getAllQuestions();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                EQuestionActivity.this);
                        builder.setTitle("Test Message");
                        builder.setMessage("You have already given this test!");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
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
                Log.e("TestActivity", error.getMessage().toString());
//                loading.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("result_user_email",sharedPreferences.getString(Property.user_email,"null"));
                params.put("result_test_id",String.valueOf(intent.getIntExtra(Property.test_id,0)));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getAllQuestions() {
        questionList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOW_QUESTIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()>0){

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        Log.e("QuestionActivity",jsonObject.toString());
                        int questionID = jsonObject.getInt("question_id");
                        int question_test_id = jsonObject.getInt("question_test_id");
                        String questionSentence = jsonObject.getString("question_sentence");
                        String question_opt_a = jsonObject.getString("question_opt_a");
                        String question_opt_b = jsonObject.getString("question_opt_b");
                        String question_opt_c = jsonObject.getString("question_opt_c");
                        String question_opt_d = jsonObject.getString("question_opt_d");
                        String question_answer = jsonObject.getString("question_answer");

//                        Log.e("QuestionActivity",questionSentence);
                        questionList.add(new Question(questionID,question_test_id,questionSentence,question_opt_a,question_opt_b,question_opt_c,question_opt_d,question_answer));
                    }
                    initialization();
                    eventsManagement();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                EQuestionActivity.this);
                        builder.setTitle("Message");
                        builder.setMessage("No test found!");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent intent = new Intent(EQuestionActivity.this,EGroupActivity.class);
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
                Log.e("TestActivity", error.getMessage().toString());
//                loading.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("question_test_id",String.valueOf(intent.getIntExtra(Property.test_id,0)));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void eventsManagement() {
    }

    private void initialization() {
        eqarv = findViewById(R.id.eqarv);
//        eqabtnSubmit = findViewById(R.id.eqabtnSubmit);

        eQuestionAdapter = new EQuestionAdapter(questionList,this,intent);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        eqarv.setLayoutManager(mLayoutManager);
        eqarv.setItemAnimator(new DefaultItemAnimator());
        eqarv.setAdapter(eQuestionAdapter);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.profile:{
                        Toast.makeText(EQuestionActivity.this,"PROFILE COMING SOON",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.groups:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(EQuestionActivity.this,EGroupActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(EQuestionActivity.this, GroupActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.result:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(EQuestionActivity.this,EResultActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(EQuestionActivity.this, ResultActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.logout:{
                        SharedPreferences preferences = getSharedPreferences("UserPref", 0);
                        preferences.edit().clear().commit();
                        intent = new Intent(EQuestionActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

    }
}
