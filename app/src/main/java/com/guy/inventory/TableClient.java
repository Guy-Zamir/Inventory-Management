package com.guy.inventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import java.text.DecimalFormat;
import java.util.List;

public class TableClient extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    LinearLayout llClientDetails;
    TextView tvClientDetailsName, tvClientDetailsSaleSum, tvClientDetailsWeightSum, tvClientDetailsPrice,
            tvClientDetailsLocation, tvClientDetailsPhoneNumber, tvClientDetailsInsidePhone, tvClientDetailsFax,
            tvClientDetailsWebSite, tvClientDetailsDetails;
    ListView lvClientList;
    AdapterClient adapter;

    int selectedItem = -1;

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
        tvClientDetailsSaleSum = findViewById(R.id.tvClientDetailsSaleSum);
        tvClientDetailsWeightSum = findViewById(R.id.tvClientDetailsWeightSum);
        tvClientDetailsPrice = findViewById(R.id.tvClientDetailsPrice);

        llClientDetails = findViewById(R.id.llClientDetails);

        // In Land
        if (findViewById(R.id.client_table_land) != null) {
            llClientDetails.setVisibility(View.VISIBLE);
        }

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("לקוחות");
        actionBar.setDisplayHomeAsUpEnabled(true);

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        final DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("objectId");
        queryBuilder.setPageSize(100);

        showProgress(true);
        tvLoad.setText("טוען נתונים, אנא המתן...");

        Backendless.Data.of(Client.class).find(queryBuilder, new AsyncCallback<List<Client>>() {
            @Override
            public void handleResponse(List<Client> response) {
                InventoryApp.clients = response;
                adapter = new AdapterClient(TableClient.this, InventoryApp.clients);
                lvClientList.setAdapter(adapter);
                Backendless.Data.of(Sale.class).find(queryBuilder, new AsyncCallback<List<Sale>>() {
                    @Override
                    public void handleResponse(List<Sale> response) {
                        InventoryApp.sales = response;
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(TableClient.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
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

                    double saleSum = 0;
                    double weightSum = 0;
                    double price;

                    if (InventoryApp.sales != null) {
                        for (Sale sale : InventoryApp.sales) {
                            if (sale.getClientName().equals(InventoryApp.clients.get(selectedItem).getName())) {
                                saleSum += sale.getSaleSum();
                                weightSum += sale.getWeight();
                            }
                        }
                    }
                    if (weightSum == 0) {
                        price = 0;
                    } else {
                        price = saleSum / weightSum;
                    }
                    DecimalFormat nf = new DecimalFormat("#,###,###,###.##");
                    tvClientDetailsName.setText(InventoryApp.clients.get(selectedItem).getName());
                    tvClientDetailsLocation.setText("כתובת:  " + InventoryApp.clients.get(selectedItem).getLocation());
                    tvClientDetailsPhoneNumber.setText("טלפון:  " + InventoryApp.clients.get(selectedItem).getPhoneNumber());
                    tvClientDetailsInsidePhone.setText("טלפון פנימי:  " + InventoryApp.clients.get(selectedItem).getInsidePhone());
                    tvClientDetailsFax.setText("פקס:  " + InventoryApp.clients.get(selectedItem).getFax());
                    tvClientDetailsWebSite.setText("כתובת אתר אינטרנט:  " + InventoryApp.clients.get(selectedItem).getWebsite());
                    tvClientDetailsDetails.setText("פרטים נוספים:  " + InventoryApp.clients.get(selectedItem).getDetails());

                    tvClientDetailsSaleSum.setText("סכום שנקנה:  " + nf.format(saleSum) + "$");
                    tvClientDetailsWeightSum.setText("משקל שנקנה: " + nf.format(weightSum));
                    tvClientDetailsPrice.setText("מחיר ממוצע לקראט: " + nf.format(price) + "$");
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
                    startActivityForResult(editClient, 1);
                    break;
                }

            case R.id.deleteIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט למחיקה", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(TableClient.this);
                    alert.setTitle("התראת מחיקה");
                    alert.setMessage("האם אתה בטוח שברצונך למחוק את הספק המסומן");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(true);
                            tvLoad.setText("מוחק את הנתונים אנא המתן...");
                            Backendless.Persistence.of(Client.class).remove(InventoryApp.clients.get(selectedItem), new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long response) {
                                    InventoryApp.clients.remove(selectedItem);
                                    Toast.makeText(TableClient.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    showProgress(false);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(TableClient.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alert.show();
                }
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
