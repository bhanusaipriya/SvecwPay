package com.svecw.a0563;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class Addmoney extends AppCompatActivity {
    private Toolbar mtbar;
    private TextView t;
    private TextInputLayout amount,debt;
    private Button btn;
    DatabaseReference mUserDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney);
        mtbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(mtbar);
        getSupportActionBar().setTitle("Add money");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        amount = (TextInputLayout) findViewById((R.id.amount));
        debt =(TextInputLayout) findViewById(R.id.debt);
         btn= (Button) findViewById(R.id.button);
         t = findViewById(R.id.debtdisplay);
        final String regdno = getIntent().getStringExtra("regdno");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("student");
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    if(d.child("regdno").getValue().toString().contentEquals(regdno)){
                        t.setText("YOUR CURRENT DEBT VALUE IS"+"  "+d.child("debt").getValue().toString()+"/-");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                  String money = amount.getEditText().getText().toString();
                  String debtgiven = debt.getEditText().getText().toString();
                 final int m;
                 final int de;
                 if((TextUtils.isEmpty(money))){
                     m = 0;
                 }
                 else m = Integer.parseInt(money);
                 if((TextUtils.isEmpty(debtgiven))){
                     de = 0;
                 }
                 else de = Integer.parseInt(debtgiven);
                 mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             for (final DataSnapshot d : dataSnapshot.getChildren()) {
                                 int bal = 0;
                                 if (d.child("regdno").getValue().toString().contentEquals(regdno)) {
                                     bal = Integer.parseInt(d.child("balance").getValue().toString());
                                     final int debtt = Integer.parseInt(d.child("debt").getValue().toString());
                                     if ((debtt - de) < 0) {
                                         Toast.makeText(getApplicationContext(), "sorry!Give the proper debt value", Toast.LENGTH_LONG).show();
                                     } else {
                                         HashMap<String, String> userMap = new HashMap<>();
                                         userMap.put("image",String.valueOf(d.child("image").getValue()));
                                         userMap.put("thumb_image",String.valueOf(d.child("thumb_image").getValue()));
                                         userMap.put("regdno",String.valueOf(d.child("regdno").getValue()));
                                         userMap.put("name",String.valueOf(d.child("name").getValue()));
                                         userMap.put("email",String.valueOf(d.child("email").getValue()));
                                         userMap.put("balance", String.valueOf(bal + m));
                                         userMap.put("debt", String.valueOf(debtt - de));
                                         mUserDatabase = d.getRef();
                                         mUserDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if (task.isSuccessful()) {
                                                     Toast.makeText(getApplicationContext(), "Sucessfully updated", Toast.LENGTH_LONG).show();
                                                     t.setText("YOUR CURRENT DEBT VALUE IS"+"  "+(debtt-de)+"/-");
                                                 }
                                             }
                                         });
                                     }
                                 }
                             }
                         }
                             @Override
                             public void onCancelled (@NonNull DatabaseError databaseError){
                             }
                 });
             }
         });
    }
}
