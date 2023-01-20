package com.example.smartparkingone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartparkingone.Models.HeaderModel;
import com.example.smartparkingone.R;

import java.util.ArrayList;
import java.util.List;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.myViewHolder> {
    ArrayList list;
    Context context;

    public HeaderAdapter(ArrayList list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_layout, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        HeaderModel model = (HeaderModel) list.get(position);
        holder.imageView.setImageResource(model.getUserPhoto());
        holder.username.setText(model.getUsername());
        holder.vehicleNumber.setText(model.getVehicleNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView username, vehicleNumber;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picture);
            username = itemView.findViewById(R.id.username);
            vehicleNumber = itemView.findViewById(R.id.vehNumber);
        }
    }
}
