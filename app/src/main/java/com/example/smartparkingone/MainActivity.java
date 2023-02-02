package com.example.smartparkingone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartparkingone.Models.BillFragment;
import com.example.smartparkingone.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    LinearLayout headerLayout;
    ImageView imageHeader;
    TextView usernameHeader, vehicleHeader;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        headerLayout = findViewById(R.id.headerLayout);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark


        navigationView.setItemIconTintList(null);
        navigationView.setItemTextAppearance(R.style.NavigationDrawerStyle);
        navigationView.setItemIconSize(90);

        View headerView = navigationView.getHeaderView(0);
        imageHeader = headerView.findViewById(R.id.imageView);
        usernameHeader = headerView.findViewById(R.id.username);
        vehicleHeader = headerView.findViewById(R.id.vehNumber);

        loadHeaderContent();

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        loadFragment(new MapsFragment());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.city:
                        loadFragment(new MapsFragment());
                        break;
                    case R.id.bills:
                        loadFragment(new BillFragment());
                        break;
                    case R.id.history:
                        loadFragment(new HistoryFragment());
                        break;
                    case R.id.settings:
                        loadFragment(new SettingsFragment());
                        break;
                    case R.id.faq:
                        loadFragment(new FAQFragment());
                        break;
                    case R.id.support:
                        loadFragment(new SupportFragment());
                        break;
                    case R.id.log_out:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, PhoneNumberActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void loadHeaderContent() {
        SharedPreferences getPreferencesUsername = getSharedPreferences("username", MODE_PRIVATE);
        String username = getPreferencesUsername.getString("username", "View");
        usernameHeader.setText(username);

        SharedPreferences getPreferencesVehicle = getSharedPreferences("vehicle_number", MODE_PRIVATE);
        String vehicleNumber = getPreferencesVehicle.getString("vehicle_number", "BAA 7283");
        vehicleHeader.setText(vehicleNumber);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                loadHeaderContent();
            }
        }
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "GPS request denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            SharedPreferences getPreferencesVehicle = getSharedPreferences("vehicle_number", MODE_PRIVATE);
            String vehicleNumber = getPreferencesVehicle.getString("vehicle_number", "BAA 7283");
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            String token = task.getResult();
                            database.getReference().child("Users").child(auth.getUid()).child("deviceToken").setValue(token);
                            database.getReference().child("VehicleTokenMap").child(vehicleNumber).setValue(token);
                            Log.w("TOKEN", token);
                        }
                    });
        }
    }
}