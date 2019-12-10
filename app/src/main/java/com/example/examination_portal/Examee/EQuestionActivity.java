package com.example.examination_portal.Examee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examination_portal.R;
import com.example.examination_portal.adapter.EQuestionAdapter;
import com.example.examination_portal.adapter.QuestionAdapter;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Question;

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
    private String URL_SHOW_QUESTIONS = Property.domain+"showQuestions.php";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equestion);

        intent = getIntent();

        getAllQuestions();
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
                params.put("question_test_id",String.valueOf(intent.getIntExtra(Property.group_id,0)));
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

    }
}
