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

public class Userwallet extends AppCompatActivity {
    Toolbar mToolbar;
    DatabaseReference data;
    FirebaseUser cur;
    TextView amount,debt;
    String amount1,debt1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userwallet);
        amount = findViewById(R.id.user_amount);
        mToolbar = (Toolbar) findViewById(R.id.wallet_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Wallet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        cur = FirebaseAuth.getInstance().getCurrentUser();
        String category = getIntent().getStringExtra("category");
        data = FirebaseDatabase.getInstance().getReference().child("Users").child(category).child(cur.getUid());
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                amount1 = dataSnapshot.child("balance").getValue().toString();
                amount.setText(amount1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
