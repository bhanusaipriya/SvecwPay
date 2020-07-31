package com.svecw.a0563;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Studentlist extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView muserslist;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase,m;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlist);
        mToolbar = (Toolbar) findViewById(R.id.workshop_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Students");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("student");
        muserslist = findViewById(R.id.studentlist);
        muserslist.setHasFixedSize(true);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        muserslist.addItemDecoration(new DividerItemDecoration(muserslist.getContext(), DividerItemDecoration.VERTICAL));
        muserslist.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected  void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users, usersviewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, usersviewHolder>(
                Users.class,
                R.layout.users_single_layout,
                usersviewHolder.class,
                mUserDatabase
        ) {
            @Override
            protected void populateViewHolder(final usersviewHolder users1, final Users users,final int position) {
                m = mUserDatabase.child(mCurrentUser.getUid());
                m.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("regdno").getValue().toString().contentEquals(users.getRegdno())){
                               users1.setDisplayName("You");
                               users1.setImage(users.getThumb_image(),getApplicationContext());
                        }
                        else{
                            users1.setDisplayName(users.getRegdno());
                            users1.setImage(users.getThumb_image(),getApplicationContext());
                            users1.mView.setOnClickListener(new View.OnClickListener() {
                                String user_id = getRef(position).getKey();
                                @Override
                                public void onClick(View view){
                                    Intent pIntent = new Intent( Studentlist.this,Amount.class);
                                    pIntent.putExtra("usercategory","student");
                                    pIntent.putExtra("userid",user_id);
                                    startActivity(pIntent);
                                }
                            });
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
        public void setImage(String thumb_image, Context ctx){
            CircleImageView myImageView = (CircleImageView)mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.ic_account_circle_black_24dp).into(myImageView);
        }
    }
}
