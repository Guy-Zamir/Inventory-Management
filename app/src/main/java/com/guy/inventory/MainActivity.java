package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button btnResult, btnBuy, btnSale, btnBuyShow, btnSaleShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnResult = findViewById(R.id.btnResult);
        btnBuy = findViewById(R.id.btnBuy);
        btnSale = findViewById(R.id.btnSale);
        btnBuyShow = findViewById(R.id.btnBuyShow);
        btnSaleShow = findViewById(R.id.btnSaleShow);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                startActivity(intent);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuyActivity.class);
                startActivity(intent);
            }
        });

        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SaleActivity.class);
                startActivity(intent);
            }
        });

        btnBuyShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuyShowActivity.class);
                startActivity(intent);
            }
        });

        btnSaleShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SaleShowActivity.class);
                startActivity(intent);
            }
        });
    }
}
