package com.example.iiui_project.DriverAdapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiui_project.DriverDashoard.UserUpdateActivityDriver;
import com.example.iiui_project.DriverDashoard.ViewDriverActivityToSeeUserDerive;
import com.example.iiui_project.MapPackage.OnlyLocationView;
import com.example.iiui_project.MapPackage.OnlyLocationViewBoth;
import com.example.iiui_project.Model.AddDeriveModelClass;
import com.example.iiui_project.Model.NotificationFromUser;
import com.example.iiui_project.R;
import com.example.iiui_project.USerDashboard.ViewDerivesByUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class MyDataAdapterSee extends RecyclerView.Adapter<MyDataAdapterSee.ViewHolder> implements Filterable {

    FirebaseAuth mAuth ;
    FirebaseUser DriverUser ;
    Activity context ;
    List<AddDeriveModelClass> list ;
    List<AddDeriveModelClass> mlistFull ;
    DatabaseReference Driver ;

    String UserId ;
    public MyDataAdapterSee(Activity context, List<AddDeriveModelClass> list) {
        this.context = context;
        this.list = list ;
        mlistFull = new ArrayList<>( ) ;
        mlistFull.addAll(list);
        Driver = FirebaseDatabase.getInstance().getReference("Driver");
     mAuth = FirebaseAuth.getInstance();
     DriverUser  = mAuth.getCurrentUser() ;
    }





    @NonNull
    @Override
    public MyDataAdapterSee.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_derive_req_for_accept_by_deri , parent , false) ;
        return  new ViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyDataAdapterSee.ViewHolder holder, int position) {
        final AddDeriveModelClass driverConstructor = list.get(position);
        holder.Name.setText(driverConstructor.getAddress()); // ok
        holder.Contact.setText(driverConstructor.getTime()); // ok
        holder.Address.setText(driverConstructor.getDate());
        holder.Cnic.setText(driverConstructor.getTitle()); // ok
        Picasso.with(context).load(driverConstructor.getImageurl()).fit().centerCrop().into(holder.img); // ok


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(context)
                        .setTitle("Select Drive")
                        .setMessage("Do You Want To accept Request of Deriver?")

                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {


                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                                String strDate = "Current Time : " + mdformat.format(calendar.getTime());

                                DatabaseReference databaseReference
                                        = FirebaseDatabase.getInstance().getReference("NotificationAccept");

                                String key = databaseReference.push().getKey();
                                NotificationFromUser NotificationFromUser
                                        = new NotificationFromUser(UserId, DriverUser.getUid(), driverConstructor.getImageurl(), driverConstructor.getLattitude(), driverConstructor.getLongitude(), strDate, driverConstructor.getAddress(), key, DriverUser.getUid(), driverConstructor.getTitle());
                                databaseReference.child(key).setValue(NotificationFromUser);
                                Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                            }

                        }).create().show();
            }
        });






        holder.mapPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , OnlyLocationViewBoth.class);
                i.putExtra("lat" , driverConstructor.getLattitude());
                i.putExtra("lat2" , driverConstructor.getLattitude2());
                i.putExtra("long" , driverConstructor.getLongitude());
                i.putExtra("long2" , driverConstructor.getLongitude2());

                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setUserID(String id) {

        UserId = id ;
    }

    public  class  ViewHolder extends  RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView Name = itemView.findViewById(R.id.viewusername);
        TextView Contact = itemView.findViewById(R.id.viewmobile);
        TextView Address = itemView.findViewById(R.id.viewcnic);
        TextView  Cnic = itemView.findViewById(R.id.viewaddress);
        ImageView img = itemView.findViewById(R.id.viewimg);
        ImageView mapPic = itemView.findViewById(R.id.mapid);
        ImageView item = itemView.findViewById(R.id.handle);

    }


    @Override
    public Filter getFilter() {
        return Dataresult;
    }

    private  Filter Dataresult = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<AddDeriveModelClass> FilterList = new ArrayList<>() ;

            if(constraint == null && constraint.length()==0){
                FilterList.addAll(mlistFull);
            }else{
                String Characters = constraint.toString().toLowerCase().trim() ;
                for (AddDeriveModelClass driverConstructor : mlistFull){
                    if(driverConstructor.getTitle().toLowerCase().contains(Characters)){
                        FilterList.add(driverConstructor);
                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = FilterList ;
            return filterResults ;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
             list.clear();
             list.addAll((Collection<? extends AddDeriveModelClass>) results.values);
             notifyDataSetChanged();

        }
    };

}
