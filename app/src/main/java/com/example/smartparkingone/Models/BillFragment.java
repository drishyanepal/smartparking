package com.example.smartparkingone.Models;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.smartparkingone.EsewaActivity;
import com.example.smartparkingone.databinding.FragmentBillBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class BillFragment extends Fragment {
    FragmentBillBinding binding;
    FirebaseDatabase database;
    String cost = "0";

    public BillFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillBinding.inflate(inflater, container, false);
        getActivity().setTitle("My Bill");
        database  = FirebaseDatabase.getInstance();

        SharedPreferences getPreferencesVehicle = getActivity().getSharedPreferences("vehicle_number", MODE_PRIVATE);
        String vehicleNumber = getPreferencesVehicle.getString("vehicle_number", "BAA 7283");

        DatabaseReference reference = database.getReference().child("BookDetails").child("TimeDetails").child(vehicleNumber).child("entryTime");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String entry_time = snapshot.getValue(String.class);
                binding.entryTime.setText(entry_time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Date currentTime = Calendar.getInstance().getTime();
        String formattedTime = DateFormat.getTimeInstance().format(currentTime);
        binding.exitTime.setText(formattedTime);



        binding.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EsewaActivity.class);
                intent.putExtra("cost",cost);
                intent.putExtra("source","bill_fragment");
                startActivity(intent);
            }
        });



        return binding.getRoot();
    }
}