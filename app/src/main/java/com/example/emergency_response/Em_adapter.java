package com.example.emergency_response;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Em_adapter extends RecyclerView.Adapter<Em_adapter.Em_holder> {
    Context context;
    ArrayList<Em_model> Em_arraylist;

    public Em_adapter(Context context, ArrayList<Em_model> em_arraylist) {
        this.context = context;
        this.Em_arraylist = em_arraylist;
    }

    @NonNull
    @Override
    public Em_adapter.Em_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent, false);
        return new Em_holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Em_adapter.Em_holder holder, int position) {

        Em_model em = Em_arraylist.get(position);

        holder.title.setText(em.title);
        holder.description.setText(em.description);
    }

    @Override
    public int getItemCount() {
        return Em_arraylist.size();
    }
    public static class Em_holder extends  RecyclerView.ViewHolder{
        TextView title, description;
        public Em_holder(@NonNull View item) {
            super(item);
            title = item.findViewById(R.id.title);
            description = item.findViewById(R.id.description);
        }
    }
}
