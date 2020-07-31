package com.svecw.a0563;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private Toolbar mToolBar;
    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private Button mLogin_btn;
    private ProgressDialog mLoginProgress;
    private FirebaseAuth mAuth;
    private String type;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
         spinner = findViewById(R.id.spinner1);
        mLoginEmail = (TextInputLayout) findViewById((R.id.name));
        mLoginPassword =(TextInputLayout) findViewById(R.id.password);
        mLogin_btn = (Button) findViewById(R.id.loginbtn);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = (String) adapterView.getItemAtPosition(i);//
                Toast.makeText(getApplicationContext(),type, Toast.LENGTH_LONG).show();
            }
            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();
                if (!TextUtils.isEmpty(email)  || ! TextUtils.isEmpty(password)){
                    mLoginProgress.setTitle(("Logging In"));
                    mLoginProgress.setMessage("Please wait while we check credentials.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    loginUser(email,password );
                }
            }
        });
    }
    private void loginUser(String email , String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mLoginProgress.dismiss();
                    if(type.equals("student")){
                        Intent intent = new Intent(Login.this, StudentPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else if(type.equals("shop")){
                        Intent intent = new Intent(Login.this, ShopPage.class);
                        intent.putExtra("category","shop");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else if(type.equals("program")){
                        Intent intent = new Intent(Login.this, ProgramPage.class);
                        intent.putExtra("category","program");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(Login.this, WorkshopPage.class);
                        intent.putExtra("category","workshop");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    mLoginProgress.hide();
                    Toast.makeText(Login.this, "Cannot Sign in.Please check the form and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}



