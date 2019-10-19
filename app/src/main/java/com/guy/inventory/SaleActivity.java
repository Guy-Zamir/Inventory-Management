package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class SaleActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private DatePicker dpSaleDate;
    private EditText etSaleCompany, etSaleID, etSaleSum, etSaleWeight, etSaleDays;
    private Button btnSaleSubmit;

    private String company, id;
    private double saleSum, weight;
    private int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleactivity);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        dpSaleDate = findViewById(R.id.dpSaleDate);
        etSaleCompany = findViewById(R.id.etSaleCompany);
        etSaleID = findViewById(R.id.etSaleID);
        etSaleSum = findViewById(R.id.etSaleSum);
        btnSaleSubmit = findViewById(R.id.btnSaleSubmit);
        etSaleWeight = findViewById(R.id.etSaleWeight);
        etSaleDays = findViewById(R.id.etSaleDays);

        btnSaleSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean toast = false;

                if (etSaleCompany.getText().toString().isEmpty() || etSaleID.getText().toString().isEmpty() ||
                        etSaleWeight.getText().toString().isEmpty() || etSaleSum.getText().toString().isEmpty()||
                etSaleDays.getText().toString().isEmpty()) {

                    toast = true;
                } else {
                    company = etSaleCompany.getText().toString();
                    id = etSaleID.getText().toString();
                    weight = Double.parseDouble(etSaleWeight.getText().toString());
                    saleSum = Double.parseDouble(etSaleSum.getText().toString());
                    days = Integer.valueOf(etSaleDays.getText().toString().trim());
                }

                if (toast) {
                    Toast.makeText(SaleActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {
                    Sale sale = new Sale();
                    sale.setSaleDate(getDateFromDatePicker(dpSaleDate));
                    sale.setCompany(company);
                    sale.setId(id);
                    sale.setSaleSum(saleSum);
                    sale.setWeight(weight);
                    sale.setDays(days);
                    sale.setPrice((saleSum/weight));
                    sale.setUserEmail(InventoryApp.user.getEmail());
                    showProgress(true);
                    Backendless.Persistence.save(sale, new AsyncCallback<Sale>() {
                        @Override
                        public void handleResponse(Sale response) {
                            Toast.makeText(SaleActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                            SaleActivity.this.finish();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            showProgress(false);
                            Toast.makeText(SaleActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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
