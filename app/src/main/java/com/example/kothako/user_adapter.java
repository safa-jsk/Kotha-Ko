package com.example.kothako;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_adapter extends RecyclerView.Adapter<user_adapter.viewholder>{
    Context mainActivity;
    ArrayList<users> userArrayList;

    public user_adapter(Context mainActivity, ArrayList<users> userArrayList) {
        this.mainActivity = mainActivity;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public user_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull user_adapter.viewholder holder, int position) {
        users user = userArrayList.get(position);
        holder.user_name.setText(user.username != null ? user.username : "Unknown");
        holder.user_status.setText(user.status != null ? user.status : "No status available");
        Picasso.get()
                .load(user.pfp)
                .placeholder(R.drawable.man)
                .error(R.drawable.man)
                .into(holder.image_Pfp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, chat_box.class);
                intent.putExtra("name",user.getUsername());
                intent.putExtra("receiverImg",user.getUsername());
                intent.putExtra("uID",user.getUserid());
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
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
