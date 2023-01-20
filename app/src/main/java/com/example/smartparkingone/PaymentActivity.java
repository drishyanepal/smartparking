package com.example.smartparkingone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartparkingone.databinding.ActivityPaymentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    String cost, slotId;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        slotId = getIntent().getStringExtra("slotId");
        cost = getIntent().getStringExtra("cost");

//        getBalanceFromFirebase();

        SharedPreferences getPreferencesBalance = getSharedPreferences("balance", MODE_PRIVATE);
        float getBalance = getPreferencesBalance.getFloat("balance", (float) 4840.78);
        binding.balance.setText(String.valueOf(getBalance));

        SharedPreferences getPreferencesUsername = getSharedPreferences("username", MODE_PRIVATE);
        String username = getPreferencesUsername.getString("username", "View");
        binding.usernamePayment.setText(username);

        SharedPreferences getPreferencesVehicle = getSharedPreferences("vehicle_number", MODE_PRIVATE);
        String vehicleNumber = getPreferencesVehicle.getString("vehicle_number", "BAA 7283");
        binding.vehicleNumberPayment.setText(vehicleNumber);

        binding.amountPayment.setText(cost);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, ParkingSlotsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String balance = binding.balance.getText().toString();
                float balanceInt = Float.parseFloat(balance);
                balanceInt = balanceInt - Float.parseFloat(cost);
                database.getReference().child("Users").child(auth.getUid()).child("Balance").setValue(balanceInt);

                SharedPreferences sharedPreferencesUsername = getSharedPreferences("balance", MODE_PRIVATE);
                SharedPreferences.Editor editorTwo = sharedPreferencesUsername.edit();
                editorTwo.putFloat("balance", balanceInt);
                editorTwo.apply();

                database.getReference().child("Slots").child(slotId).setValue("2");
                Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();

                database.getReference().child("SlotBooked").child(slotId).setValue(getSlotIdInNumber());

                database.getReference().child("SlotsByVehicle").child(String.valueOf(getSlotIdInNumber())).setValue(vehicleNumber);
                finish();
            }
        });
    }

    private int getSlotIdInNumber() {
        if (slotId.equals("slotSeven")) {
            return 7;
        }
        if (slotId.equals("slotTwentySeven")) {
            return 27;
        }
        if (slotId.equals("slotThirtyFour")) {
            return 34;
        }
        if (slotId.equals("slotForty")) {
            return 40;
        }
        if (slotId.equals("slotFortyThree")) {
            return 43;
        }
        if (slotId.equals("slotFortyFour")) {
            return 44;
        }
        if (slotId.equals("slotFiftyThree")) {
            return 53;
        }
        if (slotId.equals("slotFiftyFive")) {
            return 55;
        }
        if (slotId.equals("slotFiftySeven")) {
            return 57;
        }
        if (slotId.equals("slotFiftyNine")) {
            return 59;
        }
        if (slotId.equals("slotSixtySix")) {
            return 66;
        }
        if (slotId.equals("slotSixtySeven")) {
            return 67;
        }
        return 0;
    }

    private void getBalanceFromFirebase() {
        database.getReference().child("Users").child(auth.getUid()).child("Balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float getBalance = snapshot.getValue(float.class);
                binding.balance.setText(String.valueOf(getBalance));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}