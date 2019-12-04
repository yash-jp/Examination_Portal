package com.example.examination_portal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
//    RadioButton larbExamee,larbExameOrganizer;
    EditText laetEmail,laetPassword;
    Button labtnLogin;
    TextView latvForgotPassword,latvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideActionBar();
        initialization();
        eventsManagement();
    }

    private void eventsManagement() {
        latvRegister.setOnClickListener(this);
        latvForgotPassword.setOnClickListener(this);
    }

    private void initialization() {
//        larbExamee = findViewById(R.id.larbExamee);
//        larbExameOrganizer = findViewById(R.id.larbExameOrganizer);
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
        }
    }

    private void goToRegisterActivity() {
        Intent goToSignUp = new Intent(this, SignUpActivity.class);
        startActivity(goToSignUp);
    }
}
