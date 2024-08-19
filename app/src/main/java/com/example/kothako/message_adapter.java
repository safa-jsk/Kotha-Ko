package com.example.kothako;

import static com.example.kothako.chat_box.receiverImg;
import static com.example.kothako.chat_box.senderImg;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class message_adapter extends RecyclerView.Adapter {
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
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout , parent, false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return  new reveiverViewHolder(view);
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
            reveiverViewHolder viewHolder = (reveiverViewHolder) holder;
            viewHolder.message_text.setText(messages.getMessage());
            Picasso.get().load(receiverImg).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        message_base messages = messagesAdapterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())){
            return Item_Sent;
        }
        else {
            return Item_Receive;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView message_text;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.picc);
            message_text = itemView.findViewById(R.id.sender_text);
        }
    }


    class reveiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView message_text;
        public reveiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pic);
            message_text = itemView.findViewById(R.id.reciver_text);
        }
    }


}
