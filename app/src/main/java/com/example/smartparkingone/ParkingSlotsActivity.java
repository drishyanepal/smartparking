package com.example.smartparkingone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartparkingone.databinding.ActivityParkingSlotsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ParkingSlotsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityParkingSlotsBinding binding;
    String startHour, startMin, startSec, endHour, endMin, endSec, timeOfDayStart, timeOfDayEnd;
    String slotId;
    int parkDuration = 1;
    FirebaseAuth auth;
    FirebaseDatabase database;
    int[] clickedStatus = new int[1];
    boolean sevenBooked = false, twentySevenBooked = false;
    RelativeLayout Seven, TwentySeven, ThirtyFour, Forty, FortyThree, FortyFour, FiftyThree, FiftyFive, FiftySeven, FiftyNine, SixtySix, SixtySeven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParkingSlotsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        setTitle("Parking Space");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding.popUpLayout.setVisibility(View.GONE);

        clickedStatus[0] = 50;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        findAllViewsById();

        loadSlotData();
        //-----------on clicks-----------------------------------------------------------------
//        slotsClickHandle();


    }

    private void findAllViewsById() {
        Seven = findViewById(R.id.Seven);
        Seven.setOnClickListener(this);
        TwentySeven = findViewById(R.id.TwentySeven);
        TwentySeven.setOnClickListener(this);
        ThirtyFour = findViewById(R.id.ThirtyFour);
        ThirtyFour.setOnClickListener(this);
        Forty = findViewById(R.id.Forty);
        Forty.setOnClickListener(this);
        FortyThree = findViewById(R.id.FortyThree);
        FortyThree.setOnClickListener(this);
        FortyFour = findViewById(R.id.FortyFour);
        FortyFour.setOnClickListener(this);
        FiftyThree = findViewById(R.id.FiftyThree);
        FiftyThree.setOnClickListener(this);
        FiftyFive = findViewById(R.id.FiftyFive);
        FiftyFive.setOnClickListener(this);
        FiftySeven = findViewById(R.id.FiftySeven);
        FiftySeven.setOnClickListener(this);
        FiftyNine = findViewById(R.id.FiftyNine);
        FiftyNine.setOnClickListener(this);
        SixtySix = findViewById(R.id.SixtySix);
        SixtySix.setOnClickListener(this);
        SixtySeven = findViewById(R.id.SixtySeven);
        SixtySeven.setOnClickListener(this);
    }


    private void loadSlotData() {
        DatabaseReference reference = database.getReference().child("SlotBooked");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String idString = dataSnapshot.getKey();
                    checkSlotNumberForPreBooking(idString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference reference20 = database.getReference().child("Slots").child("slotTwenty");
        reference20.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int slotTwentyStatus = snapshot.getValue(Integer.class);
                if (slotTwentyStatus == 0) {
                    binding.Twenty.setBackgroundColor(getResources().getColor(R.color.green));
                }
                if (slotTwentyStatus == 1) {
                    binding.Twenty.setBackgroundColor(getResources().getColor(R.color.red));
                }
                if (slotTwentyStatus == 2) {
                    binding.Twenty.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference23 = database.getReference().child("Slots").child("slotTwentyThree");
        reference23.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int slotTwentyThreeStatus = snapshot.getValue(Integer.class);
                if (slotTwentyThreeStatus == 0) {
                    binding.TwentyThree.setBackgroundColor(getResources().getColor(R.color.green));
                }
                if (slotTwentyThreeStatus == 1) {
                    binding.TwentyThree.setBackgroundColor(getResources().getColor(R.color.red));
                }
                if (slotTwentyThreeStatus == 2) {
                    binding.TwentyThree.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference52 = database.getReference().child("Slots").child("slotFiftyTwo");
        reference52.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int slotFiftyTwo = snapshot.getValue(Integer.class);
                if (slotFiftyTwo == 0) {
                    binding.FiftyTwo.setBackgroundColor(getResources().getColor(R.color.green));
                }
                if (slotFiftyTwo == 1) {
                    binding.FiftyTwo.setBackgroundColor(getResources().getColor(R.color.red));
                }
                if (slotFiftyTwo == 2) {
                    binding.FiftyTwo.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void checkSlotNumberForPreBooking(String slotNumber) {
        if (slotNumber.equals("slotSeven")) {
            binding.Seven.setBackgroundColor(getResources().getColor(R.color.yellow));
            sevenBooked = true;
        }
        if (slotNumber.equals("slotTwentySeven")) {
            binding.TwentySeven.setBackgroundColor(getResources().getColor(R.color.yellow));
            twentySevenBooked = true;
        }
        if (slotNumber.equals("slotThirtyFour")) {
            binding.ThirtyFour.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        if (slotNumber.equals("slotForty")) {
            binding.Forty.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        if (slotNumber.equals("slotFortyThree")) {
            binding.FortyThree.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        if (slotNumber.equals("slotFortyFour")) {
            binding.FortyFour.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        if (slotNumber.equals("slotFiftyThree")) {
            binding.FiftyThree.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        if (slotNumber.equals("slotFiftyFive")) {
            binding.FiftyFive.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        if (slotNumber.equals("slotFiftySeven")) {
            binding.FiftySeven.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        if (slotNumber.equals("slotFiftyNine")) {
            binding.FiftyNine.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        if (slotNumber.equals("slotSixtySix")) {
            binding.SixtySix.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        if (slotNumber.equals("slotSixtySeven")) {
            binding.SixtySeven.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
    }


    private void hidePopUP() {
        binding.popUpLayout.setVisibility(View.GONE);
        binding.hiddenLayer.setVisibility(View.GONE);
    }

    private void showPopUpWith() {
        binding.popUpLayout.setVisibility(View.VISIBLE);
        binding.hiddenLayer.setVisibility(View.VISIBLE);
        binding.parkDuration.setText("1");
        parkDuration = 1;

        SharedPreferences getPreferencesPhone = getSharedPreferences("phone_number", MODE_PRIVATE);
        String phoneNumber = getPreferencesPhone.getString("phone_number", "9843569028");
        binding.phoneNumberPopUp.setText(phoneNumber);

        SharedPreferences getPreferencesVehicle = getSharedPreferences("vehicle_number", MODE_PRIVATE);
        String vehicleNumber = getPreferencesVehicle.getString("vehicle_number", "BAA 7283");
        binding.vehicleNumberPopUp.setText(vehicleNumber);

        Date currentTime = Calendar.getInstance().getTime();
        String formattedTime = DateFormat.getTimeInstance().format(currentTime);
        String[] splitTime = formattedTime.split(":");
        startHour = splitTime[0];
        startMin = splitTime[1];
        String[] splitSecond = splitTime[2].split(" ");
        startSec = splitSecond[0];
        timeOfDayStart = splitSecond[1].trim();
        binding.timeFrom.setText(formattedTime);

        endHour = String.valueOf(Integer.parseInt(startHour) + 1);
        endMin = startMin;
        endSec = startSec;
        timeOfDayEnd = timeOfDayStart;

        binding.timeTo.setText(returnEndTime());
    }

    private String returnEndTime() {
        String endTime = endHour + ":" + endMin + ":" + endSec + " " + timeOfDayEnd;
        return endTime;
    }

    private void slotsClickHandle() {
        binding.Seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slotId = getResources().getResourceEntryName(v.getId());
                getSlotClickedStatus(slotId);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (clickedStatus[0] == 1) {
                            Toast.makeText(ParkingSlotsActivity.this, "Please wait a moment for this slot ", Toast.LENGTH_SHORT).show();
                            clickedStatus[0] = 50;
                            return;
                        }
                        if (clickedStatus[0] == 0) {
                            setSlotClickedStatus();
                            clickedStatus[0] = 50;
                            if (!sevenBooked) {
                                showPopUpWith();
                                binding.slotNumber.setText("7");
                            }
                        }
                    }
                }, 2000);
            }
        });


    }

    private void setSlotClickedStatus() {
        DatabaseReference reference = database.getReference().child("SlotClicked").child(slotId);
        reference.setValue(1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reference.setValue(0);
            }
        }, 1000 * 60);
    }

    private void getSlotClickedStatus(String slotId) {
        try {
            DatabaseReference reference = database.getReference().child("SlotClicked").child(slotId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    clickedStatus[0] = snapshot.getValue(Integer.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Seven:
                Toast.makeText(this, "Shhh..", Toast.LENGTH_SHORT).show();
            case R.id.TwentySeven:
                showPopUpWith();
                binding.slotNumber.setText("27");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.ThirtyFour:
                showPopUpWith();
                binding.slotNumber.setText("34");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.Forty:
                showPopUpWith();
                binding.slotNumber.setText("40");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.FortyThree:
                showPopUpWith();
                binding.slotNumber.setText("43");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.FortyFour:
                showPopUpWith();
                binding.slotNumber.setText("44");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.FiftyThree:
                showPopUpWith();
                binding.slotNumber.setText("53");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.FiftyFive:
                showPopUpWith();
                binding.slotNumber.setText("55");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.FiftySeven:
                showPopUpWith();
                binding.slotNumber.setText("57");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.FiftyNine:
                showPopUpWith();
                binding.slotNumber.setText("59");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.SixtySix:
                showPopUpWith();
                binding.slotNumber.setText("66");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.SixtySeven:
                showPopUpWith();
                binding.slotNumber.setText("67");
                slotId = getResources().getResourceEntryName(v.getId());
            case R.id.plus:
                parkDuration++;
                binding.parkDuration.setText(String.valueOf(parkDuration));
                String cost = String.valueOf(parkDuration * 50);
                binding.cost.setText(cost);
                endHour = String.valueOf(Integer.parseInt(endHour) + 1);
                int hrs = Integer.parseInt(endHour);
                if (hrs == 12) {
                    if (timeOfDayEnd.equals("pm")) {
                        timeOfDayEnd = "am";
                    } else {
                        timeOfDayEnd = "pm";
                    }
                }
                if (hrs == 13) {
                    endHour = "1";
                }

                binding.timeTo.setText(returnEndTime());
            case R.id.minus:
                if (parkDuration > 1) {
                    parkDuration--;
                    binding.parkDuration.setText(String.valueOf(parkDuration));
                    String cost1 = String.valueOf(parkDuration * 50);
                    binding.cost.setText(cost1);
                    endHour = String.valueOf(Integer.parseInt(endHour) - 1);
                    int hrs1 = Integer.parseInt(endHour);
                    if (hrs1 == 0) {
                        endHour = "12";
                    }
                    if (hrs1 == 11) {
                        if (timeOfDayEnd.equals("pm")) {
                            timeOfDayEnd = "am";
                        } else {
                            timeOfDayEnd = "pm";
                        }
                    }
                    binding.timeTo.setText(returnEndTime());
                }
            case R.id.hiddenLayer:
                hidePopUP();
            case R.id.proceedButtonPopUp:
                Intent intent = new Intent(ParkingSlotsActivity.this, EsewaActivity.class);
                slotId = "slot" + slotId;
                intent.putExtra("slotId", slotId);
                intent.putExtra("cost", binding.cost.getText().toString());
                startActivity(intent);
                finish();
        }
    }
}
