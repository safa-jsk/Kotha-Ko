package com.example.kothako;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.viewholder>{
    MainActivity mainActivity;
    ArrayList<user> userArrayList;

    public User_Adapter(MainActivity mainActivity, ArrayList<user> userArrayList) {
        this.mainActivity = mainActivity;
        this.userArrayList = userArrayList;

    }

    @NonNull
    @Override
    public User_Adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull User_Adapter.viewholder holder, int position) {

        user user = userArrayList.get(position);
        holder.user_name.setText(user.username);
        holder.user_status.setText(user.status);
        Picasso.get().load(user.pfp).into(holder.image_Pfp);



    }

    @Override
    public int getItemCount() {

        return userArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView image_Pfp;
        TextView user_name;
        TextView user_status;

        public viewholder(@NonNull View itemView) {

            super(itemView);
            image_Pfp = itemView.findViewById(R.id.image_Pfp);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);

        }
    }
}
