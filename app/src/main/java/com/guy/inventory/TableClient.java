package com.guy.inventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import java.util.List;

public class TableClient extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    LinearLayout llClientDetails;
    TextView tvClientDetailsName, tvClientDetailsLocation, tvClientDetailsPhoneNumber,
            tvClientDetailsInsidePhone, tvClientDetailsFax,
            tvClientDetailsWebSite, tvClientDetailsDetails;
    ListView lvClientList;
    AdapterClient adapter;

    int pageSize = 100;
    int selectedItem = -1;

    boolean client;

    String aSupplier = "supplier";
    String aClient = "client";

    final DataQueryBuilder clientBuilder = DataQueryBuilder.create();
    final String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final String supplierClause = "supplier = '" + aSupplier + "'";
    final String clientClause = "supplier = '" + aClient + "'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_table);

        lvClientList = findViewById(R.id.lvClientList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvClientDetailsLocation = findViewById(R.id.tvClientDetailsLocation);
        tvClientDetailsPhoneNumber = findViewById(R.id.tvClientDetailsPhoneNumber);
        tvClientDetailsInsidePhone = findViewById(R.id.tvClientDetailsInsidePhone);
        tvClientDetailsFax = findViewById(R.id.tvClientDetailsFax);
        tvClientDetailsWebSite = findViewById(R.id.tvClientDetailsWebSite);
        tvClientDetailsDetails = findViewById(R.id.tvClientDetailsDetails);
        tvClientDetailsName = findViewById(R.id.tvClientDetailsName);
        llClientDetails = findViewById(R.id.llClientDetails);

        client = getIntent().getBooleanExtra("client", true);

        // In Land Mode
        if (findViewById(R.id.client_table_land) != null) {
            llClientDetails.setVisibility(View.VISIBLE);
        }

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        if (client) {
            actionBar.setTitle("לקוחות");
        } else {
            actionBar.setTitle("ספקים");
        }
        actionBar.setDisplayHomeAsUpEnabled(true);

        clientBuilder.setWhereClause(whereClause);
        clientBuilder.setSortBy("name");
        if (client) {
            clientBuilder.setHavingClause(clientClause);
        } else {
            clientBuilder.setHavingClause(supplierClause);
        }
        clientBuilder.setPageSize(pageSize);

        showProgress(true);
        Backendless.Data.of(Client.class).find(clientBuilder, new AsyncCallback<List<Client>>() {
            @Override
            public void handleResponse(List<Client> response) {
                InventoryApp.clients = response;
                adapter = new AdapterClient(TableClient.this, InventoryApp.clients);
                lvClientList.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableClient.this, "טרם נשרמו לקוחות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableClient.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        lvClientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;
                if (findViewById(R.id.client_table_land) != null) {
                    llClientDetails.setVisibility(View.VISIBLE);

                    tvClientDetailsName.setText(InventoryApp.clients.get(selectedItem).getName());
                    tvClientDetailsLocation.setText("כתובת:  " + InventoryApp.clients.get(selectedItem).getLocation());
                    tvClientDetailsPhoneNumber.setText("טלפון:  " + InventoryApp.clients.get(selectedItem).getPhoneNumber());
                    tvClientDetailsInsidePhone.setText("טלפון פנימי:  " + InventoryApp.clients.get(selectedItem).getInsidePhone());
                    tvClientDetailsFax.setText("פקס:  " + InventoryApp.clients.get(selectedItem).getFax());
                    tvClientDetailsWebSite.setText("כתובת אתר אינטרנט:  " + InventoryApp.clients.get(selectedItem).getWebsite());
                    tvClientDetailsDetails.setText("פרטים נוספים:  " + InventoryApp.clients.get(selectedItem).getDetails());

                } else {
                    adapter.setSelectedPosition(position);
                    adapter.notifyDataSetChanged();
                }
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
                Intent intent = new Intent(TableClient.this, NewClient.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.editIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editClient = new Intent(TableClient.this, EditClient.class);
                    editClient.putExtra("index", selectedItem);
                    editClient.putExtra("client", client);
                    startActivityForResult(editClient, 1);

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
            adapter.notifyDataSetChanged();
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
