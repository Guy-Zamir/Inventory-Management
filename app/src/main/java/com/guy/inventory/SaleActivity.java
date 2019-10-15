package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class SaleActivity extends AppCompatActivity {

    DatePicker dpSaleDate;
    EditText etSaleCompany, etSaleID, etSaleSum;
    Button btnSaleSubmit;
    String company, id, date;
    int day, month, year;
    double saleSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleactivity);

        dpSaleDate = findViewById(R.id.dpSaleDate);
        etSaleCompany = findViewById(R.id.etSaleCompany);
        etSaleID = findViewById(R.id.etSaleID);
        etSaleSum = findViewById(R.id.etSaleSum);
        btnSaleSubmit = findViewById(R.id.btnSaleSubmit);

        btnSaleSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean toast = false;

                day = dpSaleDate.getDayOfMonth();
                month = dpSaleDate.getMonth();
                year = dpSaleDate.getYear();

                if (etSaleCompany.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    company = etSaleCompany.getText().toString();
                }

                if (etSaleID.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    id = etSaleID.getText().toString();
                }

                if (etSaleSum.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    saleSum = Double.parseDouble(etSaleSum.getText().toString());
                }

                if (!toast) {
                    Intent intent = new Intent();
                    intent.putExtra("day", day);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("company", company);
                    intent.putExtra("id", id);
                    intent.putExtra("saleSum", saleSum);
                    setResult(RESULT_OK, intent);
                    finishActivity(MainActivity.buy);
                    finish();
                } else {
                    Toast.makeText(SaleActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
