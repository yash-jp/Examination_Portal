package com.example.examination_portal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SIGN_UP_ACTIVITY = "SignUpActivity";
    RadioButton sarbExamee,sarbExameOrganizer;
    EditText saetEmail,saetPassword,saetConfirmPassword,saetOrgName;
    TextView satvLogin;
    Button sabtnRegister;

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
                saetOrgName.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.sarbExamee:{
                saetOrgName.setVisibility(View.GONE);
                break;
            }
            case R.id.satvLogin :{
                goToLoginActivity();
                break;
            }

            case R.id.sabtnRegister :{
                Log.e(SIGN_UP_ACTIVITY,"email - "+saetEmail.getText().toString());
                Log.e(SIGN_UP_ACTIVITY,"password - "+saetPassword.getText().toString());
                Log.e(SIGN_UP_ACTIVITY,"confirm password - "+saetConfirmPassword.getText().toString());
                Log.e(SIGN_UP_ACTIVITY,"name - "+saetOrgName.getText().toString());
                Log.e(SIGN_UP_ACTIVITY,"type - "+sarbExamee.getText().toString());
                Log.e(SIGN_UP_ACTIVITY,"type - "+sarbExameOrganizer.getText().toString());
                break;
            }
        }
    }


    private void goToLoginActivity() {
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }
}