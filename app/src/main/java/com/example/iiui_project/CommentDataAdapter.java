package com.example.iiui_project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentDataAdapter extends RecyclerView.Adapter<CommentDataAdapter.ViewHolder> {
    private Context mContext ;
    private List<Comment_Model_class> Comment_Model_classList;

    String Pic ;
    public CommentDataAdapter(Activity mContext, List<Comment_Model_class> Comment_Model_classList) {
        this.mContext = mContext;
        this.Comment_Model_classList = Comment_Model_classList;

    }



    @NonNull
    @Override
    public CommentDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.coment_layout , null);
        return new CommentDataAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentDataAdapter.ViewHolder holder, int position) {
            Comment_Model_class Comment_Model_class = Comment_Model_classList.get(position);


        Picasso.with(mContext).load(Comment_Model_class.getPersonPic()).into(holder.circularImageView);
        holder.Description.setText(Comment_Model_class.getComment_description());
        holder.titlewhosent.setText(Comment_Model_class.getPersonNameWhoComment());


    }

    @Override
    public int getItemCount() {
        return Comment_Model_classList.size();
    }

    public void setPic(String personPic) {
    personPic = Pic ;
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);



        }
        CircularImageView circularImageView = itemView.findViewById(R.id.profilepiccomment) ;
        TextView Description = itemView.findViewById(R.id.cmddesc) ;
        TextView titlewhosent = itemView.findViewById(R.id.biddescription) ;

    }
}
