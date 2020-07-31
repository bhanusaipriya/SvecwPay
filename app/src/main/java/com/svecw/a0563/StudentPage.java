package com.svecw.a0563;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class StudentPage extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    FirebaseAuth mAuth;
    private ImageButton pay,history,wall;
    TextView name;
    private Toolbar mtbar;
    private CircleImageView mDisplayImage;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);
        mAuth = FirebaseAuth.getInstance();
        mtbar = findViewById(R.id.student_toolbar);
        setSupportActionBar(mtbar);
        getSupportActionBar().setTitle("Student Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        wall = findViewById(R.id.wall);
        wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Wallet.class);
                startActivity(intent);
            }
        });
        history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Thistory.class);
                startActivity(intent);
            }
        });
        name = findViewById(R.id.hi);
        pay = findViewById(R.id.pe);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AllPayment.class);
                startActivity(intent);
            }
        });
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("student").child(current_uid);
        setSupportActionBar(mtbar);
        getSupportActionBar().setTitle("Student");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDisplayImage = (CircleImageView) findViewById(R.id.studentprofile);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name1 = dataSnapshot.child("name").getValue().toString();
                name.setText("hi"+" "+name1);
                final String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                if(!thumb_image.equals("default")){
                    Picasso.with(StudentPage.this).load(thumb_image).
                            networkPolicy(NetworkPolicy.OFFLINE).
                            placeholder(R.drawable.ic_account_circle_black_24dp).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(StudentPage.this).load(thumb_image).placeholder(R.drawable.ic_account_circle_black_24dp).into(mDisplayImage);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

        public void sendToStart() {
        Intent startIntent = new Intent(getApplicationContext(), Home.class);
        startActivity(startIntent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if(item.getItemId() == R.id.profile){
            Intent intent = new Intent(getApplicationContext(),Profile.class);
            startActivity(intent);
        }
        return true;
    }
}
