package com.guy.inventory.Activities.TableActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.guy.inventory.Activities.EditActivities.EditBuy;
import com.guy.inventory.Activities.InventoryApp;
import com.guy.inventory.Adapters.BuysAdapter;
import com.guy.inventory.R;
import com.guy.inventory.Classes.Buy;
import java.util.List;

public class BuysTable extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    ListView lvBuyList;
    BuysAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buys_table);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lvBuyList = findViewById(R.id.lvBuyList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("created");

        showProgress(true);
        tvLoad.setText("טוען נתונים, אנא המתן...");

        Backendless.Data.of(Buy.class).find(queryBuilder, new AsyncCallback<List<Buy>>() {
            @Override
            public void handleResponse(List<Buy> response) {
                InventoryApp.buys = response;
                adapter = new BuysAdapter(BuysTable.this, InventoryApp.buys);
                lvBuyList.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(BuysTable.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });

        lvBuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BuysTable.this, EditBuy.class);
                intent.putExtra("index", position);
                startActivityForResult(intent, 1);
            }
        });
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
