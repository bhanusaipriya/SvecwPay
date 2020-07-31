package com.svecw.a0563;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Wallet extends AppCompatActivity {
    Toolbar mToolbar;
    DatabaseReference data;
    FirebaseUser cur;
    TextView amount,debt;
    String amount1,debt1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);
        amount = findViewById(R.id.user_amount);
        debt = findViewById(R.id.damount);
        mToolbar = (Toolbar) findViewById(R.id.wallet_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Wallet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cur = FirebaseAuth.getInstance().getCurrentUser();
        data = FirebaseDatabase.getInstance().getReference().child("Users").child("student").child(cur.getUid());
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                amount1 = dataSnapshot.child("balance").getValue().toString();
                debt1 = dataSnapshot.child("debt").getValue().toString();
                amount.setText(amount1);
                debt.setText(debt1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
