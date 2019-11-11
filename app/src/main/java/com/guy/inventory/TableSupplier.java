package com.guy.inventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import java.text.DecimalFormat;
import java.util.List;

public class TableSupplier extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    LinearLayout llSupplierDetails;
    TextView tvSupplierDetailsName, tvSupplierDetailsSaleSum, tvSupplierDetailsWeightSum, tvSupplierDetailsPrice,
            tvSupplierDetailsLocation, tvSupplierDetailsPhoneNumber, tvSupplierDetailsInsidePhone, tvSupplierDetailsFax,
            tvSupplierDetailsWebSite, tvSupplierDetailsDetails;
    ListView lvSupplierList;
    AdapterSupplier adapter;

    int selectedItem = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_table);

        lvSupplierList = findViewById(R.id.lvSupplierList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvSupplierDetailsLocation = findViewById(R.id.tvSupplierDetailsLocation);
        tvSupplierDetailsPhoneNumber = findViewById(R.id.tvSupplierDetailsPhoneNumber);
        tvSupplierDetailsInsidePhone = findViewById(R.id.tvSupplierDetailsInsidePhone);
        tvSupplierDetailsFax = findViewById(R.id.tvSupplierDetailsFax);
        tvSupplierDetailsWebSite = findViewById(R.id.tvSupplierDetailsWebSite);
        tvSupplierDetailsDetails = findViewById(R.id.tvSupplierDetailsDetails);
        tvSupplierDetailsName = findViewById(R.id.tvSupplierDetailsName);
        tvSupplierDetailsSaleSum = findViewById(R.id.tvSupplierDetailsSaleSum);
        tvSupplierDetailsWeightSum = findViewById(R.id.tvSupplierDetailsWeightSum);
        tvSupplierDetailsPrice = findViewById(R.id.tvSupplierDetailsPrice);

        llSupplierDetails = findViewById(R.id.llSupplierDetails);

        // In Land
        if (findViewById(R.id.supplier_table_land) != null) {
            llSupplierDetails.setVisibility(View.VISIBLE);
        }

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("ספקים");
        actionBar.setDisplayHomeAsUpEnabled(true);

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        final DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setSortBy("name");
        queryBuilder.setPageSize(100);

        showProgress(true);
        tvLoad.setText("טוען נתונים, אנא המתן...");

        Backendless.Data.of(Supplier.class).find(queryBuilder, new AsyncCallback<List<Supplier>>() {
            @Override
            public void handleResponse(List<Supplier> response) {
                InventoryApp.suppliers = response;
                adapter = new AdapterSupplier(TableSupplier.this, InventoryApp.suppliers);
                lvSupplierList.setAdapter(adapter);

                String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
                final DataQueryBuilder buyBuilder = DataQueryBuilder.create();
                buyBuilder.setWhereClause(whereClause);
                buyBuilder.setSortBy("buyDate DESC");
                buyBuilder.setPageSize(100);

                Backendless.Data.of(Buy.class).find(buyBuilder, new AsyncCallback<List<Buy>>() {
                    @Override
                    public void handleResponse(List<Buy> response) {
                        InventoryApp.buys = response;
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(TableSupplier.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableSupplier.this, "טרם נשרמו ספקים", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableSupplier.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        lvSupplierList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;

                if (findViewById(R.id.supplier_table_land) != null) {
                    llSupplierDetails.setVisibility(View.VISIBLE);

                    double saleSum = 0;
                    double weightSum = 0;
                    double price;

                    if (InventoryApp.buys != null) {
                        for (Buy buy : InventoryApp.buys) {
                            if (buy.getSupplierName().equals(InventoryApp.suppliers.get(selectedItem).getName())) {
                                saleSum += buy.getSum();
                                weightSum += buy.getWeight();
                            }
                        }
                    }
                    if (weightSum == 0) {
                        price = 0;
                    } else {
                        price = saleSum / weightSum;
                    }
                    DecimalFormat nf = new DecimalFormat("#,###,###,###.##");
                    tvSupplierDetailsName.setText(InventoryApp.suppliers.get(selectedItem).getName());
                    tvSupplierDetailsLocation.setText("כתובת:  " + InventoryApp.suppliers.get(selectedItem).getLocation());
                    tvSupplierDetailsPhoneNumber.setText("טלפון:  " + InventoryApp.suppliers.get(selectedItem).getPhoneNumber());
                    tvSupplierDetailsInsidePhone.setText("טלפון פנימי:  " + InventoryApp.suppliers.get(selectedItem).getInsidePhone());
                    tvSupplierDetailsFax.setText("פקס:  " + InventoryApp.suppliers.get(selectedItem).getFax());
                    tvSupplierDetailsWebSite.setText("כתובת אתר אינטרנט:  " + InventoryApp.suppliers.get(selectedItem).getWebsite());
                    tvSupplierDetailsDetails.setText("פרטים נוספים:  " + InventoryApp.suppliers.get(selectedItem).getDetails());

                    tvSupplierDetailsSaleSum.setText("סכום שנקנה:  " + nf.format(saleSum) + "$");
                    tvSupplierDetailsWeightSum.setText("משקל שנקנה: " + nf.format(weightSum));
                    tvSupplierDetailsPrice.setText("מחיר ממוצע לקראט: " + nf.format(price) + "$");
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
                Intent intent = new Intent(TableSupplier.this, NewSupplier.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.editIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editSupplier = new Intent(TableSupplier.this, EditSupplier.class);
                    editSupplier.putExtra("index", selectedItem);
                    startActivityForResult(editSupplier, 1);
                    break;
                }

            case R.id.deleteIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט למחיקה", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(TableSupplier.this);
                    alert.setTitle("התראת מחיקה");
                    alert.setMessage("האם אתה בטוח שברצונך למחוק את הספק המסומן");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(true);
                            tvLoad.setText("מוחק את הנתונים אנא המתן...");
                            Backendless.Persistence.of(Supplier.class).remove(InventoryApp.suppliers.get(selectedItem), new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long response) {
                                    InventoryApp.suppliers.remove(selectedItem);
                                    Toast.makeText(TableSupplier.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    showProgress(false);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(TableSupplier.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
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
