package com.example.examination_portal.organizer;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examination_portal.Examee.EGroupActivity;
import com.example.examination_portal.Examee.EQuestionActivity;
import com.example.examination_portal.Examee.EResultActivity;
import com.example.examination_portal.LoginActivity;
import com.example.examination_portal.R;
import com.example.examination_portal.adapter.QuestionAdapter;
import com.example.examination_portal.adapter.TestAdapter;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Question;
import com.example.examination_portal.model.Test;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {
    QuestionAdapter questionAdapter;
    RecyclerView qarr;

    private List<Question> questionList = new ArrayList<>();
    private String URL_SHOW_QUESTIONS =Property.domain+"showQuestions.php";
    Intent intent;

    //    SHAREDPREFERENCE
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
//        get shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("UserPref",0);
        intent = getIntent();

        getAllQuestions();



//        Log.e("QuestionActivity",String.valueOf(intent.getIntExtra("test_id",0)));
    }

    private void eventsManagement() {
    }

    private void initialization() {
        qarr = findViewById(R.id.qarr);

        Log.e("QuestionActivity","size - "+this.questionList.size());
        questionAdapter = new QuestionAdapter(questionList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        qarr.setLayoutManager(mLayoutManager);
        qarr.setItemAnimator(new DefaultItemAnimator());
        qarr.setAdapter(questionAdapter);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.profile:{
                        Toast.makeText(QuestionActivity.this,"PROFILE COMING SOON",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.groups:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(QuestionActivity.this,EGroupActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(QuestionActivity.this, GroupActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.result:{
                        if(sharedPreferences.getString(Property.user_type,"null").equals("stu")){
                            intent = new Intent(QuestionActivity.this, EResultActivity.class);
                            startActivity(intent);
                        }else{
                            intent = new Intent(QuestionActivity.this, ResultActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                    case R.id.logout:{
                        SharedPreferences preferences = getSharedPreferences("UserPref", 0);
                        preferences.edit().clear().commit();
                        intent = new Intent(QuestionActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    private void getAllQuestions() {
        questionList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOW_QUESTIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e("QuestionActivity",jsonObject.toString());
                        int questionID = jsonObject.getInt("question_id");
                        int question_test_id = jsonObject.getInt("question_test_id");
                        String questionSentence = jsonObject.getString("question_sentence");
                        String question_opt_a = jsonObject.getString("question_opt_a");
                        String question_opt_b = jsonObject.getString("question_opt_b");
                        String question_opt_c = jsonObject.getString("question_opt_c");
                        String question_opt_d = jsonObject.getString("question_opt_d");
                        String question_answer = jsonObject.getString("question_answer");

                        Log.e("QuestionActivity",questionSentence);
                        questionList.add(new Question(questionID,question_test_id,questionSentence,question_opt_a,question_opt_b,question_opt_c,question_opt_d,question_answer));
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
                params.put("question_test_id",String.valueOf(intent.getIntExtra(Property.test_id,0)));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
