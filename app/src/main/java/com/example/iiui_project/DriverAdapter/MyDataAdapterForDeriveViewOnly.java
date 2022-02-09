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
import com.example.iiui_project.Model.AddDeriveModelClass;
import com.example.iiui_project.Model.NotificationFromUser;
import com.example.iiui_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyDataAdapterForDeriveViewOnly extends RecyclerView.Adapter<MyDataAdapterForDeriveViewOnly.ViewHolder> implements Filterable {

    Activity context ;
    List<AddDeriveModelClass> list ;
    List<AddDeriveModelClass> mlistFull ;
    DatabaseReference Driver ;

    public MyDataAdapterForDeriveViewOnly(Activity context, List<AddDeriveModelClass> list) {
        this.context = context;
        this.list = list ;
        mlistFull = new ArrayList<>( ) ;
        mlistFull.addAll(list);
        Driver = FirebaseDatabase.getInstance().getReference("Driver");
    }







    @NonNull
    @Override
    public MyDataAdapterForDeriveViewOnly.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_derive_for_request_by_userr , parent , false) ;
        return  new ViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyDataAdapterForDeriveViewOnly.ViewHolder holder, int position) {
        final AddDeriveModelClass AddDeriveModelClass = list.get(position);
        holder.Title.setText(AddDeriveModelClass.getTitle());
        holder.Time.setText(AddDeriveModelClass.getTime());
        holder.Address.setText(AddDeriveModelClass.getAddress());
        holder. Date.setText(AddDeriveModelClass.getDate());
        Picasso.with(context).load(AddDeriveModelClass.getImageurl()).fit().centerCrop().into(holder.img);



        holder.mapPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , ViewDeriveLocation.class);
                i.putExtra("lat" , AddDeriveModelClass.getLattitude());
                i.putExtra("long" , AddDeriveModelClass.getLongitude());
                context.startActivity(i);


            }
        });

        holder.Response.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public String GetNodeAtPosition(int adapterPosition) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        AddDeriveModelClass driverConstructor = list.get(adapterPosition);
        String key = driverConstructor.getDrivePushId();
        DatabaseReference   Driver = FirebaseDatabase.getInstance().getReference("UserDerive").child(user.getUid());
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

            List<AddDeriveModelClass> FilterList = new ArrayList<>() ;

            if(constraint == null && constraint.length()==0){
                FilterList.addAll(mlistFull);
            }else{
                String Characters = constraint.toString().toLowerCase().trim() ;
                for (AddDeriveModelClass AddDeriveModelClass : mlistFull){
                    if(AddDeriveModelClass.getTitle().toLowerCase().contains(Characters)){
                        FilterList.add(AddDeriveModelClass);
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
