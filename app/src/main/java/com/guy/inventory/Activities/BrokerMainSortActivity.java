package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.guy.inventory.R;

public class BrokerMainSortActivity extends AppCompatActivity {
    Button btnPR, btnAS, btnRA, btnEM, btnCU, btnBA, btnAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_main_sort);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnPR = findViewById(R.id.btnPR);
        btnAS = findViewById(R.id.btnAS);
        btnRA = findViewById(R.id.btnRA);
        btnEM = findViewById(R.id.btnEM);
        btnCU = findViewById(R.id.btnCU);
        btnBA = findViewById(R.id.btnBA);
        btnAll = findViewById(R.id.btnAll);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("מיונים - ניב");
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "ALL");
                startActivity(intent);
            }
        });

        btnAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "AS");
                startActivity(intent);
            }
        });

        btnEM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "EM");
                startActivity(intent);
            }
        });

        btnPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "PR");
                startActivity(intent);
            }
        });

        btnRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "RA");
                startActivity(intent);
            }
        });

        btnCU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "CU");
                startActivity(intent);
            }
        });

        btnBA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "BA");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
