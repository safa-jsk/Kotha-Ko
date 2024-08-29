package com.triad.kothako;

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

public class group_adapter extends RecyclerView.Adapter<group_adapter.viewholder>{
    Context mainActivity;
    ArrayList<groups> group_ArrayList;

    public group_adapter(Context mainActivity, ArrayList<groups> group_ArrayList){
        this.mainActivity = mainActivity;
        this.group_ArrayList = group_ArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull group_adapter.viewholder holder, int position) {
        groups group = group_ArrayList.get(position);
        holder.user_name.setText(group.group_name != null ? group.group_name : "Unknown");
        holder.user_status.setText(R.string.online);
        Picasso.get()
                .load(group.pfp)
                .placeholder(R.drawable.group)
                .error(R.drawable.group)
                .into(holder.image_Pfp);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mainActivity, chat_box.class);
            intent.putExtra("name",group.getGroup_name());
            intent.putExtra("receiverImg",group.getPfp());
            intent.putExtra("uID",group.getGroup_id());
            mainActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return group_ArrayList.size();
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
