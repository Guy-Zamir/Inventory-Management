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
import java.util.Calendar;
import java.util.List;

public class TableBuy extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvBuyDetailsSupplier, tvBuyDetailsBuyDate, tvBuyDetailsPayDate, tvBuyDetailsID,
            tvBuyDetailsPrice, tvBuyDetailsWeight, tvBuyDetailsDays, tvBuyDetailsSum, tvBuyDetailsDoneWeight, tvBuyDetailsWage, tvBuyDetailsWorkDe;

    ListView lvBuyList;
    LinearLayout llBuyDetails;
    AdapterBuys adapter;

    int selectedItem = -1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buys_table);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        llBuyDetails.setVisibility(View.GONE);

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
                adapter = new AdapterBuys(TableBuy.this, InventoryApp.buys);
                lvBuyList.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (fault.getCode().equals("1009")) {
                    Toast.makeText(TableBuy.this, "טרם נשרמו קניות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableBuy.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
                showProgress(false);
            }
        });

        lvBuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selectedItem = position;
                llBuyDetails.setVisibility(View.VISIBLE);

                final DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
                Calendar saleDate = Calendar.getInstance();
                saleDate.setTime(InventoryApp.buys.get(selectedItem).getBuyDate());
                @SuppressLint("DefaultLocale") String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
                @SuppressLint("DefaultLocale") String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH)+1);

                Calendar payDate = Calendar.getInstance();
                payDate.setTime(InventoryApp.buys.get(selectedItem).getPayDate());
                @SuppressLint("DefaultLocale") String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
                @SuppressLint("DefaultLocale") String payMonth = String.format("%02d", payDate.get(Calendar.MONTH)+1);

                tvBuyDetailsBuyDate.setText("תאריך קניה: " + buyDays + "/" + buyMonth);
                tvBuyDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth);

                tvBuyDetailsSupplier.setText(InventoryApp.buys.get(selectedItem).getSupplierName());
                tvBuyDetailsID.setText("מספר אסמכתא:  " + InventoryApp.buys.get(selectedItem).getId());
                tvBuyDetailsPrice.setText("מחיר לקראט:  " + nf.format(InventoryApp.buys.get(selectedItem).getPrice()) + "$");
                tvBuyDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.buys.get(selectedItem).getWeight()) + " קראט ");
                tvBuyDetailsDays.setText("מספר ימים:  " + InventoryApp.buys.get(selectedItem).getDays());
                tvBuyDetailsSum.setText("סכום עסקה:  " + nf.format(InventoryApp.buys.get(selectedItem).getSum()) + "$");
                tvBuyDetailsDoneWeight.setText("משקל גמור:  " + nf.format(InventoryApp.buys.get(selectedItem).getDoneWeight()) + " קראט ");
                tvBuyDetailsWage.setText("שכר עבודה:  " + nf.format(InventoryApp.buys.get(selectedItem).getWage()) + "$" + " , " +
                        nf.format(InventoryApp.buys.get(selectedItem).getWage()/InventoryApp.buys.get(selectedItem).getPrice()*100) + "%" + " , " +
                        nf.format(InventoryApp.buys.get(selectedItem).getWage()*InventoryApp.buys.get(selectedItem).getWeight()) + "$");
                tvBuyDetailsWorkDe.setText("אחוז ליטוש:  " + nf.format(InventoryApp.buys.get(selectedItem).getWorkDepreciation()*100) + "%");
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
                Intent intent = new Intent(TableBuy.this, NewBuy.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.editIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent editBuy = new Intent(TableBuy.this, EditBuy.class);
                    editBuy.putExtra("index", selectedItem);
                    startActivityForResult(editBuy, 1);
                    break;
                }

            case R.id.deleteIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור פריט למחיקה", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(TableBuy.this);
                    alert.setTitle("התראת מחיקה");
                    alert.setMessage("האם אתה בטוח שברצונך למחוק את המכירה המסומנת?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showProgress(true);
                            tvLoad.setText("מוחק את הנתונים אנא המתן...");
                            Backendless.Persistence.of(Buy.class).remove(InventoryApp.buys.get(selectedItem), new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long response) {
                                    InventoryApp.buys.remove(selectedItem);
                                    Toast.makeText(TableBuy.this, "עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    showProgress(false);
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(TableBuy.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alert.show();
                }
                break;

            case R.id.doneIcon:
                if (selectedItem == -1) {
                    Toast.makeText(this, "יש לחבור חבילה לא גמורה", Toast.LENGTH_SHORT).show();
                } else {
                    if (!InventoryApp.buys.get(selectedItem).isDone()) {
                        AlertDialog.Builder alert2 = new AlertDialog.Builder(TableBuy.this);
                        alert2.setTitle("שינוי נתונים");
                        alert2.setMessage("האם אתה בטוח שברצונך לשנות את החבילה לגמורה?");
                        alert2.setNegativeButton(android.R.string.no, null);
                        alert2.setIcon(android.R.drawable.ic_dialog_alert);

                        alert2.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                InventoryApp.buys.get(selectedItem).setDone(true);
                                showProgress(true);
                                Backendless.Persistence.save(InventoryApp.buys.get(selectedItem), new AsyncCallback<Buy>() {
                                    @Override
                                    public void handleResponse(Buy response) {
                                        showProgress(false);
                                        Intent intent = new Intent(TableBuy.this, EditBuyDone.class);
                                        intent.putExtra("index", selectedItem);
                                        startActivityForResult(intent, 1);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        showProgress(false);
                                        Toast.makeText(TableBuy.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                        alert2.show();
                    } else {
                        Toast.makeText(this, "יש לחבור חבילה לא גמורה", Toast.LENGTH_SHORT).show();
                    }
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
