package com.example.examination_portal.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    QuestionAdapter questionAdapter;
    RecyclerView qarr;
    Button qabtnAdd;

    private List<Question> questionList = new ArrayList<>();
    private String URL_SHOW_QUESTIONS =Property.domain+"showQuestions.php";
    private String URL_ADD_QUESTION =Property.domain+"addQuestion.php";
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
        qabtnAdd.setOnClickListener(this);
    }

    private void initialization() {
        qarr = findViewById(R.id.qarr);
        qabtnAdd = findViewById(R.id.qabtnAdd);
        Log.e("QuestionActivity","size - "+this.questionList.size());
        questionAdapter = new QuestionAdapter(questionList,this,intent.getIntExtra("test_id",0));
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.qabtnAdd:{
                openDialogue();
                break;
            }
        }
    }

    private void openDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Question");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.addquestion_custom_dialogue, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText qretQuestionSentence,qretOptionA,qretOptionB,qretOptionC,qretOptionD,qretAnswer;

                qretQuestionSentence = customLayout.findViewById(R.id.qretQuestionSentence);
                qretOptionA = customLayout.findViewById(R.id.qretOptionA);
                qretOptionB = customLayout.findViewById(R.id.qretOptionB);
                qretOptionC = customLayout.findViewById(R.id.qretOptionC);
                qretOptionD = customLayout.findViewById(R.id.qretOptionD);
                qretAnswer = customLayout.findViewById(R.id.qretAnswer);

                addQuestion(qretQuestionSentence.getText().toString().trim(),
                            qretOptionA.getText().toString().trim(),
                            qretOptionB.getText().toString().trim(),
                            qretOptionC.getText().toString().trim(),
                            qretOptionD.getText().toString().trim(),
                            qretAnswer.getText().toString().trim().toLowerCase());

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addQuestion(final String trim, final String trim1, final String trim2, final String trim3, final String trim4, final String answer) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_QUESTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1")){
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(
                                QuestionActivity.this);
                        builder.setTitle("Message");
                        builder.setMessage("Question Added!");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    }else{
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(
                                QuestionActivity.this);
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
                    getAllQuestions();
                    questionAdapter.notifyDataSetChanged();
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
                params.put("question_test_id",String.valueOf(intent.getIntExtra(Property.test_id,0)));
                params.put("question_sentence",trim);
                params.put("question_opt_a",trim1);
                params.put("question_opt_b",trim2);
                params.put("question_opt_c",trim3);
                params.put("question_opt_d",trim4);
                params.put("question_answer",answer);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
