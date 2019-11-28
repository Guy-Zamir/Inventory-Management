package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.guy.inventory.R;

public class MainActivity extends AppCompatActivity {
    Button btnResult, btnBuyTable, btnSaleTable, btnSupplierTable, btnClientTable, btnExportTable,
    btnSortingTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnResult = findViewById(R.id.btnResult);
        btnBuyTable = findViewById(R.id.btnBuyTable);
        btnSaleTable = findViewById(R.id.btnSaleTable);
        btnSupplierTable = findViewById(R.id.btnSupplierTable);
        btnClientTable = findViewById(R.id.btnClientTable);
        btnExportTable = findViewById(R.id.btnExportTable);
        btnSortingTable = findViewById(R.id.btnSortingTable);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("ניהול מלאי");
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BalanceActivity.class);
                startActivity(intent);
            }
        });

        btnBuyTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TableBuyActivity.class);
                startActivity(intent);
            }
        });

        btnSaleTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TableSaleActivity.class);
                intent.putExtra("exports", false);
                startActivity(intent);
            }
        });

        btnSupplierTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TableClientActivity.class);
                intent.putExtra("client", false);
                startActivity(intent);
            }
        });

        btnClientTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TableClientActivity.class);
                intent.putExtra("client", true);
                startActivity(intent);
            }
        });

        btnExportTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TableSaleActivity.class);
                intent.putExtra("exports", true);
                startActivity(intent);
            }
        });

        btnSortingTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TableSortingActivity.class);
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
