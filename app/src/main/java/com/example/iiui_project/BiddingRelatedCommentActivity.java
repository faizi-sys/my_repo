package com.example.iiui_project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiui_project.Model.AppUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BiddingRelatedCommentActivity extends AppCompatActivity {



    FirebaseAuth mAuth ;
    FirebaseUser user ;
    ImageView CommentPic ;
    List<Comment_Model_class> Comment_Model_classList ;
    String PersonKey , PersonPic , PersonName  ;


    RecyclerView recyclerView ;
    DatabaseReference CommentofPostDatabase ;
    EditText CommentText ;
    ImageView CommentSentbtn ;
    ImageView BackArrow ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding_related_comment);
        CommentSentbtn = findViewById(R.id.commentsentbtn) ;
        CommentText = findViewById(R.id.commentedittext) ;





        recyclerView = findViewById(R.id.comentrecylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Comment_Model_classList = new ArrayList<>();
        try {
            CommentofPostDatabase = FirebaseDatabase.getInstance().getReference("comment").child(getIntent().getStringExtra("Pkey"));

            PersonKey = getIntent().getStringExtra("personkey");
            PersonPic = getIntent().getStringExtra("personpic");
            PersonName = getIntent().getStringExtra("personname");

            Picasso.with(getApplicationContext()).load(PersonPic).into(CommentPic);

        }catch (Exception e){
            Toast.makeText(this, "not assigin due to net", Toast.LENGTH_SHORT).show();
        }
        CommentSentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CommentText.getText().toString().trim().isEmpty()){
                    if(!PersonKey.isEmpty()&&!PersonPic.isEmpty() &&!PersonName.isEmpty())
                    {

                        String key = CommentofPostDatabase.push().getKey().toString();
                        Comment_Model_class Comment_Model_class = new
                                Comment_Model_class(key , getIntent().getStringExtra("Pkey") , PersonKey,
                                PersonName ,  PersonPic ,  CommentText.getText().toString().trim() ,
                                getCurrentDate() );
                        CommentofPostDatabase.child(key).setValue(Comment_Model_class);
                       CommentText.setText("");
                    }
                    else{
                        CommentText.setError("Please Write Something");
                    }
                }
            }
        });
        CommentofPostDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Comment_Model_classList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Comment_Model_class Comment_Model_class = dataSnapshot1.getValue(Comment_Model_class.class);
                    Comment_Model_classList.add(Comment_Model_class);
                }
                CommentDataAdapter commentDataAdapter = new CommentDataAdapter(BiddingRelatedCommentActivity.this,Comment_Model_classList);
                commentDataAdapter.setPic(PersonPic) ;
                recyclerView.setAdapter(commentDataAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });









    }

    public  String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
     mAuth = FirebaseAuth.getInstance();
     user = mAuth.getCurrentUser();
     DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
     databaseReference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
             {
                 AppUser appUser = dataSnapshot1.getValue(AppUser.class);
                 if(appUser.getPushid().equals(user.getUid()))
                 {

                     PersonKey = appUser.getPushid();
                     PersonPic = appUser.getPicUri();
                     PersonName = appUser.getName();

                 }
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });


    }
}
