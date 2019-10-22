package com.guy.inventory.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.guy.inventory.Activities.NewActivities.NewBuy;
import com.guy.inventory.Activities.NewActivities.NewClient;
import com.guy.inventory.Activities.NewActivities.NewSale;
import com.guy.inventory.Activities.NewActivities.NewSupplier;
import com.guy.inventory.Activities.TableActivities.BuysTable;
import com.guy.inventory.Activities.TableActivities.ClientTable;
import com.guy.inventory.Activities.TableActivities.SalesTable;
import com.guy.inventory.Activities.TableActivities.SupplierTable;
import com.guy.inventory.R;

public class MainActivity extends AppCompatActivity {
    Button btnResult, btnNewBuy, btnNewSale, btnBuyTable, btnSaleTable, btnNewSupplier, btnSupplierTable, btnNewClient, btnClientTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnResult = findViewById(R.id.btnResult);
        btnNewBuy = findViewById(R.id.btnNewBuy);
        btnNewSale = findViewById(R.id.btnNewSale);
        btnBuyTable = findViewById(R.id.btnBuyTable);
        btnSaleTable = findViewById(R.id.btnSaleTable);
        btnNewSupplier = findViewById(R.id.btnNewSupplier);
        btnSupplierTable = findViewById(R.id.btnSupplierTable);
        btnNewClient = findViewById(R.id.btnNewClient);
        btnClientTable = findViewById(R.id.btnClientTable);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Summary.class);
                startActivity(intent);
            }
        });

        btnNewBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewBuy.class);
                startActivity(intent);
            }
        });

        btnNewSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewSale.class);
                startActivity(intent);
            }
        });

        btnBuyTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuysTable.class);
                startActivity(intent);
            }
        });

        btnSaleTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalesTable.class);
                startActivity(intent);
            }
        });

        btnNewSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewSupplier.class);
                startActivity(intent);
            }
        });

        btnNewClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewClient.class);
                startActivity(intent);
            }
        });

        btnSupplierTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SupplierTable.class);
                startActivity(intent);
            }
        });

        btnClientTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientTable.class);
                startActivity(intent);
            }
        });
    }
}
