package com.guy.inventory.Activities;

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
import com.guy.inventory.Adapters.SalesAdapter;
import com.guy.inventory.EndlessScrollListener;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.BrokerSort;
import com.guy.inventory.Tables.Sale;
import com.guy.inventory.Tables.Sort;
import com.guy.inventory.Tables.SortInfo;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableSaleActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvSaleDetailsClientName, tvSaleDetailsBuyDate, tvSaleDetailsPayDate, tvSaleDetailsID,
            tvSaleDetailsPrice, tvSaleDetailsWeight, tvSaleDetailsDays, tvSaleDetailsSum;

    ListView lvSaleList;
    LinearLayout llSaleDetails;

    SalesAdapter salesAdapter;

    String aSale = "sale";
    String anExport = "export";

    final String saleClause = "kind = '" + aSale + "'";
    final String exportClause = "kind = '" + anExport + "'";

    String order = "saleDate DESC";
    int selectedItem = -1;
    boolean exports;

    final int PAGE_SIZE = 50;

    final DataQueryBuilder saleBuilder = DataQueryBuilder.create();
    final String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_table);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


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

        // If the activity is an export or not
        exports = getIntent().getBooleanExtra("exports", false);

        // Setting the action bar title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle((exports) ? "יצואים" : "מכירות");
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSales();

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

                    // Setting the values for sales
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

                // Setting the values from the clientAdapter in port mode
                } else {
                    salesAdapter.setSelectedPosition(position);
                    salesAdapter.notifyDataSetChanged();
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
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_LONG).show();

                } else if (InventoryApp.sales.get(selectedItem).isSorted()) {
                    Toast.makeText(this, "אין ניתן לערוך מכירה ממוינת", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(this, "יש לחבור פריט למחיקה", Toast.LENGTH_LONG).show();

                } else if (InventoryApp.sales.get(selectedItem).isSorted()) {
                    AlertDialog.Builder deleteAlert = new AlertDialog.Builder(TableSaleActivity.this);
                    deleteAlert.setTitle("התראת מחיקה");
                    deleteAlert.setMessage("האם אתה בטוח שברצונך למחוק את המכירה המסומנת?");
                    deleteAlert.setNegativeButton(android.R.string.no, null);
                    deleteAlert.setIcon(android.R.drawable.ic_dialog_alert);
                    deleteAlert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(true);
                            tvLoad.setText("מוחק את הנתונים אנא המתן...");

                            final DataQueryBuilder sortBuilder = DataQueryBuilder.create();
                            sortBuilder.setWhereClause("saleId = '" + InventoryApp.sales.get(selectedItem).getObjectId() + "'");

                            Backendless.Data.of(Sort.class).find(sortBuilder, new AsyncCallback<List<Sort>>() {
                                @Override
                                public void handleResponse(List<Sort> response) {
                                    // Finding all the sortInfo and deleting them after collecting the data of the weight and sum
                                    double sum = 0;
                                    double weight = 0;
                                    String sortName = "";

                                    for (Sort sort : response) {
                                        sum += sort.getSum();
                                        weight += sort.getWeight();
                                        sortName = sort.getName();
                                    }

                                    final double allSum = sum;
                                    final double allWeight = weight;
                                    final String name = sortName.substring(0, sortName.length()-1);

                                    Backendless.Data.of("Sort").remove("saleId = '" + InventoryApp.sales.get(selectedItem).getObjectId() + "'", new AsyncCallback<Integer>() {
                                        @Override
                                        public void handleResponse(Integer response) {
                                            DataQueryBuilder sortBuilder = DataQueryBuilder.create();
                                            sortBuilder.setWhereClause("name = '" + name + "'");
                                            sortBuilder.setHavingClause("last = true");
                                            Backendless.Data.of(Sort.class).find(sortBuilder, new AsyncCallback<List<Sort>>() {
                                                @Override
                                                public void handleResponse(List<Sort> response) {
                                                    final Sort sort = response.get(0);

                                                    // Adding to the original sort the weight and sum
                                                    sort.setSum(sort.getSum() + allSum);
                                                    sort.setWeight(sort.getWeight() + allWeight);
                                                    sort.setPrice(sort.getSum() / sort.getWeight());
                                                    Backendless.Data.of(Sort.class).save(sort, new AsyncCallback<Sort>() {
                                                        @Override
                                                        public void handleResponse(Sort response) {
                                                            Backendless.Persistence.of(Sale.class).remove(InventoryApp.sales.get(selectedItem), new AsyncCallback<Long>() {
                                                                @Override
                                                                public void handleResponse(Long response) {
                                                                    showProgress(false);
                                                                    Toast.makeText(TableSaleActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                                                                    setResult(RESULT_OK);
                                                                    finishActivity(1);
                                                                    TableSaleActivity.this.finish();
                                                                }

                                                                @Override
                                                                public void handleFault(BackendlessFault fault) {
                                                                    Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    showProgress(false);
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void handleFault(BackendlessFault fault) {
                                                            Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                            showProgress(false);
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                    showProgress(false);
                                                }
                                            });
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        }
                                    });
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    showProgress(false);
                                }
                            });
                        }
                    });
                    deleteAlert.show();

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
                            Backendless.Persistence.of(Sale.class).remove(InventoryApp.sales.get(selectedItem), new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long response) {
                                    InventoryApp.sales.remove(selectedItem);
                                    Toast.makeText(TableSaleActivity.this, "עודכן בהצלחה", Toast.LENGTH_LONG).show();
                                    salesAdapter.notifyDataSetChanged();
                                    showProgress(false);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    alert.show();
                }
                break;

            case R.id.dateOrderIcon:
                order = "saleDate DESC";
                getSales();
                break;

            case R.id.nameOrderIcon:
                order = "clientName";
                getSales();
                break;

            case R.id.sumOrderIcon:
                order = "saleSum DESC";
                getSales();
                break;

            case R.id.sortIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור מכירה", Toast.LENGTH_SHORT).show();

                } else if (InventoryApp.sales.get(selectedItem).isSorted()) {
                    Toast.makeText(this, "יש לחבור מכירה לא ממוינת", Toast.LENGTH_SHORT).show();

                } else {
                    Intent newSaleSort = new Intent(TableSaleActivity.this, NewSaleSortActivity.class);
                    newSaleSort.putExtra("index", selectedItem);
                    startActivityForResult(newSaleSort, 1);
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
            salesAdapter.notifyDataSetChanged();
            getSales();
        }
    }

    private void getSales() {
        saleBuilder.setWhereClause(whereClause);
        saleBuilder.setSortBy(order);
        saleBuilder.setPageSize(PAGE_SIZE);
        saleBuilder.setHavingClause((exports) ? exportClause : saleClause);

        showProgress(true);
        Backendless.Data.of(Sale.class).find(saleBuilder, new AsyncCallback<List<Sale>>() {
            @Override
            public void handleResponse(List<Sale> response) {
                // Up to 50
                InventoryApp.sales = response;

                final DataQueryBuilder saleNameBuilderLoad = DataQueryBuilder.create();
                saleNameBuilderLoad.setSortBy(order);
                saleNameBuilderLoad.setWhereClause(whereClause);
                saleNameBuilderLoad.setPageSize(PAGE_SIZE);
                saleNameBuilderLoad.setHavingClause((exports) ? exportClause : saleClause);
                saleNameBuilderLoad.addRelated("Client");
                Backendless.Data.of("Sale").find(saleNameBuilderLoad, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        for (int i = 0; i < response.size(); i++) {
                            HashMap client = (HashMap) response.get(i).get("Client");
                            InventoryApp.sales.get(i).setClientName((client == null) ? "1" : (String) client.get("name"));
                        }

                            salesAdapter = new SalesAdapter(TableSaleActivity.this, InventoryApp.sales);
                            lvSaleList.setAdapter(salesAdapter);
                            showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public void loadNextDataFromApi(final int offset) {
        final DataQueryBuilder exportBuilderLoad = DataQueryBuilder.create();
        exportBuilderLoad.setOffset(offset);
        exportBuilderLoad.setSortBy(order);
        exportBuilderLoad.setWhereClause(whereClause);
        exportBuilderLoad.setPageSize(PAGE_SIZE);
        exportBuilderLoad.setHavingClause((exports) ? exportClause : saleClause);

        showProgress(true);
        Backendless.Data.of(Sale.class).find(exportBuilderLoad, new AsyncCallback<List<Sale>>() {
            @Override
            public void handleResponse(List<Sale> response) {
                InventoryApp.sales.addAll(response);

                final DataQueryBuilder exportNameBuilderLoad = DataQueryBuilder.create();
                exportNameBuilderLoad.setOffset(offset);
                exportNameBuilderLoad.setSortBy(order);
                exportNameBuilderLoad.setWhereClause(whereClause);
                exportNameBuilderLoad.setPageSize(PAGE_SIZE);
                exportNameBuilderLoad.setHavingClause((exports) ? exportClause : saleClause);
                exportNameBuilderLoad.addRelated("Client");
                Backendless.Data.of("Sale").find(exportNameBuilderLoad, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> response) {
                        for (int i = 0; i < response.size(); i++) {
                            HashMap client = (HashMap) response.get(i).get("Client");
                            InventoryApp.sales.get(i + offset).setClientName((client == null) ? "1" : (String) client.get("name"));
                        }
                        salesAdapter.notifyDataSetChanged();
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(TableSaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }
}
