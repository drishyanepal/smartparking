package com.example.smartparkingone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class EsewaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esewa);

        String source = getIntent().getStringExtra("source");

        if (source.equals("bill_fragment")) {
            fromBillFragment();
        } else {
            fromParkingSlotsActivity();
        }

    }

    private void fromParkingSlotsActivity() {
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
                intent.putExtra("entryTime", entryTime);
                intent.putExtra("duration", duration);
                intent.putExtra("exitTime", exitTime);
                intent.putExtra("source","parking_slots_activity");
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private void fromBillFragment() {
        String cost = getIntent().getStringExtra("cost");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(EsewaActivity.this, PaymentActivity.class);
                intent.putExtra("cost", cost);
                intent.putExtra("source","bill_fragment");
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}