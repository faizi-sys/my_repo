package com.example.iiui_project.USerDashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiui_project.DriverAdapter.MyDataAdapter;
import com.example.iiui_project.Model.AppUser;
import com.example.iiui_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ViewDriverActivity extends AppCompatActivity {

    DatabaseReference Driver ;
   List<AppUser> StoreDriverList ;
   ListView listView ;

   ImageView imageView ;
    MyDataAdapter Adapter ;
    RecyclerView recyclerView ;
   LinearLayoutManager layoutManager ;

   Toolbar toolbar ;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nvmenu , menu);
        MenuItem searchmenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchmenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;


    }
ImageView imageView1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_driver);

        StoreDriverList  = new ArrayList<>();

        toolbar = findViewById(R.id.toolbars);
//        setSupportActionBar(toolbar);
        Driver = FirebaseDatabase.getInstance().getReference("User");
        //listView = findViewById(R.id.list);


        recyclerView = findViewById(R.id.list) ;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);





        Driver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                StoreDriverList.clear();
                for( DataSnapshot dataSnapshot1 : dataSnapshot.getChildren() ){

                    AppUser driverConstructor = dataSnapshot1.getValue(AppUser.class) ;
                     if(driverConstructor.getUsertype().equals("Driver")){
                    StoreDriverList.add(driverConstructor);}



                }

                 Adapter = new MyDataAdapter(ViewDriverActivity.this,StoreDriverList);
                  recyclerView.setAdapter(Adapter);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }
                    @Override
                    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                        new RecyclerViewSwipeDecorator.Builder(ViewDriverActivity.this , c ,recyclerView , viewHolder ,dX, dY , actionState , isCurrentlyActive )
                                .addSwipeRightBackgroundColor(ContextCompat.getColor(ViewDriverActivity.this , R.color.colorAccent))
                                .addSwipeRightActionIcon(R.drawable.ic_delete_black_24dp)
                                .addBackgroundColor(ContextCompat.getColor(ViewDriverActivity.this , R.color.colorAccent)).addSwipeLeftBackgroundColor((ContextCompat.getColor(ViewDriverActivity.this , R.color.green))).addSwipeLeftActionIcon(R.drawable.ic_call_black)
                                .addSwipeRightActionIcon(R.drawable.ic_delete_black_24dp).create().decorate();
                        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                       Toast.makeText(getApplicationContext() , direction + "direction" , Toast.LENGTH_LONG).show();

                        if(direction==8){
                            Toast.makeText(ViewDriverActivity.this, "Call To Mobile Number" + Adapter.GetPhoneNumber(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                            CAllByPhone(Adapter.GetPhoneNumber(viewHolder.getAdapterPosition()));

                            Adapter.notifyDataSetChanged();
                        }

                        if(direction==4){
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
    @Override
    protected void onStart() {
        super.onStart();


    }


    private void CAllByPhone(String getNodeAtPosition) {

        Toast.makeText(getApplicationContext() , getNodeAtPosition , Toast.LENGTH_LONG).show();

        if(!checkPermission()){
            requestPermission();
        }

        // "tel:0377778888" fromat //
        if(!getNodeAtPosition.isEmpty()){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"  +getNodeAtPosition));
            if (ActivityCompat.checkSelfPermission(ViewDriverActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);}
        else {
        }



        recyclerView.setAdapter(Adapter);

    }




    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ViewDriverActivity.this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {


        ActivityCompat.requestPermissions(ViewDriverActivity.this,new String[]{Manifest.permission.CALL_PHONE}, 1);

    }

}
