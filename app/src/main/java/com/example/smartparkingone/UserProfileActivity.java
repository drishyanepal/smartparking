package com.example.smartparkingone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartparkingone.Models.UserModel;
import com.example.smartparkingone.databinding.ActivityUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    private DatePickerDialog datePickerDialog;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Profile Settings");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        loadProfile();
        initDatePicker();

        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = binding.firstName.getText().toString();
                String lastName = binding.lastName.getText().toString();
                String dob = binding.dob.getText().toString();
                String email = binding.email.getText().toString();
                String vehicleNumber = binding.vehicleNumber.getText().toString();
                String phoneNumber = binding.phoneNumberProfile.getText().toString();

                if (firstName.isEmpty() || vehicleNumber.isEmpty()) {
                    Toast.makeText(UserProfileActivity.this, "Invalid field entry!", Toast.LENGTH_SHORT).show();
                } else {
                    UserModel model = new UserModel(firstName, lastName, phoneNumber, vehicleNumber, email, dob);
                    DatabaseReference reference = database.getReference().child("Users").child(auth.getUid()).child("Profile");
                    reference.setValue(model);

                    setSharedPreferences();

                    Toast.makeText(UserProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                }
            }
        });

    }

    //--------------------------------------------------------------------------------------------------------------------------------
    private void loadProfile() {
        DatabaseReference reference = database.getReference().child("Users").child(auth.getUid()).child("Profile");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel model = snapshot.getValue(UserModel.class);
                binding.firstName.setText(model.getFirstName());
                binding.lastName.setText(model.getLastName());
                binding.dob.setText(model.getDob());
                binding.email.setText(model.getEmail());
                binding.vehicleNumber.setText(model.getVehicleNumber());
                binding.phoneNumberProfile.setText(model.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                binding.dob.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(UserProfileActivity.this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + ", " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return "Jan";
    }


    private void setSharedPreferences() {
        SharedPreferences sharedPreferencesVehicle = getSharedPreferences("vehicle_number", MODE_PRIVATE);
        SharedPreferences.Editor editorOne = sharedPreferencesVehicle.edit();
        editorOne.putString("vehicle_number", binding.vehicleNumber.getText().toString());
        editorOne.apply();

        SharedPreferences sharedPreferencesUsername = getSharedPreferences("username", MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = sharedPreferencesUsername.edit();
        editorTwo.putString("username", binding.firstName.getText().toString());
        editorTwo.apply();
    }


}