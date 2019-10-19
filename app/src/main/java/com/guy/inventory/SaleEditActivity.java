package com.guy.inventory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import java.text.DecimalFormat;

public class SaleEditActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    ImageView ivSaleDelete, ivSaleEdit;
    TextView tvSaleSum, tvSaleCompany;

    DatePicker dpSaleEditDate;
    EditText etSaleEditCompany, etSaleEditID, etSaleEditWeight, etSaleEditSum;
    TextView tvSaleEditCompany, tvSaleEditID, tvSaleEditWeight, tvSaleEditSum;
    Button btnSaleEditSubmit;
    int index;

    boolean edit = false;

    String company, id, date;
    double weight, saleSum;
    boolean toast;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_edit);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvSaleCompany = findViewById(R.id.tvSaleCompany);
        tvSaleSum = findViewById(R.id.tvSaleSum);

        ivSaleDelete = findViewById(R.id.ivSaleDelete);
        ivSaleEdit = findViewById(R.id.ivSaleEdit);

        dpSaleEditDate = findViewById(R.id.dpSaleEditDate);
        etSaleEditCompany = findViewById(R.id.etSaleEditCompany);
        etSaleEditID = findViewById(R.id.etSaleEditID);
        etSaleEditWeight = findViewById(R.id.etSaleEditWeight);
        etSaleEditSum = findViewById(R.id.etSaleEditSum);
        btnSaleEditSubmit = findViewById(R.id.btnSaleEditSubmit);

        tvSaleEditCompany = findViewById(R.id.tvSaleEditCompany);
        tvSaleEditID = findViewById(R.id.tvSaleEditID);
        tvSaleEditWeight = findViewById(R.id.tvSaleEditWeight);
        tvSaleEditSum = findViewById(R.id.tvSaleEditSum);

        dpSaleEditDate.setVisibility(View.GONE);
        etSaleEditCompany.setVisibility(View.GONE);
        etSaleEditID.setVisibility(View.GONE);
        etSaleEditWeight.setVisibility(View.GONE);
        etSaleEditSum.setVisibility(View.GONE);
        btnSaleEditSubmit.setVisibility(View.GONE);
        tvSaleEditCompany.setVisibility(View.GONE);
        tvSaleEditID.setVisibility(View.GONE);
        tvSaleEditWeight.setVisibility(View.GONE);
        tvSaleEditSum.setVisibility(View.GONE);


        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
        index = getIntent().getIntExtra("index", 0);

        tvSaleCompany.setText(InventoryApp.sales.get(index).getCompany());
        tvSaleSum.setText(nf.format(InventoryApp.sales.get(index).getSaleSum()) + "$");

        etSaleEditCompany.setText(InventoryApp.sales.get(index).getCompany());
        etSaleEditID.setText(InventoryApp.sales.get(index).getId());
        etSaleEditWeight.setText(String.valueOf(InventoryApp.sales.get(index).getWeight()));
        etSaleEditSum.setText(String.valueOf(InventoryApp.sales.get(index).getSaleSum()));

        int day = Integer.valueOf(InventoryApp.sales.get(index).getSaleDate().substring(0,2));
        int month = Integer.valueOf(InventoryApp.sales.get(index).getSaleDate().substring(3,5));
        int year = Integer.valueOf(InventoryApp.sales.get(index).getSaleDate().substring(6,10));
        dpSaleEditDate.updateDate(year, month, day);

        ivSaleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit = !edit;
                if (edit) {
                    dpSaleEditDate.setVisibility(View.VISIBLE);
                    etSaleEditCompany.setVisibility(View.VISIBLE);
                    etSaleEditID.setVisibility(View.VISIBLE);
                    etSaleEditWeight.setVisibility(View.VISIBLE);
                    etSaleEditSum.setVisibility(View.VISIBLE);
                    btnSaleEditSubmit.setVisibility(View.VISIBLE);
                    tvSaleEditCompany.setVisibility(View.VISIBLE);
                    tvSaleEditID.setVisibility(View.VISIBLE);
                    tvSaleEditWeight.setVisibility(View.VISIBLE);
                    tvSaleEditSum.setVisibility(View.VISIBLE);
                } else {
                    dpSaleEditDate.setVisibility(View.GONE);
                    etSaleEditCompany.setVisibility(View.GONE);
                    etSaleEditID.setVisibility(View.GONE);
                    etSaleEditWeight.setVisibility(View.GONE);
                    etSaleEditSum.setVisibility(View.GONE);
                    btnSaleEditSubmit.setVisibility(View.GONE);
                    tvSaleEditCompany.setVisibility(View.GONE);
                    tvSaleEditID.setVisibility(View.GONE);
                    tvSaleEditWeight.setVisibility(View.GONE);
                    tvSaleEditSum.setVisibility(View.GONE);
                }
            }
        });

        ivSaleDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SaleEditActivity.this);
                alert.setTitle("התראת מחיקה");
                alert.setMessage("האם אתה בטוח שברצונך למחוק את הנתונים המסומנים?");
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("מוחק את הנתונים אנא המתן...");
                        Backendless.Persistence.of(Sale.class).remove(InventoryApp.sales.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                InventoryApp.sales.remove(index);
                                showProgress(false);
                                Toast.makeText(SaleEditActivity.this, "נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                SaleEditActivity.this.finish();
                                }
                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(SaleEditActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    alert.show();
                }
        });

        btnSaleEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = dpSaleEditDate.getDayOfMonth();
                int month = dpSaleEditDate.getMonth();
                int year = dpSaleEditDate.getYear();
                @SuppressLint("DefaultLocale") String dayText = String.format("%02d", day);
                @SuppressLint("DefaultLocale") String monthText = String.format("%02d", (month + 1));
                @SuppressLint("DefaultLocale") String yearText = String.format("%02d", year);
                date = dayText + monthText + yearText;

                if (etSaleEditCompany.getText().toString().isEmpty() || etSaleEditID.getText().toString().isEmpty() ||
                        etSaleEditWeight.getText().toString().isEmpty()|| etSaleEditSum.getText().toString().isEmpty()) {
                    toast = false;
                } else {
                    company = etSaleEditCompany.getText().toString();
                    id = etSaleEditID.getText().toString();
                    weight = Double.parseDouble(etSaleEditWeight.getText().toString());
                    saleSum = Double.parseDouble(etSaleEditSum.getText().toString());
                }

                if (toast) {
                    Toast.makeText(SaleEditActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SaleEditActivity.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            InventoryApp.sales.get(index).setSaleDate(getDateFromDatePicker(dpSaleEditDate));
                            InventoryApp.sales.get(index).setCompany(company);
                            InventoryApp.sales.get(index).setId(id);
                            InventoryApp.sales.get(index).setWeight(weight);
                            InventoryApp.sales.get(index).setSaleSum(saleSum);
                            Backendless.Persistence.save(InventoryApp.sales.get(index), new AsyncCallback<Sale>() {
                                @Override
                                public void handleResponse(Sale response) {
                                    Toast.makeText(SaleEditActivity.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finishActivity(1);
                                    SaleEditActivity.this.finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(SaleEditActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                        alert.show();
                }
            }
        });
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private String getDateFromDatePicker(DatePicker datePicker){
        @SuppressLint("DefaultLocale") String day = String.format("%02d", datePicker.getDayOfMonth());
        @SuppressLint("DefaultLocale") String month = String.format("%02d", (datePicker.getMonth()+1));
        @SuppressLint("DefaultLocale") String year = String.format("%02d", datePicker.getYear());
        return day+"/"+month+"/"+year;
    }
}