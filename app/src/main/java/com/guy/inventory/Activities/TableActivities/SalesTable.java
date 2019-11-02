package com.guy.inventory.Activities.TableActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Activities.EditActivities.EditSale;
import com.guy.inventory.Activities.InventoryApp;
import com.guy.inventory.Activities.NewActivities.NewSale;
import com.guy.inventory.Adapters.SalesAdapter;
import com.guy.inventory.R;
import com.guy.inventory.Classes.Sale;
import java.util.List;

public class SalesTable extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    ListView lvSaleList;
    SalesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_table);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lvSaleList = findViewById(R.id.lvSaleList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("מכירות");

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("created");
        queryBuilder.setPageSize(100);

        showProgress(true);

        Backendless.Data.of(Sale.class).find(queryBuilder, new AsyncCallback<List<Sale>>() {
            @Override
            public void handleResponse(List<Sale> response) {
                InventoryApp.sales = response;
                adapter = new SalesAdapter(SalesTable.this, InventoryApp.sales);
                lvSaleList.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(SalesTable.this, "טרם נשרמו מכירות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SalesTable.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        lvSaleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SalesTable.this, EditSale.class);
                intent.putExtra("index", position);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tables, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newIcon:
                Intent intent = new Intent(SalesTable.this, NewSale.class);
                startActivityForResult(intent, 1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            adapter.notifyDataSetChanged();
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
