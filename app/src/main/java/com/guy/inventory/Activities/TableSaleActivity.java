package com.guy.inventory.Activities;

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
import com.guy.inventory.Adapters.ExportsAdapter;
import com.guy.inventory.Adapters.SalesAdapter;
import com.guy.inventory.EndlessScrollListener;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Export;
import com.guy.inventory.Tables.Sale;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class TableSaleActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvSaleDetailsClientName, tvSaleDetailsBuyDate, tvSaleDetailsPayDate, tvSaleDetailsID,
            tvSaleDetailsPrice, tvSaleDetailsWeight, tvSaleDetailsDays, tvSaleDetailsSum;

    ListView lvSaleList;
    LinearLayout llSaleDetails;

    SalesAdapter salesAdapter;
    ExportsAdapter exportsAdapter;

    String order = "saleDate DESC";
    int selectedItem = -1;
    boolean exports;

    final int PAGE_SIZE = 50;

    final DataQueryBuilder exportBuilder = DataQueryBuilder.create();
    final DataQueryBuilder saleBuilder = DataQueryBuilder.create();
    final String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";

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

        // An Export Sale - Setting the action bar title
        if (exports) {
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("יצואים");
            actionBar.setDisplayHomeAsUpEnabled(true);

            getExports();

        // Not an Export Sale - Setting the action bar title
        } else {
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("מכירות");
            actionBar.setDisplayHomeAsUpEnabled(true);

            getSales();
        }

        lvSaleList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });



        lvSaleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;
                // Setting the values in land mode
                if (findViewById(R.id.table_sales_layout_land) != null) {
                    llSaleDetails.setVisibility(View.VISIBLE);

                    DecimalFormat nf = new DecimalFormat("#,###,###,###.##");

                    // Setting the values for exports
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
                        tvSaleDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.exports.get(position).getWeight()) + " קראט ");
                        tvSaleDetailsDays.setText("מספר ימים:  " + nf.format(InventoryApp.exports.get(position).getDays()));
                        tvSaleDetailsSum.setText("סכום עסקה:  " + nf.format(InventoryApp.exports.get(position).getSaleSum()) + "$");

                    // Setting the values for sales
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
                        tvSaleDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.sales.get(position).getWeight()) + " קראט ");
                        tvSaleDetailsDays.setText("מספר ימים:  " + nf.format(InventoryApp.sales.get(position).getDays()));
                        tvSaleDetailsSum.setText("סכום עסקה:  " + nf.format(InventoryApp.sales.get(position).getSaleSum()) + "$");
                    }

                // Setting the values from the clientAdapter in port mode
                } else {
                    if (exports) {
                        exportsAdapter.setSelectedPosition(position);
                        exportsAdapter.notifyDataSetChanged();
                    } else {
                        salesAdapter.setSelectedPosition(position);
                        salesAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_sale_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newIcon:
                Intent newSale = new Intent(TableSaleActivity.this, NewSaleActivity.class);
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
                    Intent editSale = new Intent(TableSaleActivity.this, EditSaleActivity.class);
                    editSale.putExtra("index", selectedItem);
                    if (exports) {
                        editSale.putExtra("export", true);
                    } else {
                        editSale.putExtra("export", false);
                    }
                    startActivityForResult(editSale, 1);
                }
                break;

            case R.id.deleteIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט למחיקה", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(TableSaleActivity.this);
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
                                        Toast.makeText(TableSaleActivity.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                        exportsAdapter.notifyDataSetChanged();
                                        showProgress(false);
                                        tvLoad.setText("טוען...");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Backendless.Persistence.of(Sale.class).remove(InventoryApp.sales.get(selectedItem), new AsyncCallback<Long>() {
                                    @Override
                                    public void handleResponse(Long response) {
                                        InventoryApp.sales.remove(selectedItem);
                                        Toast.makeText(TableSaleActivity.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                        salesAdapter.notifyDataSetChanged();
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    alert.show();
                }
                break;

            case R.id.dateOrderIcon:
                order = "saleDate DESC";
                if (exports) {
                    getExports();
                } else {
                    getSales();
                }
                break;

            case R.id.nameOrderIcon:
                order = "clientName";
                if (exports) {
                    getExports();
                } else {
                    getSales();
                }
                break;

            case R.id.sumOrderIcon:
                order = "saleSum DESC";
                if (exports) {
                    getExports();
                }else {
                    getSales();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (exports) {
                exportsAdapter.notifyDataSetChanged();
                getExports();
            } else {
                salesAdapter.notifyDataSetChanged();
                getSales();
            }
        }
    }

    private void getSales() {
        saleBuilder.setWhereClause(whereClause);
        saleBuilder.setSortBy(order);
        saleBuilder.setPageSize(PAGE_SIZE);
        saleBuilder.setRelated("Client");
        saleBuilder.addRelated("Client");

        showProgress(true);
        Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {

            @Override
            public void handleResponse(List<Sale> response) {
                // Up to 50
                InventoryApp.sales = response;
                salesAdapter = new SalesAdapter(TableSaleActivity.this, InventoryApp.sales);
                lvSaleList.setAdapter(salesAdapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableSaleActivity.this, "טרם נשרמו מכירות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });
    }

    private void getExports() {
        exportBuilder.setWhereClause(whereClause);
        exportBuilder.setSortBy(order);
        exportBuilder.setPageSize(PAGE_SIZE);
        showProgress(true);

        Backendless.Data.of(Export.class).find(exportBuilder, new AsyncCallback<List<Export>>() {
            @Override
            public void handleResponse(List<Export> response) {
                InventoryApp.exports = response;
                exportsAdapter = new ExportsAdapter(TableSaleActivity.this, InventoryApp.exports);
                lvSaleList.setAdapter(exportsAdapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableSaleActivity.this, "טרם נשרמו יצואים", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public void loadNextDataFromApi(int offset) {
        if (exports) {
            DataQueryBuilder exportBuilderLoad = DataQueryBuilder.create();
            exportBuilderLoad.setOffset(offset);
            exportBuilderLoad.setSortBy(order);
            exportBuilderLoad.setWhereClause(whereClause);
            exportBuilderLoad.setPageSize(PAGE_SIZE);

            showProgress(true);
            Backendless.Data.of(Export.class).find(exportBuilderLoad, new AsyncCallback<List<Export>>() {
                @Override
                public void handleResponse(List<Export> response) {
                    InventoryApp.exports.addAll(response);
                    exportsAdapter.notifyDataSetChanged();
                    showProgress(false);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    showProgress(false);
                    Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            DataQueryBuilder saleBuilderLoad = DataQueryBuilder.create();
            saleBuilderLoad.setOffset(offset);
            saleBuilderLoad.setSortBy(order);
            saleBuilderLoad.setWhereClause(whereClause);
            saleBuilderLoad.setPageSize(PAGE_SIZE);

            showProgress(true);
            Backendless.Data.of(Sale.class).find(saleBuilderLoad, new AsyncCallback<List<Sale>>() {
                @Override
                public void handleResponse(List<Sale> response) {
                    InventoryApp.sales.addAll(response);
                    salesAdapter.notifyDataSetChanged();
                    showProgress(false);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    showProgress(false);
                    Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
