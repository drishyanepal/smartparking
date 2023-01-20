package com.example.smartparkingone;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartparkingone.Models.UserModel;
import com.example.smartparkingone.databinding.ActivityPhoneNumberBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {
    ActivityPhoneNumberBinding binding;
    private int screenNumber = 1;
    private KeyListener listener;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        binding.policyTextView.setText(Html.fromHtml("By tapping" + " &#34Next&#34" + " you agree to <u>Terms and Conditions</u>" + " and " + "<u>Privacy Policy</u>"));

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PhoneNumberActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        listener = binding.phoneNumber.getKeyListener();
        showFirstScreen();

        //---------------------on clicks------------------------------------------------------------------------------
        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (screenNumber == 1) {
                    checkPhoneNumberValidity();
                } else if (screenNumber == 3) {
                    inputFieldsAndLogin();
                }
            }
        });

        binding.phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (screenNumber == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PhoneNumberActivity.this);
                    builder.setMessage("Do you want to change number?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showFirstScreen();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                    alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                } else {

                }
            }
        });
        //--------------------on clicks end---------------------------------------------------------------------------
        binding.loginCode.addTextChangedListener(textWatcher);
    }//on create end


    //--------------------------login code verification-------------------------------------------------------------------
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String loginCode = binding.loginCode.getText().toString();
            if (loginCode.length() == 6) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, loginCode);
                signInWithPhoneAuthCredential(credential);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    //--------------------------login code verification end---------------------------------------------------------------


    private void showFirstScreen() {
        screenNumber = 1;
        binding.titleTextView.setText("Join us via phone number");
        binding.phoneNumber.setKeyListener(listener);
        binding.loginCode.setVisibility(View.GONE);
        binding.username.setVisibility(View.GONE);
        binding.vehicleNumberLogin.setVisibility(View.GONE);
        binding.nextButton.setVisibility(View.VISIBLE);
    }

    private void showSecondScreen() {
        screenNumber = 2;
        binding.titleTextView.setText("We sent you a login code");
        binding.phoneNumber.setKeyListener(null);
        binding.loginCode.setVisibility(View.VISIBLE);
        binding.loginCode.requestFocus();
        binding.username.setVisibility(View.GONE);
        binding.vehicleNumberLogin.setVisibility(View.GONE);
        binding.nextButton.setVisibility(View.GONE);
    }

    private void showThirdScreen() {
        screenNumber = 3;
        binding.titleTextView.setText("Enter Details:");
        binding.loginCode.setVisibility(View.GONE);
        binding.username.setVisibility(View.VISIBLE);
        binding.username.requestFocus();
        binding.vehicleNumberLogin.setVisibility(View.VISIBLE);
        binding.nextButton.setVisibility(View.VISIBLE);
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber("+977" + phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(PhoneNumberActivity.this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PhoneNumberActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                    SharedPreferences getPreferences = getSharedPreferences("old_user", MODE_PRIVATE);
                    boolean oldUser = getPreferences.getBoolean("old_user", true);

                    if (oldUser == false) {
                        Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showThirdScreen();
                        SharedPreferences sharedPreferences = getSharedPreferences("old_user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("old_user", false);
                        editor.apply();
                    }

                } else {
                    Toast.makeText(PhoneNumberActivity.this, "Verification failed 22", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkPhoneNumberValidity() {
        String phoneNumber = binding.phoneNumber.getText().toString();
        if (phoneNumber.length() == 10) {
            sendVerificationCode(phoneNumber);
            showSecondScreen();
        } else {
            Toast.makeText(PhoneNumberActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
        }
    }

    private void inputFieldsAndLogin() {
        if (binding.username.getText().toString().isEmpty() || binding.vehicleNumberLogin.getText().toString().isEmpty()) {
            Toast.makeText(PhoneNumberActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            String firstName = binding.username.getText().toString();
            String vehicleNumber = binding.vehicleNumberLogin.getText().toString();
            String phoneNumber = binding.phoneNumber.getText().toString().trim();

            UserModel model = new UserModel();
            model.setFirstName(firstName);
            model.setVehicleNumber(vehicleNumber);
            model.setPhoneNumber(phoneNumber);

            DatabaseReference reference = database.getReference().child("Users").child(auth.getUid()).child("Profile");
            reference.setValue(model);

            setSharedPreferences();

            Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void setSharedPreferences() {
        SharedPreferences sharedPreferencesPhone = getSharedPreferences("phone_number", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesPhone.edit();
        editor.putString("phone_number", binding.phoneNumber.getText().toString());
        editor.apply();

        SharedPreferences sharedPreferencesVehicle = getSharedPreferences("vehicle_number", MODE_PRIVATE);
        SharedPreferences.Editor editorOne = sharedPreferencesVehicle.edit();
        editorOne.putString("vehicle_number", binding.vehicleNumberLogin.getText().toString());
        editorOne.apply();

        SharedPreferences sharedPreferencesUsername = getSharedPreferences("username", MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = sharedPreferencesUsername.edit();
        editorTwo.putString("username", binding.username.getText().toString());
        editorTwo.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}