package com.guy.inventory.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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
import com.guy.inventory.Adapters.SortAdapter;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Sort;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TableSortingActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ListView lvSortList;
    private SortAdapter sortAdapter;

    private int selectedItem = -1;

    final int PAGE_SIZE = 100;

    private final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
    private final String EMAIL_CLAUSE = "userEmail = '" + InventoryApp.user.getEmail() + "'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_sorting);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lvSortList = findViewById(R.id.lvSortList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("מיונים");
        actionBar.setDisplayHomeAsUpEnabled(true);
        sortBuilder.setWhereClause(EMAIL_CLAUSE);
        sortBuilder.setPageSize(PAGE_SIZE);

        showProgress(true);
        Backendless.Data.of(Sort.class).find(sortBuilder, new AsyncCallback<List<Sort>>() {
            @Override
            public void handleResponse(List<Sort> response) {
                InventoryApp.sorts = response;

                DataQueryBuilder sortInfoBuilder = DataQueryBuilder.create();
                sortInfoBuilder.setWhereClause(EMAIL_CLAUSE);
                sortInfoBuilder.setPageSize(100);
                sortInfoBuilder.setGroupBy("toId");
                sortInfoBuilder.setProperties("Sum(sum) as sum");
                sortInfoBuilder.addProperty("Sum(weight) as weight");
                sortInfoBuilder.addProperty("toId");

                Backendless.Data.of("SortInfo").find(sortInfoBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        for (int i=0; i < response.size(); i++) {
                            String toId = (String) response.get(i).get("toId");
                            for (int y=0; y < InventoryApp.sorts.size(); y++) {
                                if (toId.equals(InventoryApp.sorts.get(y).getObjectId())) {
                                    double sum;
                                    double weight;
                                    if (Objects.requireNonNull(response.get(i).get("sum")).getClass().equals(Integer.class)) {
                                        sum = (int) response.get(i).get("sum");
                                        weight = (int) response.get(i).get("weight");
                                    } else {
                                        sum = (double) response.get(i).get("sum");
                                        weight = (double) response.get(i).get("weight");
                                    }
                                    InventoryApp.sorts.get(y).setSum(sum);
                                    InventoryApp.sorts.get(y).setWeight(weight);
                                    InventoryApp.sorts.get(y).setPrice(sum/weight);
                                }
                            }
                        }
                        sortAdapter = new SortAdapter(TableSortingActivity.this, InventoryApp.sorts);
                        lvSortList.setAdapter(sortAdapter);
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(TableSortingActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableSortingActivity.this, "טרם נשרמו מיונים", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableSortingActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        lvSortList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;
                sortAdapter.setSelectedPosition(position);
                sortAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newIcon:
                Intent intent = new Intent(TableSortingActivity.this, NewSortActivity.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.editIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editSort = new Intent(TableSortingActivity.this, EditSortActivity.class);
                    editSort.putExtra("index", selectedItem);
                    startActivityForResult(editSort, 1);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            sortAdapter.notifyDataSetChanged();
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}