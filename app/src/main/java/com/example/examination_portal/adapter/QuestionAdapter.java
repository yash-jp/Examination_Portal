package com.example.examination_portal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examination_portal.R;
import com.example.examination_portal.model.Group;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Question;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {
    private List<Question> questionList;
    private Context context;

//   APIs
    private String URL_QUESTION_UPDATE = Property.domain+"updateQuestion.php";
    private String URL_QUESTION_DELETE =Property.domain+"deleteQuestion.php";

    public QuestionAdapter(List<Question> questionList, Context context) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_row, parent, false);

        return new QuestionAdapter.QuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionHolder holder, final int position) {
        final Question question = questionList.get(position);

        holder.qretQuestionSentence.setText(question.getQuestionSentence());
        holder.qretOptionA.setText(question.getOptionA());
        holder.qretOptionB.setText(question.getOptionB());
        holder.qretOptionC.setText(question.getOptionC());
        holder.qretOptionD.setText(question.getOptionD());
        holder.qretAnswer.setText(question.getQuestionAnswer());

        holder.qrbtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question_id = String.valueOf(questionList.get(position).getQuestionID());
                String question_sentence = holder.qretQuestionSentence.getText().toString().trim();
                String optionA = holder.qretOptionA.getText().toString().trim();
                String optionB = holder.qretOptionB.getText().toString().trim();
                String optionC = holder.qretOptionC.getText().toString().trim();
                String optionD = holder.qretOptionD.getText().toString().trim();
                String answer = holder.qretAnswer.getText().toString().trim();

                updateQuestion(question_id,question_sentence,optionA,optionB,optionC,optionD,answer);
            }
        });

        holder.qrbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question_id = String.valueOf(questionList.get(position).getQuestionID());
                deleteQuestion(question_id);
            }
        });
    }

    private void deleteQuestion(final String question_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_QUESTION_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("message");

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
                params.put("question_id",question_id);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void updateQuestion(final String question_id, final String question_sentence, final String optionA, final String optionB, final String optionC, final String optionD, final String answer) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_QUESTION_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("message");

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
                params.put("question_id",question_id);
                params.put("question_sentence",question_sentence);
                params.put("question_opt_a",optionA);
                params.put("question_opt_b",optionB);
                params.put("question_opt_c",optionC);
                params.put("question_opt_d",optionD);
                params.put("question_answer",answer);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public int getItemCount() {
        return this.questionList.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder{
        public EditText qretQuestionSentence,qretOptionA,qretOptionB,qretOptionC,qretOptionD,qretAnswer;
        Button qrbtnUpdate,qrbtnDelete;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            qretQuestionSentence = itemView.findViewById(R.id.qretQuestionSentence);
            qretOptionA = itemView.findViewById(R.id.qretOptionA);
            qretOptionB = itemView.findViewById(R.id.qretOptionB);
            qretOptionC = itemView.findViewById(R.id.qretOptionC);
            qretOptionD = itemView.findViewById(R.id.qretOptionD);
            qretAnswer = itemView.findViewById(R.id.qretAnswer);
            qrbtnUpdate = itemView.findViewById(R.id.qrbtnUpdate);
            qrbtnDelete = itemView.findViewById(R.id.qrbtnDelete);
        }
    }
}
