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
    TextView tvSupplierDetailsName, tvSupplierDetailsSaleSum, tvSupplierDetailsWeightSum, tvSupplierDetailsPrice, tvSupplierDetailsSaleAVG, tvSupplierDetailsWeightAVG;
    ListView lvSupplierList;
    AdapterSupplier adapter;

    double saleSum = 0;
    double weightSum = 0;
    double price;

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

        tvSupplierDetailsName = findViewById(R.id.tvSupplierDetailsName);
        tvSupplierDetailsSaleSum = findViewById(R.id.tvSupplierDetailsSaleSum);
        tvSupplierDetailsWeightSum = findViewById(R.id.tvSupplierDetailsWeightSum);
        tvSupplierDetailsPrice = findViewById(R.id.tvSupplierDetailsPrice);
        tvSupplierDetailsSaleAVG = findViewById(R.id.tvSupplierDetailsSaleAVG);
        tvSupplierDetailsWeightAVG = findViewById(R.id.tvSupplierDetailsWeightAVG);

        llSupplierDetails = findViewById(R.id.llSupplierDetails);

        llSupplierDetails.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("ספקים");
        actionBar.setDisplayHomeAsUpEnabled(true);

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("objectId");
        queryBuilder.setPageSize(100);

        showProgress(true);
        tvLoad.setText("טוען נתונים, אנא המתן...");

        Backendless.Data.of(Supplier.class).find(queryBuilder, new AsyncCallback<List<Supplier>>() {
            @Override
            public void handleResponse(List<Supplier> response) {
                InventoryApp.suppliers = response;
                adapter = new AdapterSupplier(TableSupplier.this, InventoryApp.suppliers);
                lvSupplierList.setAdapter(adapter);
                showProgress(false);
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
                llSupplierDetails.setVisibility(View.VISIBLE);
                if (InventoryApp.buys != null) {
                    for (Buy buy : InventoryApp.buys) {
                        if (buy.getSupplierName().equals(InventoryApp.suppliers.get(selectedItem).getName())) {
                            saleSum += buy.getSum();
                            weightSum += buy.getWeight();
                        }
                    }
                }
                price = saleSum/weightSum;

                DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

                tvSupplierDetailsName.setText(InventoryApp.suppliers.get(selectedItem).getName());
                tvSupplierDetailsSaleSum.setText("סכום קניות:  " + nf.format(saleSum) + "$");
                tvSupplierDetailsWeightSum.setText("סכום משקל: " + nf.format(weightSum));
                tvSupplierDetailsPrice.setText("מחיר ממוצע לקראט: " + nf.format(price) + "$");
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
