package com.svecw.a0563;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class AdminLogin extends AppCompatActivity {
    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mLoginEmail = (TextInputLayout) findViewById((R.id.email));
        mLoginPassword =(TextInputLayout) findViewById(R.id.password);
        btn = findViewById(R.id.loginbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();
                if(email.contentEquals("admin") && password.contentEquals("admin")){
                    Intent intent = new Intent(getApplicationContext(),Adminpage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
