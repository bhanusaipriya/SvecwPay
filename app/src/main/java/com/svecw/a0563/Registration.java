package com.svecw.a0563;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
public class Registration extends AppCompatActivity {
    private TextInputLayout name, regdno, email, password;
    private FirebaseAuth mAuth;
    private ProgressDialog mRegProgress;
    private DatabaseReference mDatabase;
    private Button regbtn;
    String debt="0",amount="0";
    private Toolbar mtbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mRegProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mtbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mtbar);
        getSupportActionBar().setTitle("Register User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.name);
        regdno = findViewById(R.id.regdno);
        email = findViewById(R.id.email);
        regbtn = findViewById(R.id.regbtn);
        password = findViewById(R.id.password);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regdno1 = regdno.getEditText().getText().toString();
                String email1 = email.getEditText().getText().toString();
                String name1 = name.getEditText().getText().toString();
                String p = password.getEditText().getText().toString();
                //Toast.makeText(getApplicationContext(),p,Toast.LENGTH_LONG).show();
                if (!TextUtils.isEmpty(regdno1) || !TextUtils.isEmpty(email1) || !TextUtils.isEmpty(p) || !TextUtils.isEmpty(name1)) {
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your Account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(regdno1, email1, p, name1);
                } else {
                    Toast.makeText(getApplicationContext(), "fill the details properly", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    private void register_user(final String regdno, final String email, final String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();
                            //Toast.makeText(getApplicationContext(),email+" "+password,Toast.LENGTH_LONG).show();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("student").child(uid);
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("regdno", regdno);
                            userMap.put("email", email);
                            userMap.put("debt",debt);
                            userMap.put("balance",amount);
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mRegProgress.dismiss();
                                        Toast.makeText(getApplicationContext(),"Registered suceessfully",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(),Adminpage.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            mRegProgress.hide();
                            Toast.makeText(getApplicationContext(), "Cannot SIgn in , Please check the form and try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}