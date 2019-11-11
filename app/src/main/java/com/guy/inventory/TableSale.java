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

    AdapterSales adapterSales;
    AdapterExports adapterExports;

    int pageSize = 100;
    int selectedItem = -1;
    boolean exports;

    final DataQueryBuilder saleBuilder = DataQueryBuilder.create();

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

        // If the activity is an Export or not
        exports = getIntent().getBooleanExtra("exports", false);

        // An Export Sale
        if (exports) {
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("יצואים");
            actionBar.setDisplayHomeAsUpEnabled(true);

            String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
            DataQueryBuilder queryBuilder = DataQueryBuilder.create();
            queryBuilder.setWhereClause(whereClause);
            queryBuilder.setGroupBy("saleDate DESC");
            queryBuilder.setPageSize(pageSize);
            showProgress(true);

            Backendless.Data.of(Export.class).find(queryBuilder, new AsyncCallback<List<Export>>() {
                @Override
                public void handleResponse(List<Export> response) {
                    InventoryApp.exports = response;
                    adapterExports = new AdapterExports(TableSale.this, InventoryApp.exports);
                    lvSaleList.setAdapter(adapterExports);
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

        // Not an Export Sale
        } else {

            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("מכירות");
            actionBar.setDisplayHomeAsUpEnabled(true);

            String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
            saleBuilder.setWhereClause(whereClause);
            saleBuilder.setSortBy("saleDate DESC");
            saleBuilder.setPageSize(pageSize);
            showProgress(true);

            Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {
                int offset = 0;
                @Override
                public void handleResponse(List<Sale> response) {
                    // Up to 100
                    InventoryApp.sales = response;

                    if (InventoryApp.sales.size() == pageSize) {
                        offset += InventoryApp.sales.size();
                        saleBuilder.setOffset(offset);

                        Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {
                            @Override
                            public void handleResponse(List<Sale> response) {
                                // Up to 200
                                InventoryApp.sales.addAll(response);

                                if (InventoryApp.sales.size() == pageSize * 2) {
                                    offset += InventoryApp.sales.size();
                                    saleBuilder.setOffset(offset);

                                    Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {
                                        @Override
                                        public void handleResponse(List<Sale> response) {
                                            // Up to 300
                                            InventoryApp.sales.addAll(response);

                                            if (InventoryApp.sales.size() == pageSize * 3) {
                                                offset += InventoryApp.sales.size();
                                                saleBuilder.setOffset(offset);

                                                Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {
                                                    @Override
                                                    public void handleResponse(List<Sale> response) {
                                                        // Up to 400
                                                        InventoryApp.sales.addAll(response);
                                                        adapterSales = new AdapterSales(TableSale.this, InventoryApp.sales);
                                                        lvSaleList.setAdapter(adapterSales);
                                                        showProgress(false);
                                                    }

                                                    @Override
                                                    public void handleFault(BackendlessFault fault) {
                                                        Toast.makeText(TableSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                        showProgress(false);
                                                    }
                                                });
                                            } else {
                                                adapterSales = new AdapterSales(TableSale.this, InventoryApp.sales);
                                                lvSaleList.setAdapter(adapterSales);
                                                showProgress(false);
                                            }
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(TableSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        }
                                    });
                                } else {
                                    adapterSales = new AdapterSales(TableSale.this, InventoryApp.sales);
                                    lvSaleList.setAdapter(adapterSales);
                                    showProgress(false);
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(TableSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        });
                    } else {
                        adapterSales = new AdapterSales(TableSale.this, InventoryApp.sales);
                        lvSaleList.setAdapter(adapterSales);
                        showProgress(false);
                    }
                }
                    @Override
                    public void handleFault (BackendlessFault fault){
                        if (fault.getCode().equals("1009")) {
                            Toast.makeText(TableSale.this, "טרם נשרמו מכירות", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TableSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        showProgress(false);
                    }
            });
        }

        lvSaleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;

                if (findViewById(R.id.table_sales_layout_land) != null) {
                    llSaleDetails.setVisibility(View.VISIBLE);
                    DecimalFormat nf = new DecimalFormat("#,###,###,###.##");
                    if (exports) {
                        Calendar saleDate = Calendar.getInstance();
                        saleDate.setTime(InventoryApp.exports.get(position).getSaleDate());
                        @SuppressLint("DefaultLocale") String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
                        @SuppressLint("DefaultLocale") String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH) + 1);
                        @SuppressLint("DefaultLocale") String buyYear = String.format("%02d", saleDate.get(Calendar.YEAR));

                        Calendar payDate = Calendar.getInstance();
                        payDate.setTime(InventoryApp.exports.get(position).getPayDate());
                        @SuppressLint("DefaultLocale") String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
                        @SuppressLint("DefaultLocale") String payMonth = String.format("%02d", payDate.get(Calendar.MONTH) + 1);
                        @SuppressLint("DefaultLocale") String payYear = String.format("%02d", payDate.get(Calendar.YEAR));

                        tvSaleDetailsBuyDate.setText("תאריך קניה: " + buyDays + "/" + buyMonth + "/" + buyYear);
                        tvSaleDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth + "/" + payYear);

                        tvSaleDetailsClientName.setText(InventoryApp.exports.get(position).getClientName());
                        tvSaleDetailsID.setText("מספר חשבונית:  " + InventoryApp.exports.get(position).getId());
                        tvSaleDetailsPrice.setText("מחיר ממוצע:  " + nf.format(InventoryApp.exports.get(position).getPrice()) + "$");
                        tvSaleDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.exports.get(position).getWeight()));
                        tvSaleDetailsDays.setText("מספר ימים:  " + nf.format(InventoryApp.exports.get(position).getDays()));
                        tvSaleDetailsSum.setText("סכום עסקה:  " + nf.format(InventoryApp.exports.get(position).getSaleSum()) + "$");
                    } else {
                        Calendar saleDate = Calendar.getInstance();
                        saleDate.setTime(InventoryApp.sales.get(position).getSaleDate());
                        @SuppressLint("DefaultLocale") String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
                        @SuppressLint("DefaultLocale") String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH) + 1);
                        @SuppressLint("DefaultLocale") String buyYear = String.format("%02d", saleDate.get(Calendar.YEAR));

                        Calendar payDate = Calendar.getInstance();
                        payDate.setTime(InventoryApp.sales.get(position).getPayDate());
                        @SuppressLint("DefaultLocale") String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
                        @SuppressLint("DefaultLocale") String payMonth = String.format("%02d", payDate.get(Calendar.MONTH) + 1);
                        @SuppressLint("DefaultLocale") String payYear = String.format("%02d", payDate.get(Calendar.YEAR));

                        tvSaleDetailsBuyDate.setText("תאריך קניה: " + buyDays + "/" + buyMonth + "/" + buyYear);
                        tvSaleDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth + "/" + payYear);

                        tvSaleDetailsClientName.setText(InventoryApp.sales.get(position).getClientName());
                        tvSaleDetailsID.setText("מספר חשבונית:  " + InventoryApp.sales.get(position).getId());
                        tvSaleDetailsPrice.setText("מחיר ממוצע:  " + nf.format(InventoryApp.sales.get(position).getPrice()) + "$");
                        tvSaleDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.sales.get(position).getWeight()));
                        tvSaleDetailsDays.setText("מספר ימים:  " + nf.format(InventoryApp.sales.get(position).getDays()));
                        tvSaleDetailsSum.setText("סכום עסקה:  " + nf.format(InventoryApp.sales.get(position).getSaleSum()) + "$");
                    }

                    // In Port
                } else {
                    if (exports) {
                        adapterExports.setSelectedPosition(position);
                        adapterExports.notifyDataSetChanged();
                    } else {
                        adapterSales.setSelectedPosition(position);
                        adapterSales.notifyDataSetChanged();
                    }
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
                if (exports) {
                    newSale.putExtra("export", true);
                } else {
                    newSale.putExtra("export", false);
                }
                startActivityForResult(newSale, 1);
                break;

            case R.id.editIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editSale = new Intent(TableSale.this, EditSale.class);
                    editSale.putExtra("index", selectedItem);
                    if (exports) {
                        editSale.putExtra("export", true);
                    } else {
                        editSale.putExtra("export", false);
                    }
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

                            if (exports) {

                                Backendless.Persistence.of(Export.class).remove(InventoryApp.exports.get(selectedItem), new AsyncCallback<Long>() {
                                    @Override
                                    public void handleResponse(Long response) {
                                        InventoryApp.exports.remove(selectedItem);
                                        Toast.makeText(TableSale.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                        adapterExports.notifyDataSetChanged();
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(TableSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Backendless.Persistence.of(Sale.class).remove(InventoryApp.sales.get(selectedItem), new AsyncCallback<Long>() {
                                    @Override
                                    public void handleResponse(Long response) {
                                        InventoryApp.sales.remove(selectedItem);
                                        Toast.makeText(TableSale.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                        adapterSales.notifyDataSetChanged();
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(TableSale.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
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
            if (exports) {
                adapterExports.notifyDataSetChanged();
            } else {
                adapterSales.notifyDataSetChanged();
            }
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
