package com.example.iiui_project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiui_project.Model.NotificationFromUser;
import com.example.iiui_project.USerDashboard.MyDataAdapterForResponseFromDriverAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class See_Notification_Accept_User extends AppCompatActivity {

    FirebaseAuth mAuth ;
    FirebaseUser user  ;
            List<NotificationFromUser> notificationFromUserList ;
    RecyclerView recyclerView ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_notificattion);
        (recyclerView  = findViewById(R.id.see_recy)).setLayoutManager(new LinearLayoutManager(this));

        notificationFromUserList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        DatabaseReference databaseReference
                = FirebaseDatabase.getInstance().getReference("NotificationAccept")
                ;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notificationFromUserList.clear();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
                 {
                     NotificationFromUser notificationFromUser = dataSnapshot1.getValue(NotificationFromUser.class);
                     if(notificationFromUser.getUserId().equals(user.getUid()) ||
                             notificationFromUser.getReckey().equals(user.getUid())
                     ){
                    notificationFromUserList.add(notificationFromUser);
                     }

                }
                MyDataAdapterForResponseFromDriverAdapter myDataAdapterForNotification =
                        new MyDataAdapterForResponseFromDriverAdapter(See_Notification_Accept_User.this , notificationFromUserList);
                recyclerView.setAdapter(myDataAdapterForNotification);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
