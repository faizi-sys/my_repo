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

import com.example.iiui_project.MapPackage.ViewDeriveLocation;
import com.example.iiui_project.Model.NotificationFromUser;
import com.example.iiui_project.Model.NotificationFromUser;
import com.example.iiui_project.Model.NotificationFromUser;
import com.example.iiui_project.R;
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

public class MyDataAdapterForNotification extends RecyclerView.Adapter<MyDataAdapterForNotification.ViewHolder> implements Filterable {

    Activity context ;
    List<NotificationFromUser> list ;
    List<NotificationFromUser> mlistFull ;
    DatabaseReference Driver ;

    public MyDataAdapterForNotification(Activity context, List<NotificationFromUser> list) {
        this.context = context;
        this.list = list ;
        mlistFull = new ArrayList<>( ) ;
        mlistFull.addAll(list);
        Driver = FirebaseDatabase.getInstance().getReference("Driver");
    }







    @NonNull
    @Override
    public MyDataAdapterForNotification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_derive_for_request_by_userr , parent , false) ;
        return  new ViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyDataAdapterForNotification.ViewHolder holder, int position) {
        final NotificationFromUser notificationFromUser = list.get(position);
        holder.Title.setText(notificationFromUser.getAddress());
        holder.Time.setText(notificationFromUser.getTime());
        holder.Address.setText(notificationFromUser.getAddress());
        holder. Date.setVisibility(View.INVISIBLE);
        Picasso.with(context).load(notificationFromUser.getDrivePic()).fit().centerCrop().into(holder.img);




        holder.mapPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , ViewDeriveLocation.class);
                i.putExtra("lat" , notificationFromUser.getDriverLat());
                i.putExtra("long" ,notificationFromUser.getDriverLong());
                context.startActivity(i);


            }
        });


        holder.Response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(context)
                        .setTitle("Select Drive")
                        .setMessage("Do You Want To accept Request of Deriver?")

                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser() ;

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                                String strDate = "Current Time : " + mdformat.format(calendar.getTime());

                                DatabaseReference databaseReference
                                        = FirebaseDatabase.getInstance().getReference("NotificationAccept");

                                String keyF = databaseReference.push().getKey();
                                NotificationFromUser NotificationFromUser
                                        = new   NotificationFromUser(user.getUid(), notificationFromUser.getDriverId(),notificationFromUser.getDrivePic(),  notificationFromUser.getDriverLat(),  notificationFromUser.getDriverLong(), strDate , notificationFromUser.getAddress() , keyF  , notificationFromUser.getUserId() , notificationFromUser.getDriveTitle());
                                databaseReference.child(keyF).setValue(NotificationFromUser);
                                Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                            }

                        }).create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public String GetNodeAtPosition(int adapterPosition) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        NotificationFromUser driverConstructor = list.get(adapterPosition);
        String key = driverConstructor.getPushid();
        DatabaseReference   Driver = FirebaseDatabase.getInstance().getReference("Notification").child(user.getUid());
        Driver.child(key).removeValue();
        return key ;
    }


    public  class  ViewHolder extends  RecyclerView.ViewHolder{



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView Title = itemView.findViewById(R.id.viewusername);
        TextView Time = itemView.findViewById(R.id.viewmobile);
        TextView Address = itemView.findViewById(R.id.viewcnic);
        TextView  Date = itemView.findViewById(R.id.viewaddress);
        ImageView img = itemView.findViewById(R.id.viewimg);
        ImageView mapPic = itemView.findViewById(R.id.mapid);
        ImageView Response = itemView.findViewById(R.id.response);
    }


    @Override
    public Filter getFilter() {
        return Dataresult;
    }

    private  Filter Dataresult = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<NotificationFromUser> FilterList = new ArrayList<>() ;

            if(constraint == null && constraint.length()==0){
                FilterList.addAll(mlistFull);
            }else{
                String Characters = constraint.toString().toLowerCase().trim() ;
                for (NotificationFromUser NotificationFromUser : mlistFull){
                    if(NotificationFromUser.getAddress().toLowerCase().contains(Characters)){
                        FilterList.add(NotificationFromUser);
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
             list.addAll((Collection<? extends NotificationFromUser>) results.values);
             notifyDataSetChanged();
        }
    };

}
