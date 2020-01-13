package com.berkanaslan.instagramclonefirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.PostHolder> {

    private ArrayList<String> userEmailList;
    private ArrayList<String> userCommentList;
    private ArrayList<String> userImageList;
    private ArrayList<String> userDateList;

    public ProfileRecyclerAdapter(ArrayList<String> userEmailList, ArrayList<String> userCommentList, ArrayList<String> userImageList, ArrayList<String> userDateList) {

        this.userEmailList = userEmailList;
        this.userCommentList = userCommentList;
        this.userImageList = userImageList;
        this.userDateList = userDateList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.profile_recycler,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.profileDate.setText(userDateList.get(position));
        holder.profileComment.setText(userCommentList.get(position));
        Picasso.get().load(userImageList.get(position)).into(holder.sharedImageProfile);

    }

    @Override
    public int getItemCount() {
        return userEmailList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        ImageView sharedImageProfile;
        TextView profileComment;
        TextView profileDate;


        public PostHolder(@NonNull View itemView) {
            super(itemView);

            profileDate = itemView.findViewById(R.id.profileDate);
            sharedImageProfile = itemView.findViewById(R.id.sharedImageProfile);
            profileComment = itemView.findViewById(R.id.profileComment);
        }
    }
}
