package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;

public class MainActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    Button btnResult, btnBuyTable, btnSaleTable, btnSupplierTable, btnClientTable, btnExportTable,
    btnSortingTable, btnMemo, btnBroker, btn2019, btn2020 ,btnDemo;

    final String EMAIL_2019 = "zamirbit@012.net.il";
    final String EMAIL_2020 = "zamirbit20@012.net.il";
    final String EMAIL_TEST = "test@test.com";
    final String PASSWORD = "1";

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
        btnBroker = findViewById(R.id.btnBroker);
        btnMemo = findViewById(R.id.btnMemo);
        btn2019 = findViewById(R.id.btn2019);
        btn2020 = findViewById(R.id.btn2020);
        btnDemo = findViewById(R.id.btnDemo);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("ניהול מלאי");
        actionBar.setDisplayHomeAsUpEnabled(true);

        btn2019.setBackgroundResource(InventoryApp.user.getEmail().equals(EMAIL_2019) ? R.drawable.table_row_selected : R.drawable.table_row);
        btn2020.setBackgroundResource(InventoryApp.user.getEmail().equals(EMAIL_2020) ? R.drawable.table_row_selected : R.drawable.table_row);
        btnDemo.setBackgroundResource(InventoryApp.user.getEmail().equals(EMAIL_TEST) ? R.drawable.table_row_selected : R.drawable.table_row);

        btnMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BalanceActivity.class);
                startActivity(intent);
            }
        });

        btnBroker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BrokerMainSortActivity.class);
                startActivity(intent);
            }
        });

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

        btn2019.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                Backendless.UserService.login(EMAIL_2019, PASSWORD, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        showProgress(false);
                        InventoryApp.user = response;
                        btn2019.setBackgroundResource(R.drawable.table_row_selected);
                        btn2020.setBackgroundResource(R.drawable.table_row);
                        btnDemo.setBackgroundResource(R.drawable.table_row);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(MainActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, true);
            }
        });

        btn2020.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                Backendless.UserService.login(EMAIL_2020, PASSWORD, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        showProgress(false);
                        InventoryApp.user = response;
                        btn2019.setBackgroundResource(R.drawable.table_row);
                        btn2020.setBackgroundResource(R.drawable.table_row_selected);
                        btnDemo.setBackgroundResource(R.drawable.table_row);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(MainActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, true);
            }
        });

        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                Backendless.UserService.login(EMAIL_TEST, PASSWORD, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        showProgress(false);
                        InventoryApp.user = response;
                        btn2019.setBackgroundResource(R.drawable.table_row);
                        btn2020.setBackgroundResource(R.drawable.table_row);
                        btnDemo.setBackgroundResource(R.drawable.table_row_selected);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(MainActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, true);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
