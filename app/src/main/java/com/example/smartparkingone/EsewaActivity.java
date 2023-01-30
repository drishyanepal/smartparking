package com.example.smartparkingone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EsewaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esewa);
        String slotId = getIntent().getStringExtra("slotId");
        String cost = getIntent().getStringExtra("cost");
        String entryTime = getIntent().getStringExtra("entryTime");
        String duration = getIntent().getStringExtra("duration");
        String exitTime = getIntent().getStringExtra("exitTime");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(EsewaActivity.this, PaymentActivity.class);
                intent.putExtra("slotId", slotId);
                intent.putExtra("cost", cost);
                intent.putExtra("entryTime",entryTime);
                intent.putExtra("duration",duration);
                intent.putExtra("exitTime",exitTime);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}