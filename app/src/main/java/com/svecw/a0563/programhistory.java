package com.svecw.a0563;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class programhistory extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView muserslist;
    FirebaseUser mcurrentuser;
    List<String> key;
    List<String> keynames;
    List<Transactions> objlist;
    List<DatabaseReference> l;
    int pos =0;
    FirebaseRecyclerAdapter<Transactions, usersviewHolder> firebaseRecyclerAdapter=null;
    private DatabaseReference mUserDatabase,mprogram;
     String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programhistory);
        mToolbar = (Toolbar) findViewById(R.id.pay_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Programs history");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        objlist = new ArrayList<>();
        mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        mprogram = FirebaseDatabase.getInstance().getReference().child("Users").child("program");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Transactions").child("student").child(mcurrentuser.getUid()).child("program");
        muserslist = (RecyclerView) findViewById(R.id.reclist);
        muserslist.setHasFixedSize(false);
        muserslist.addItemDecoration(new DividerItemDecoration(muserslist.getContext(), DividerItemDecoration.VERTICAL));
        muserslist.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected  void onStart() {
        super.onStart();
        final DatabaseReference program = FirebaseDatabase.getInstance().getReference().child("Users").child("program");
         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Transactions, usersviewHolder>(
                Transactions.class,
                R.layout.history,
                usersviewHolder.class,
                mUserDatabase
        ) {


             @Override
            protected void populateViewHolder(final usersviewHolder usersviewHolder, Transactions transactions, final int i) {
                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<ArrayList<String>>  ar = new ArrayList<>();
                        int p=0;
                        for (final DataSnapshot d : dataSnapshot.getChildren()) {
                            if((p++) == i) {
                                program.child(d.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        name = dataSnapshot.child("name").getValue().toString();
                                        int l =0;
                           for (DataSnapshot p : d.getChildren()) {
                               String amount = d.child(String.valueOf((++l))+"_key").child("amount").getValue().toString();
                               String type = d.child(String.valueOf((l))+"_key").child("type").getValue().toString();
                               String date = d.child(String.valueOf((l))+"_key").child("date").getValue().toString();
                               usersviewHolder.setDisplayName(name);
                               usersviewHolder.setDate(date);
                               usersviewHolder.setAmount(amount,type);
                              // Toast.makeText(getApplicationContext(), " "+" "+name + " " + amount + " " + type + " " + date, Toast.LENGTH_LONG).show();
                            }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        };
        muserslist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class usersviewHolder extends RecyclerView.ViewHolder {
        View mView;
        public usersviewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDisplayName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }
        public void setDate(String date) {
            TextView userNameView = (TextView) mView.findViewById(R.id.date);
            userNameView.setText(date);
        }
        public void setAmount(String name,String type) {
            TextView userNameView = (TextView) mView.findViewById(R.id.user_amount);
            if(type.equals("sent")){
                userNameView.setTextColor(Color.parseColor("#FF0000"));
            }
            else{
                userNameView.setTextColor(Color.parseColor("#008000"));
            }
            userNameView.setText(name);
        }

    }
}