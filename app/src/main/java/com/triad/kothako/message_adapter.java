package com.triad.kothako;

import static com.triad.kothako.chat_box.receiverImg;
import static com.triad.kothako.chat_box.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class message_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<message_base> messagesAdapterArrayList;
    int Item_Sent = 1;
    int Item_Receive = 2;

    public message_adapter(Context context, ArrayList<message_base> messagesAdapterArrayList) {
        this.context = context;
        this.messagesAdapterArrayList = messagesAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Item_Sent){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        message_base messages = messagesAdapterArrayList.get(position);
        if (holder.getClass() == senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.message_text.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }
        else {
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.message_text.setText(messages.getMessage());
            Picasso.get().load(receiverImg).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        message_base messages = messagesAdapterArrayList.get(position);
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(messages.getSender_id())){
            return Item_Sent;
        }
        return Item_Receive;
    }

    public static class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView message_text;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.sender_pic);
            message_text = itemView.findViewById(R.id.sender_text);
        }
    }

    public static class receiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView message_text;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.receiver_pic);
            message_text = itemView.findViewById(R.id.receiver_text);
        }
    }
}
