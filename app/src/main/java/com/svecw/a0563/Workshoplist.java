package com.svecw.a0563;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Workshoplist extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView muserslist;
    static int[] myImageList ;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshoplist);
        mToolbar = (Toolbar) findViewById(R.id.workshop_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Workshops");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("workshop");
        muserslist = (RecyclerView) findViewById(R.id.program_list);
        myImageList = new int[]{R.drawable.aws,R.drawable.android};
        muserslist.setHasFixedSize(true);
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
            protected void populateViewHolder(usersviewHolder users1, Users users,final int position) {
                users1.setDisplayName(users.getName());
                users1.setImage(position,getApplicationContext());
                users1.mView.setOnClickListener(new View.OnClickListener() {
                    String user_id = getRef(position).getKey();
                    @Override
                    public void onClick(View view){
                        Intent pIntent = new Intent( Workshoplist.this,Amount.class);
                        pIntent.putExtra("usercategory","workshop");
                        pIntent.putExtra("userid",user_id);
                        startActivity(pIntent);
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
        public void setImage(int pos, Context ctx){
            ImageView myImageView = mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(myImageList[pos]).placeholder(R.drawable.ic_account_circle_black_24dp).into(myImageView);
        }
    }
}
