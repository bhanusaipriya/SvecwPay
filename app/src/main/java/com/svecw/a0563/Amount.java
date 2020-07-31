package com.svecw.a0563;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class Amount extends AppCompatActivity {
    private TextInputLayout amount, id;
    private Button Send;
    private DatabaseReference mUsersDatabase, mdata, student, other, mdata1,mUsersDatabase1;
    private ProgressDialog mProgressDialog;
    private FirebaseUser mCurrentUser;
    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
    long stucount,othercount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);
        amount = findViewById(R.id.user_amount);
        mProgressDialog = new ProgressDialog(this);
        final String user_id = getIntent().getStringExtra("userid");
        final String category = getIntent().getStringExtra("usercategory");
        //Toast.makeText(getApplicationContext(),user_id+" "+category,Toast.LENGTH_LONG).show();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String curid = mCurrentUser.getUid();
        student = FirebaseDatabase.getInstance().getReference().child("Users").child("student").child(curid);
        other = FirebaseDatabase.getInstance().getReference().child("Users").child(category).child(user_id);
        mdata1 = FirebaseDatabase.getInstance().getReference().child("Transactions").child(category).child(user_id).child("student").child(curid);
        mdata1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 othercount = dataSnapshot.getChildrenCount();
                othercount = othercount+1;
                mdata = FirebaseDatabase.getInstance().getReference().child("Transactions").child(category).child(user_id).child("student").child(curid).child(String.valueOf(othercount)+"_key");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        mUsersDatabase1 = FirebaseDatabase.getInstance().getReference().child("Transactions").child("student").child(curid).child(category).child(user_id);
        mUsersDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stucount = dataSnapshot.getChildrenCount();
                stucount = stucount+1;
                String p = String.valueOf(stucount);
                mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Transactions").child("student").child(curid).child(category).child(user_id).child(p+"_key");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Send = findViewById(R.id.send);
        //Toast.makeText(getApplicationContext(), user_id, Toast.LENGTH_LONG).show();
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String amount1 = amount.getEditText().getText().toString();
                student.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String useramt = dataSnapshot.child("balance").getValue().toString();
                        String debt = dataSnapshot.child("debt").getValue().toString();
                        int debt1=Integer.parseInt(debt);
                        int useramt1 = Integer.parseInt(useramt);
                        final int amt1 = Integer.parseInt(amount1);
                        final int debit = debt1 + amt1;
                        final int total = useramt1 - amt1;
                        final String total1 = String.valueOf(total);
                        if (total >= 0) {
                           // Toast.makeText(getApplicationContext(), total + "", Toast.LENGTH_LONG).show();
                            student.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataSnapshot.child("balance").getRef().setValue(total1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    other.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String val = dataSnapshot.child("balance").getValue().toString();
                                                            int val1 = Integer.parseInt(val);
                                                            int sum = val1+amt1;
                                                            final String Sum = String.valueOf(sum);
                                                            other.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    dataSnapshot.child("balance").getRef().setValue(Sum).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            mUsersDatabase.addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    HashMap<String, String> userMap = new HashMap<>();
                                                                                    userMap.put("amount", amount1);
                                                                                    userMap.put("type", "sent");
                                                                                    userMap.put("date",currentDate);
                                                                                    mUsersDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Intent intent = new Intent(getApplicationContext(),Home.class);
                                                                                                startActivity(intent);
                                                                                                mdata.addValueEventListener(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        HashMap<String, String> userMap = new HashMap<>();
                                                                                                        userMap.put("amount", amount1);
                                                                                                        userMap.put("type", "received");
                                                                                                        userMap.put("date",currentDate);
                                                                                                        mdata.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if(task.isSuccessful()){
                                                                                                                    Toast.makeText(getApplicationContext(),"YOUR TRANSACTION COMPLETED",Toast.LENGTH_LONG).show();
                                                                                                                    Intent intent = new Intent(getApplicationContext(),AllPayment.class);
                                                                                                                    startActivity(intent);
                                                                                                                }

                                                                                                            }
                                                                                                        });
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else if((debit) <= 1000){
                            student.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.child("debt").getRef().setValue(String.valueOf(debit)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            other.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String val = dataSnapshot.child("balance").getValue().toString();
                                                    int val1 = Integer.parseInt(val);
                                                    int sum = val1+amt1;
                                                    final String Sum = String.valueOf(sum);
                                                    other.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            dataSnapshot.child("balance").getRef().setValue(Sum).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    mUsersDatabase.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            HashMap<String, String> userMap = new HashMap<>();
                                                                            userMap.put("amount", amount1);
                                                                            userMap.put("type", "sent");
                                                                            userMap.put("date",currentDate);

                                                                            mUsersDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        Intent intent = new Intent(getApplicationContext(),Home.class);
                                                                                        startActivity(intent);
                                                                                        mdata.addValueEventListener(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                HashMap<String, String> userMap = new HashMap<>();
                                                                                                userMap.put("amount", amount1);
                                                                                                userMap.put("type", "received");
                                                                                                userMap.put("date",currentDate);

                                                                                                mdata.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if(task.isSuccessful()){
                                                                                                            Toast.makeText(getApplicationContext(),"You have successfully send the money...",Toast.LENGTH_LONG).show();
                                                                                                            Intent intent = new Intent(getApplicationContext(),AllPayment.class);
                                                                                                            startActivity(intent);
                                                                                                        }

                                                                                                    }
                                                                                                });
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else{
                            Toast.makeText(getApplicationContext()," Sry! you are not able to send the money",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
