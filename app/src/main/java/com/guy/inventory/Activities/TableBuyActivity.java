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
import com.guy.inventory.Adapters.BuysAdapter;
import com.guy.inventory.EndlessScrollListener;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Buy;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBuyActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvBuyDetailsSupplier, tvBuyDetailsBuyDate, tvBuyDetailsPayDate, tvBuyDetailsID,
            tvBuyDetailsPrice, tvBuyDetailsWeight, tvBuyDetailsDays, tvBuyDetailsSum, tvBuyDetailsDoneWeight, tvBuyDetailsWage, tvBuyDetailsWorkDe;

    ListView lvBuyList;
    LinearLayout llBuyDetails;
    BuysAdapter buysAdapter;

    int selectedItem = -1;
    String order = "buyDate DESC";
    final int PAGE_SIZE = 50;

    final String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
    final DataQueryBuilder buyBuilder = DataQueryBuilder.create();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buys_table);

        lvBuyList = findViewById(R.id.lvBuyList);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        llBuyDetails = findViewById(R.id.llBuyDetails);

        tvBuyDetailsSupplier = findViewById(R.id.tvBuyDetailsSupplier);
        tvBuyDetailsBuyDate = findViewById(R.id.tvBuyDetailsBuyDate);
        tvBuyDetailsPayDate = findViewById(R.id.tvBuyDetailsPayDate);
        tvBuyDetailsID = findViewById(R.id.tvBuyDetailsID);
        tvBuyDetailsPrice = findViewById(R.id.tvBuyDetailsPrice);
        tvBuyDetailsWeight = findViewById(R.id.tvBuyDetailsWeight);
        tvBuyDetailsDays = findViewById(R.id.tvBuyDetailsDays);
        tvBuyDetailsSum = findViewById(R.id.tvBuyDetailsSum);
        tvBuyDetailsDoneWeight = findViewById(R.id.tvBuyDetailsDoneWeight);
        tvBuyDetailsWage = findViewById(R.id.tvBuyDetailsWage);
        tvBuyDetailsWorkDe = findViewById(R.id.tvBuyDetailsWorkDe);

        // In Land
        if (findViewById(R.id.table_buys_layout_land) != null) {
            llBuyDetails.setVisibility(View.VISIBLE);
        }

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("קניות");
        actionBar.setDisplayHomeAsUpEnabled(true);

        getBuys();

        lvBuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;

                if (findViewById(R.id.table_buys_layout_land) != null) {

                    llBuyDetails.setVisibility(View.VISIBLE);

                    final DecimalFormat nf = new DecimalFormat("#,###,###,###.##");
                    Calendar buyDate = Calendar.getInstance();
                    buyDate.setTime(InventoryApp.buys.get(selectedItem).getBuyDate());
                    @SuppressLint("DefaultLocale") String buyDays = String.format("%02d", buyDate.get(Calendar.DAY_OF_MONTH));
                    @SuppressLint("DefaultLocale") String buyMonth = String.format("%02d", buyDate.get(Calendar.MONTH) + 1);
                    @SuppressLint("DefaultLocale") String buyYear = String.format("%02d", buyDate.get(Calendar.YEAR));

                    Calendar payDate = Calendar.getInstance();
                    payDate.setTime(InventoryApp.buys.get(selectedItem).getPayDate());
                    @SuppressLint("DefaultLocale") String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
                    @SuppressLint("DefaultLocale") String payMonth = String.format("%02d", payDate.get(Calendar.MONTH) + 1);
                    @SuppressLint("DefaultLocale") String payYear = String.format("%02d", payDate.get(Calendar.YEAR));

                    tvBuyDetailsBuyDate.setText("תאריך קניה: " + buyDays + "/" + buyMonth+ "/" + buyYear);
                    tvBuyDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth+ "/" + payYear);

                    tvBuyDetailsSupplier.setText(InventoryApp.buys.get(selectedItem).getSupplierName());
                    tvBuyDetailsID.setText("מספר אסמכתא:  " + InventoryApp.buys.get(selectedItem).getId());
                    tvBuyDetailsPrice.setText("מחיר לקראט:  " + nf.format(InventoryApp.buys.get(selectedItem).getPrice()) + "$");
                    tvBuyDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.buys.get(selectedItem).getWeight()) + " קראט ");
                    tvBuyDetailsDays.setText("מספר ימים:  " + InventoryApp.buys.get(selectedItem).getDays());
                    tvBuyDetailsSum.setText("סכום עסקה:  " + nf.format(InventoryApp.buys.get(selectedItem).getSum()) + "$");
                    tvBuyDetailsDoneWeight.setText("משקל גמור:  " + nf.format(InventoryApp.buys.get(selectedItem).getDoneWeight()) + " קראט ");
                    tvBuyDetailsWage.setText("שכר עבודה:  " + nf.format(InventoryApp.buys.get(selectedItem).getWage()) + "$" + " , " +
                            nf.format(InventoryApp.buys.get(selectedItem).getWage() / InventoryApp.buys.get(selectedItem).getPrice() * 100) + "%" + " , " +
                            nf.format(InventoryApp.buys.get(selectedItem).getWage() * InventoryApp.buys.get(selectedItem).getWeight()) + "$");
                    tvBuyDetailsWorkDe.setText("אחוז ליטוש:  " + nf.format(InventoryApp.buys.get(selectedItem).getWorkDepreciation() * 100) + "%");

                } else {
                    buysAdapter.setSelectedPosition(position);
                    buysAdapter.notifyDataSetChanged();
                }
            }
        });

        lvBuyList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.table_buy_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newIcon:
                Intent intent = new Intent(TableBuyActivity.this, NewBuyActivity.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.editIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editBuy = new Intent(TableBuyActivity.this, EditBuyActivity.class);
                    editBuy.putExtra("index", selectedItem);
                    startActivityForResult(editBuy, 1);
                }
                break;

            case R.id.deleteIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט למחיקה", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder deleteAlert = new AlertDialog.Builder(TableBuyActivity.this);
                    deleteAlert.setTitle("התראת מחיקה");
                    deleteAlert.setMessage("האם אתה בטוח שברצונך למחוק את הקניה המסומנת?");
                    deleteAlert.setNegativeButton(android.R.string.no, null);
                    deleteAlert.setIcon(android.R.drawable.ic_dialog_alert);
                    deleteAlert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(true);
                            tvLoad.setText("מוחק את הנתונים אנא המתן...");
                            Backendless.Persistence.of(Buy.class).remove(InventoryApp.buys.get(selectedItem), new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long response) {
                                    InventoryApp.buys.remove(selectedItem);
                                    Toast.makeText(TableBuyActivity.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                    buysAdapter.notifyDataSetChanged();
                                    showProgress(false);
                                    tvLoad.setText("טוען...");
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(TableBuyActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    deleteAlert.show();
                }
                break;

            case R.id.doneIcon:
                if (selectedItem == -1 || InventoryApp.buys.get(selectedItem).isDone()) {
                    Toast.makeText(this, "יש לחבור חבילה לא גמורה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent doneIntent = new Intent(TableBuyActivity.this, DoneActivity.class);
                    doneIntent.putExtra("index", selectedItem);
                    startActivityForResult(doneIntent, 1);
                }
                break;

            case R.id.dateOrderIcon:
                order = "buyDate DESC";
                getBuys();
                break;

            case R.id.nameOrderIcon:
                order = "supplierName";
                getBuys();
                break;

            case R.id.priceOrderIcon:
                order = "price DESC";
                getBuys();
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
                buysAdapter.notifyDataSetChanged();
                getBuys();
            }
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void getBuys() {
        buyBuilder.setWhereClause(whereClause);
        buyBuilder.setSortBy(order);
        buyBuilder.setPageSize(PAGE_SIZE);
        buyBuilder.addRelated("Supplier");

        showProgress(true);
        Backendless.Data.of(Buy.class).find(buyBuilder, new AsyncCallback<List<Buy>>() {
            @Override
            public void handleResponse(List<Buy> response) {
                InventoryApp.buys = response;
                buysAdapter = new BuysAdapter(TableBuyActivity.this, InventoryApp.buys);
                lvBuyList.setAdapter(buysAdapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableBuyActivity.this, "טרם נשרמו קניות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableBuyActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });
    }

    public void loadNextDataFromApi(int offset) {
            DataQueryBuilder buyBuilderLoad = DataQueryBuilder.create();
            buyBuilderLoad.setOffset(offset);
            buyBuilderLoad.setSortBy(order);
            buyBuilderLoad.setWhereClause(whereClause);
            buyBuilderLoad.setPageSize(PAGE_SIZE);

            showProgress(true);
            Backendless.Data.of(Buy.class).find(buyBuilderLoad, new AsyncCallback<List<Buy>>() {
                @Override
                public void handleResponse(List<Buy> response) {
                    InventoryApp.buys.addAll(response);
                    buysAdapter.notifyDataSetChanged();
                    showProgress(false);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    showProgress(false);
                    Toast.makeText(TableBuyActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}
