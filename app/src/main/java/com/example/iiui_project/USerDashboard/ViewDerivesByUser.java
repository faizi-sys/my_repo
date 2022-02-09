package com.example.iiui_project.USerDashboard;

import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiui_project.DriverAdapter.MyDataAdapterForDerive;
import com.example.iiui_project.Model.AddDeriveModelClass;
import com.example.iiui_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ViewDerivesByUser extends AppCompatActivity {
    FirebaseAuth mAuth ;
    FirebaseUser user ;
    List<AddDeriveModelClass> StoreDriverList ;

    RecyclerView recyclerView ;
    DatabaseReference UserDataBaseReferenceDrive ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_derives);

        StoreDriverList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        (recyclerView = findViewById(R.id.recyler)).setLayoutManager(new LinearLayoutManager(this));
        UserDataBaseReferenceDrive = FirebaseDatabase.getInstance().getReference("UserDerive").child(getIntent().getStringExtra("id")) ;




        UserDataBaseReferenceDrive.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                StoreDriverList.clear();
                for( DataSnapshot dataSnapshot1 : dataSnapshot.getChildren() ){

                    AddDeriveModelClass driverConstructor = dataSnapshot1.getValue(AddDeriveModelClass.class) ;
                    StoreDriverList.add(driverConstructor);



                }

         MyDataAdapterForDerive Adapter = new MyDataAdapterForDerive(ViewDerivesByUser.this,StoreDriverList);
                recyclerView.setAdapter(Adapter);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }
                    @Override
                    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                        new RecyclerViewSwipeDecorator.Builder(ViewDerivesByUser.this , c ,recyclerView , viewHolder ,dX, dY , actionState , isCurrentlyActive )
                                .addSwipeRightBackgroundColor(ContextCompat.getColor(ViewDerivesByUser.this , R.color.colorAccent))
                                .addSwipeRightActionIcon(R.drawable.ic_delete_black_24dp)
                                .addBackgroundColor(ContextCompat.getColor(ViewDerivesByUser.this , R.color.colorAccent)).addSwipeLeftBackgroundColor((ContextCompat.getColor(ViewDerivesByUser.this , R.color.green))).addSwipeLeftActionIcon(R.drawable.ic_call_black)
                                .addSwipeRightActionIcon(R.drawable.ic_delete_black_24dp).create().decorate();
                        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        Toast.makeText(getApplicationContext() , direction + "direction" , Toast.LENGTH_LONG).show();



                        if(direction==8){
                            Adapter.GetNodeAtPosition(viewHolder.getAdapterPosition());

                        }


                    }
                }
                ) ;


                itemTouchHelper.attachToRecyclerView(recyclerView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
