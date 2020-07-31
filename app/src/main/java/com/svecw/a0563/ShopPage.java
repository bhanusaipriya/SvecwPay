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

public class ShopPage extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    FirebaseAuth mAuth;
    private ImageButton pay,history,wall;
    TextView name;
    private Toolbar mtbar;
    static int[] myImageList;
    private CircleImageView mDisplayImage;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);
        mAuth = FirebaseAuth.getInstance();
        mtbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(mtbar);
        getSupportActionBar().setTitle("shop Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        wall = findViewById(R.id.wall);
        name = findViewById(R.id.name);
        myImageList = new int[]{R.drawable.juice,R.drawable.store};
        wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Userwallet.class);
                intent.putExtra("category",getIntent().getStringExtra("category"));
                startActivity(intent);
            }
        });
        history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Userhistory.class);
                intent.putExtra("category",getIntent().getStringExtra("category"));
                startActivity(intent);
            }
        });
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("shop").child(current_uid);
        mDisplayImage = (CircleImageView) findViewById(R.id.studentprofile);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name1 = dataSnapshot.child("name").getValue().toString();
                name.setText(name1);
                if(name1.equals("templesquare"))
                    Picasso.with(ShopPage.this).load(myImageList[1]).placeholder(R.drawable.ic_account_circle_black_24dp).into(mDisplayImage);
                else{
                    Picasso.with(ShopPage.this).load(myImageList[0]).placeholder(R.drawable.ic_account_circle_black_24dp).into(mDisplayImage);
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
        getMenuInflater().inflate(R.menu.adminmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        return true;
    }
}
