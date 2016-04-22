package com.example.tomsdeath.message.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.tomsdeath.message.MainActivity;
import com.example.tomsdeath.message.R;
import com.example.tomsdeath.message.SendActivity;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tomsdeath on 2016/4/4.
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageRecyclerViewHolder> {
    List<Map<String, Object>> list;
    Context context;
    public static LayoutInflater layoutInflater;
    ItemClickListener itemClickListener;

    public MessageRecyclerViewAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(final MessageRecyclerViewHolder holder, int position) {
        holder.number.setText(list.get(position).get(MainActivity.NUMBER).toString());
        holder.msg_body.setText(list.get(position).get(MainActivity.BODY).toString());
        holder.state.setText(list.get(position).get(MainActivity.TYPE).toString());
        holder.date.setText(list.get(position).get(MainActivity.DATE).toString());
        if(itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    itemClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public MessageRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageRecyclerViewHolder(layoutInflater.inflate(R.layout.showlist, parent, false));
    }

    public interface ItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class MessageRecyclerViewHolder extends RecyclerView.ViewHolder{
       // @Bind(R.id.head) TextView head;
        @Bind(R.id.message_number) TextView number;
        @Bind(R.id.msg_body) TextView msg_body;
        @Bind(R.id.state) TextView state;
        @Bind(R.id.date) TextView date;
        public MessageRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
