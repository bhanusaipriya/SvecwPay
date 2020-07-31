package com.svecw.a0563;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayUsers extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView muserslist;
    DatabaseReference mUserDatabase;
    List<String> list;
    RecyclerView rec;
    static  int i=0;
    static int[] myImageList ;
    ArrayList<Integer> myImage = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);
        mToolbar = (Toolbar) findViewById(R.id.pay_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rec = findViewById(R.id.reclist);
        rec.addItemDecoration(new DividerItemDecoration(rec.getContext(), DividerItemDecoration.VERTICAL));
        list = new ArrayList<String>();
        muserslist = (RecyclerView) findViewById(R.id.reclist);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        muserslist.setHasFixedSize(true);
        myImage.add(R.drawable.shop);
        myImageList = new int[]{R.drawable.program, R.drawable.shop,R.drawable.friends,R.drawable.workshop};
        muserslist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total = (int) dataSnapshot.getChildrenCount();
                int count = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    list.add(d.getKey().toString());
                    //  Toast.makeText(getApplicationContext(),+total+" "+i+" "+temp, Toast.LENGTH_SHORT).show()
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        FirebaseRecyclerAdapter<Users, usersviewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, usersviewHolder>(
                Users.class,
                R.layout.users_single_layout,
                usersviewHolder.class,
                mUserDatabase
        ) {
            @Override
            protected void populateViewHolder(usersviewHolder users1, Users users, final int position) {
                users1.setDisplayName(list.get(position));
                users1.setImage(position,getApplicationContext());
                users1.mView.setOnClickListener(new View.OnClickListener() {
                    String user_id =list.get(position);
                    @Override
                    public void onClick(View view) {
                        if(user_id.equals("workshop")){
                            Intent intent = new Intent(getApplicationContext(),Adminworkshoplist.class);
                            startActivity(intent);
                        }
                        else if(user_id.equals("program")){
                            Intent intent = new Intent(getApplicationContext(),Adminprogramlist.class);
                            startActivity(intent);
                        }
                        else if(user_id.equals("student")){
                            Intent intent = new Intent(getApplicationContext(),Adminstudentlist.class);
                            startActivity(intent);
                        }
                        else if(user_id.equals("shop")){
                            Intent intent = new Intent(getApplicationContext(),Adminshoplist.class);
                            startActivity(intent);
                        }
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

        public void setDisplayName(String users) {
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(users);
        }
        public void setImage(int pos, Context ctx){
            ImageView myImageView = mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(myImageList[pos]).placeholder(R.drawable.ic_account_circle_black_24dp).into(myImageView);
        }
    }



}

