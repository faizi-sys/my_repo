package com.example.iiui_project.DriverAdapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiui_project.DriverDashoard.ViewDriverActivityToSeeUserDerive;
import com.example.iiui_project.MapPackage.OnlyLocationView;
import com.example.iiui_project.Model.AppUser;
import com.example.iiui_project.R;
import com.example.iiui_project.USerDashboard.ViewDerivesByUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyDataAdapterViewUSer extends RecyclerView.Adapter<MyDataAdapterViewUSer.ViewHolder> implements Filterable {

    Activity context ;
    List<AppUser> list ;
    List<AppUser> mlistFull ;
    DatabaseReference Driver ;

    public MyDataAdapterViewUSer(Activity context, List<AppUser> list) {
        this.context = context;
        this.list = list ;
        mlistFull = new ArrayList<>( ) ;
        mlistFull.addAll(list);
        Driver = FirebaseDatabase.getInstance().getReference("Driver");
    }



    public  String  GetNodeAtPosition(int adapterPosition) {

        AppUser driverConstructor = list.get(adapterPosition);
         String key = driverConstructor.getCnic();
      //  DatabaseReference Driver = FirebaseDatabase.getInstance().getReference("Driver");
        Driver.child(key).removeValue();
         return key ;
    }


    public  String GetPhoneNumber(int adapterPosition){
        AppUser driverConstructor = list.get(adapterPosition);
        return driverConstructor.getPhone();
    }


    @NonNull
    @Override
    public MyDataAdapterViewUSer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sp_list , parent , false) ;
        return  new ViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyDataAdapterViewUSer.ViewHolder holder, int position) {
        final AppUser driverConstructor = list.get(position);
        holder.Name.setText(driverConstructor.getName()); // ok
        holder.Contact.setText(driverConstructor.getPhone()); // ok
        holder.Address.setText(driverConstructor.getLocation());
        holder. Cnic.setText(driverConstructor.getCartype()); // ok
        Picasso.with(context).load(driverConstructor.getPicUri()).fit().centerCrop().into(holder.img); // ok



        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , ViewDriverActivityToSeeUserDerive.class);
                 i.putExtra("id" , driverConstructor.getPushid());
                context.startActivity(i);
            }
        });



        holder.mapPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , OnlyLocationView.class);
                i.putExtra("lat" , driverConstructor.getLattitude());
                i.putExtra("long" , driverConstructor.getLongitude());
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
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

            List<AppUser> FilterList = new ArrayList<>() ;

            if(constraint == null && constraint.length()==0){
                FilterList.addAll(mlistFull);
            }else{
                String Characters = constraint.toString().toLowerCase().trim() ;
                for (AppUser driverConstructor : mlistFull){
                    if(driverConstructor.getName().toLowerCase().contains(Characters)){
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
             list.addAll((Collection<? extends AppUser>) results.values);
             notifyDataSetChanged();

        }
    };

}
