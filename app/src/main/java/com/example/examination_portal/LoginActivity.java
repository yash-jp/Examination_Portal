package com.example.examination_portal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examination_portal.model.Property;
import com.example.examination_portal.organizer.GroupActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
//    RadioButton larbExamee,larbExameOrganizer;
    EditText laetEmail,laetPassword;
    Button labtnLogin;
    TextView latvForgotPassword,latvRegister;
    ProgressBar loading;
    Context context;
    RelativeLayout root;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String URL_LOGIN = Property.domain+"login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideActionBar();
        initialization();
        eventsManagement();

//        if user_email has been set inside the sharedpref then directly go to groups page acc to user type
        sharedPreferences = getApplicationContext().getSharedPreferences("UserPref",0);
        editor = sharedPreferences.edit();

        if(sharedPreferences.getString(Property.user_email,null)!=null){
            Intent intent = new Intent(context, GroupActivity.class);
            startActivity(intent);
        }

    }

    private void eventsManagement() {
        latvRegister.setOnClickListener(this);
        latvForgotPassword.setOnClickListener(this);
        labtnLogin.setOnClickListener(this);
    }

    private void initialization() {
//        larbExamee = findViewById(R.id.larbExamee);
//        larbExameOrganizer = findViewById(R.id.larbExameOrganizer);
        context = this;
        root = findViewById(R.id.root);
        loading = findViewById(R.id.loading);
        laetEmail = findViewById(R.id.laetEmail);
        laetPassword = findViewById(R.id.laetPassword);
        labtnLogin = findViewById(R.id.labtnLogin);
        latvForgotPassword = findViewById(R.id.latvForgotPassword);
        latvRegister = findViewById(R.id.latvRegister);

    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.latvRegister:{
                goToRegisterActivity();
                break;
            }

            case R.id.latvForgotPassword:{
                Toast.makeText(this,"Forgot Password?",Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.labtnLogin:{
                performLogin();
            }
        }
    }

    private void performLogin() {
//        make progressbar visible
        loading.setVisibility(View.VISIBLE);

        final String useremail = this.laetEmail.getText().toString().trim();
        final String password = this.laetPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    loading.setVisibility(View.GONE);
                    Log.e("loginActivity", "onResponse: "+message);

//                    write code to go to next page
                    if(message.equals("success")){
                        Intent intent = new Intent(context,GroupActivity.class);
                        editor.putString(Property.user_email,useremail);

//                        committing the changes
                        editor.commit();
                        startActivity(intent);
                    }else{
                        final Snackbar snackbar = Snackbar.make(root, "Not valid username and/or password", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                       snackbar.dismiss();
                                    }
                                });;
                        snackbar.show();
                    }

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
                params.put("useremail",useremail);
                params.put("password",password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void goToRegisterActivity() {
        Intent goToSignUp = new Intent(this, SignUpActivity.class);
        startActivity(goToSignUp);
    }
}
