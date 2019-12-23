package com.guy.inventory.Activities;

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
import com.guy.inventory.Adapters.ClientAdapter;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Client;

import java.util.List;

public class TableClientActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    LinearLayout llClientDetails;
    TextView tvClientDetailsName, tvClientDetailsLocation, tvClientDetailsPhoneNumber,
            tvClientDetailsInsidePhone, tvClientDetailsFax,
            tvClientDetailsWebSite, tvClientDetailsDetails;
    ListView lvClientList;
    ClientAdapter clientAdapter;

    int PAGE_SIZE = 100;
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
        actionBar.setTitle((client) ? "לקוחות" : "ספקים");
        actionBar.setDisplayHomeAsUpEnabled(true);

        clientBuilder.setWhereClause(whereClause);
        clientBuilder.setSortBy("name");
        clientBuilder.setHavingClause((client) ? clientClause : supplierClause);
        clientBuilder.setPageSize(PAGE_SIZE);

        showProgress(true);
        Backendless.Data.of(Client.class).find(clientBuilder, new AsyncCallback<List<Client>>() {
            @Override
            public void handleResponse(List<Client> response) {
                InventoryApp.clients = response;
                clientAdapter = new ClientAdapter(TableClientActivity.this, InventoryApp.clients);
                lvClientList.setAdapter(clientAdapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableClientActivity.this, "טרם נשרמו לקוחות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableClientActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
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
                    clientAdapter.setClient(client);
                    clientAdapter.setSelectedPosition(position);
                    clientAdapter.notifyDataSetChanged();
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
                Intent intent = new Intent(TableClientActivity.this, NewClientActivity.class);
                intent.putExtra("client", client);
                startActivityForResult(intent, 1);
                break;

            case R.id.editIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editClient = new Intent(TableClientActivity.this, EditClientActivity.class);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                clientAdapter.notifyDataSetChanged();
            }
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
