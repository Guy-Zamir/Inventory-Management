package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnResult, btnBuy, btnSale, btnBuyShow, btnSaleShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        btnResult = findViewById(R.id.btnResult);
        btnBuy = findViewById(R.id.btnBuy);
        btnSale = findViewById(R.id.btnSale);
        btnBuyShow = findViewById(R.id.btnBuyShow);
        btnSaleShow = findViewById(R.id.btnSaleShow);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Summary.class);
                startActivity(intent);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewBuy.class);
                startActivity(intent);
            }
        });

        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewSale.class);
                startActivity(intent);
            }
        });

        btnBuyShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuysTable.class);
                startActivity(intent);
            }
        });

        btnSaleShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalesTable.class);
                startActivity(intent);
            }
        });
    }
}
