package com.guy.inventory.Activities.TableActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
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
import com.guy.inventory.Activities.EditActivities.EditBuy;
import com.guy.inventory.Activities.EditActivities.EditBuyDone;
import com.guy.inventory.Activities.InventoryApp;
import com.guy.inventory.Activities.NewActivities.NewBuy;
import com.guy.inventory.Adapters.BuyDoneAdapter;
import com.guy.inventory.Adapters.BuysAdapter;
import com.guy.inventory.R;
import com.guy.inventory.Classes.Buy;
import java.util.List;

public class BuysTable extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    ListView lvBuyList, lvBuyDone;
    BuysAdapter adapterBuy;
    BuyDoneAdapter adapterDone;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buys_table);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lvBuyList = findViewById(R.id.lvBuyList);
        lvBuyDone = findViewById(R.id.lvBuyDone);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("קניות");
        actionBar.setDisplayHomeAsUpEnabled(true);

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("created");
        queryBuilder.setPageSize(100);
        showProgress(true);

        Backendless.Data.of(Buy.class).find(queryBuilder, new AsyncCallback<List<Buy>>() {
            @Override
            public void handleResponse(List<Buy> response) {
                InventoryApp.buys = response;
                adapterBuy = new BuysAdapter(BuysTable.this, InventoryApp.buys);
                adapterDone = new BuyDoneAdapter(BuysTable.this, InventoryApp.buys);
                lvBuyList.setAdapter(adapterBuy);
                lvBuyDone.setAdapter(adapterDone);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(BuysTable.this, "טרם נשרמו קניות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BuysTable.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
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

        lvBuyDone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;
                AlertDialog.Builder alert = new AlertDialog.Builder(BuysTable.this);
                alert.setTitle("שינוי נתונים");
                if (InventoryApp.buys.get(index).isDone()) {
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את החבילה ללא גמורה?");
                } else {
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את החבילה לגמורה?");
                }
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes,  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (InventoryApp.buys.get(index).isDone()) {
                            InventoryApp.buys.get(index).setDone(false);
                        } else {
                            InventoryApp.buys.get(index).setDone(true);
                        }
                        showProgress(true);
                        Backendless.Persistence.save(InventoryApp.buys.get(index), new AsyncCallback<Buy>() {
                            @Override
                            public void handleResponse(Buy response) {
                                showProgress(false);
                                if (InventoryApp.buys.get(index).isDone()) {
                                    Intent intent = new Intent(BuysTable.this, EditBuyDone.class);
                                    intent.putExtra("index", index);
                                    startActivityForResult(intent, 1);
                                } else {
                                    Toast.makeText(BuysTable.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                    adapterDone.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(BuysTable.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alert.show();
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
                Intent intent = new Intent(BuysTable.this, NewBuy.class);
                startActivityForResult(intent, 1);
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
            adapterBuy.notifyDataSetChanged();
            adapterDone.notifyDataSetChanged();
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
