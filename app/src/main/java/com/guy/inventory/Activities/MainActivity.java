package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.guy.inventory.Activities.SummaryActivities.Summary;
import com.guy.inventory.Activities.TableActivities.BuysTable;
import com.guy.inventory.Activities.TableActivities.ClientTable;
import com.guy.inventory.Activities.TableActivities.SalesTable;
import com.guy.inventory.Activities.TableActivities.SupplierTable;
import com.guy.inventory.R;


public class MainActivity extends AppCompatActivity {
    Button btnResult, btnBuyTable, btnSaleTable, btnSupplierTable, btnClientTable;

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

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("ניהול מלאי");
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Summary.class);
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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
