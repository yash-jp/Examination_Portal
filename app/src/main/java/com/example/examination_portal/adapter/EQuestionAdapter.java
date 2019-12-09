package com.example.examination_portal.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.examination_portal.model.Answer;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.model.Question;
import com.example.examination_portal.model.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EQuestionAdapter extends RecyclerView.Adapter<EQuestionAdapter.EQuestionHolder> {
    private List<Question> questionList;
//    private List<Answer> answers;
    private Context context;
    private Intent intent;


//    SHAREDPREFERENCE
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String URL_ADDRESULT = "http://10.111.2.226/android_scripts/addResult.php";

    float percentage;

    public EQuestionAdapter(List<Question> questionList, Context context,Intent intent) {
        this.questionList = questionList;
        this.context = context;
        this.intent = intent;

//        get shared preference
        sharedPreferences = context.getApplicationContext().getSharedPreferences("UserPref",0);
    }

    @NonNull
    @Override
    public EQuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.equestion_row, parent, false);

        return new EQuestionAdapter.EQuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EQuestionHolder holder, final int position) {
        final Question question = questionList.get(position);

        holder.eqaQuestion.setText(question.getQuestionSentence());
        holder.eqaOptionA.setText(question.getOptionA());
        holder.eqaOptionB.setText(question.getOptionB());
        holder.eqaOptionC.setText(question.getOptionC());
        holder.eqaOptionD.setText(question.getOptionD());

        if(position == questionList.size()-1){
            holder.eqabtnSubmit.setVisibility(View.VISIBLE);
        }

        holder.eqaRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.eqaOptionA:{
                        question.setAnswerSelected("a");
                        questionList.set(position, question);
                        break;
                    }
                    case R.id.eqaOptionB:{
                        question.setAnswerSelected("b");
                        questionList.set(position, question);
                        break;
                    }
                    case R.id.eqaOptionC:{
                        question.setAnswerSelected("c");
                        questionList.set(position, question);
                        break;
                    }
                    case R.id.eqaOptionD:{
                        question.setAnswerSelected("d");
                        questionList.set(position, question);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class EQuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView eqaQuestion;
        RadioButton eqaOptionA,eqaOptionB,eqaOptionC,eqaOptionD;
        Button eqabtnSubmit;
        RadioGroup eqaRG;

        public EQuestionHolder(@NonNull View itemView) {
            super(itemView);
            eqaQuestion = itemView.findViewById(R.id.eqaQuestion);
            eqaOptionA = itemView.findViewById(R.id.eqaOptionA);
            eqaOptionB = itemView.findViewById(R.id.eqaOptionB);
            eqaOptionC = itemView.findViewById(R.id.eqaOptionC);
            eqaOptionD = itemView.findViewById(R.id.eqaOptionD);
            eqabtnSubmit = itemView.findViewById(R.id.eqabtnSubmit);
            eqaRG = itemView.findViewById(R.id.eqaRG);

            eqabtnSubmit.setOnClickListener(this);
//            eqaRG.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.eqabtnSubmit:{
                    int total_score = 0;
//                    Toast.makeText(context,String.valueOf(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    for (int i = 0; i <questionList.size() ; i++) {
                        if(questionList.get(i).getAnswerSelected().equalsIgnoreCase(questionList.get(i).getQuestionAnswer())){
                            total_score++;
                        }
                    }

                    percentage =((float)total_score/questionList.size())*100;
                    makeEntryInResultsTable();
                }
            }
        }

        private void makeEntryInResultsTable() {
//            testList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDRESULT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
//                        loading.setVisibility(View.GONE);
//                        Log.e("loginActivity", "onResponse: "+success);

//                    write code to go to next page

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    try{


//                        for(int i=0;i<jsonArray.length();i++){
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            Log.e("TestActivity",jsonObject.toString());
//                            int testID = jsonObject.getInt("test_id");
//                            String testName = jsonObject.getString("test_name");
//                            Log.e("TestActivity",testName);
////                            testList.add(new Test(testID,testName));
//                        }
//                        initialization();
//                        eventsManagement();
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
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
                    params.put("result_test_id",String.valueOf(intent.getIntExtra("test_id",0)));
                    params.put("result_user_email",sharedPreferences.getString(Property.user_email,null));
                    params.put("result_score",String.valueOf(percentage));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }
}
