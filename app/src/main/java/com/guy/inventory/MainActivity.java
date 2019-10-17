package com.guy.inventory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    Button btnResult, btnBuy, btnSale, btnBuyShow, btnSaleShow;
    public static LinkedList<Buy> buyArray = new LinkedList<>();
    public static LinkedList<Sale> saleArray = new LinkedList<>();
    public final static int buy = 1;
    public final static int sale = 2;

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
