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
import java.util.Calendar;
import java.util.List;

public class TableSale extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvSaleDetailsClientName, tvSaleDetailsBuyDate, tvSaleDetailsPayDate, tvSaleDetailsID,
            tvSaleDetailsPrice, tvSaleDetailsWeight, tvSaleDetailsDays, tvSaleDetailsSum;

    ListView lvSaleList;
    LinearLayout llSaleDetails;
    AdapterSales adapter;
    int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_table);

        lvSaleList = findViewById(R.id.lvSaleList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvSaleDetailsClientName = findViewById(R.id.tvSaleDetailsClientName);
        tvSaleDetailsBuyDate = findViewById(R.id.tvSaleDetailsBuyDate);
        tvSaleDetailsPayDate = findViewById(R.id.tvSaleDetailsPayDate);
        tvSaleDetailsID = findViewById(R.id.tvSaleDetailsID);
        tvSaleDetailsPrice = findViewById(R.id.tvSaleDetailsPrice);
        tvSaleDetailsWeight = findViewById(R.id.tvSaleDetailsWeight);
        tvSaleDetailsDays = findViewById(R.id.tvSaleDetailsDays);
        tvSaleDetailsSum = findViewById(R.id.tvSaleDetailsSum);
        llSaleDetails = findViewById(R.id.llSaleDetails);

        // In Land
        if (findViewById(R.id.table_sales_layout_land) != null) {
            llSaleDetails.setVisibility(View.VISIBLE);
        }

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("מכירות");
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                adapter = new AdapterSales(TableSale.this, InventoryApp.sales);
                lvSaleList.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableSale.this, "טרם נשרמו מכירות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        lvSaleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;

                if (findViewById(R.id.table_sales_layout_land) != null) {
                    llSaleDetails.setVisibility(View.VISIBLE);

                    DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
                    Calendar saleDate = Calendar.getInstance();
                    saleDate.setTime(InventoryApp.sales.get(position).getSaleDate());
                    @SuppressLint("DefaultLocale") String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
                    @SuppressLint("DefaultLocale") String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH)+1);
                    @SuppressLint("DefaultLocale") String buyYear = String.format("%02d", saleDate.get(Calendar.YEAR));

                    Calendar payDate = Calendar.getInstance();
                    payDate.setTime(InventoryApp.sales.get(position).getPayDate());
                    @SuppressLint("DefaultLocale") String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
                    @SuppressLint("DefaultLocale") String payMonth = String.format("%02d", payDate.get(Calendar.MONTH)+1);
                    @SuppressLint("DefaultLocale") String payYear = String.format("%02d", payDate.get(Calendar.YEAR));

                    tvSaleDetailsBuyDate.setText("תאריך קניה: " + buyDays + "/" + buyMonth + "/" + buyYear);
                    tvSaleDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth+ "/" + payYear);

                    tvSaleDetailsClientName.setText(InventoryApp.sales.get(position).getClientName());
                    tvSaleDetailsID.setText("מספר חשבונית:  " + InventoryApp.sales.get(position).getId());
                    tvSaleDetailsPrice.setText("מחיר ממוצע:  " + nf.format(InventoryApp.sales.get(position).getPrice()) + "$");
                    tvSaleDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.sales.get(position).getWeight()));
                    tvSaleDetailsDays.setText("מספר ימים:  " + nf.format(InventoryApp.sales.get(position).getDays()));
                    tvSaleDetailsSum.setText("סכום עסקה:  " + nf.format(InventoryApp.sales.get(position).getSaleSum()) + "$");

                    // In Port
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
                Intent newSale = new Intent(TableSale.this, NewSale.class);
                startActivityForResult(newSale, 1);
                break;

            case R.id.editIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editSale = new Intent(TableSale.this, EditSale.class);
                    editSale.putExtra("index", selectedItem);
                    startActivityForResult(editSale, 1);
                    break;
                }

            case R.id.deleteIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט למחיקה", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(TableSale.this);
                    alert.setTitle("התראת מחיקה");
                    alert.setMessage("האם אתה בטוח שברצונך למחוק את המכירה המסומנת?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(true);
                            tvLoad.setText("מוחק את הנתונים אנא המתן...");
                            Backendless.Persistence.of(Sale.class).remove(InventoryApp.sales.get(selectedItem), new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long response) {
                                    InventoryApp.sales.remove(selectedItem);
                                    Toast.makeText(TableSale.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    showProgress(false);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(TableSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
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
