package com.example.examination_portal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SIGN_UP_ACTIVITY = "SignUpActivity";
    RadioButton sarbExamee,sarbExameOrganizer;
    EditText saetEmail,saetPassword,saetConfirmPassword,saetOrgName;
    TextView satvLogin;
    Button sabtnRegister;
    ProgressBar loading;

    private String URL_SIGNUP = "http://192.168.0.13/android_scripts/signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        hideActionBar();
        initialization();
        eventsManagement();
    }

    private void eventsManagement() {
        sarbExamee.setOnClickListener(this);
        sarbExameOrganizer.setOnClickListener(this);
        satvLogin.setOnClickListener(this);
        sabtnRegister.setOnClickListener(this);
    }

    private void initialization() {
        sarbExamee = findViewById(R.id.sarbExamee);
        sarbExameOrganizer = findViewById(R.id.sarbExameOrganizer);
        saetOrgName = findViewById(R.id.saetOrgName);
        satvLogin = findViewById(R.id.satvLogin);
        saetEmail = findViewById(R.id.saetEmail);
        saetPassword = findViewById(R.id.saetPassword);
        saetConfirmPassword = findViewById(R.id.saetConfirmPassword);
        sabtnRegister = findViewById(R.id.sabtnRegister);
        loading = findViewById(R.id.loading);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void onClick(View view) {
//        Log.i("TAG",String.valueOf(view.getId()));
        switch(view.getId()){
            case R.id.sarbExameOrganizer :{
//                saetOrgName.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.sarbExamee:{
//                saetOrgName.setVisibility(View.GONE);
                break;
            }
            case R.id.satvLogin :{
                goToLoginActivity();
                break;
            }

            case R.id.sabtnRegister :{
                registerUser();
                break;
            }
        }
    }

    private void registerUser() {

        final String user_email = saetEmail.getText().toString().trim();
        final String user_name = saetOrgName.getText().toString().trim();
        final String user_password = saetPassword.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    loading.setVisibility(View.GONE);
                    Log.e("loginActivity", "onResponse: "+success);

//                    write code to go to next page

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("loginActivity", error.getMessage().toString());
                loading.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_email",user_email);
                params.put("user_password",user_password);
                params.put("user_name",user_name);
                if(sarbExameOrganizer.isChecked()){
                    params.put("user_type","org");
                }else{
                    params.put("user_type","stu");
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void goToLoginActivity() {
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }
}