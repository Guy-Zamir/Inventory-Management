package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaleActivity extends AppCompatActivity {

    EditText etSaleDate, etSaleCompany, etSaleID, etSaleSum;
    Button btnSaleSubmit;
    String company, id, date;
    double saleSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleactivity);

        etSaleDate = findViewById(R.id.etSaleDate);
        etSaleCompany = findViewById(R.id.etSaleCompany);
        etSaleID = findViewById(R.id.etSaleID);
        etSaleSum = findViewById(R.id.etSaleSum);
        btnSaleSubmit = findViewById(R.id.btnSaleSubmit);

        btnSaleSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = true;
                boolean theDate = true;

                if (etSaleDate.getText().toString().isEmpty()) {
                    ok = false;
                } else if (etSaleDate.getText().toString().length() != 6) {
                    theDate = false;
                } else {
                    date = etSaleDate.getText().toString().substring(0, 2);
                    date += ".";
                    date += etSaleDate.getText().toString().substring(2, 4);
                    date += ".";
                    date += etSaleDate.getText().toString().substring(4, 6);
                }

                if (etSaleCompany.getText().toString().isEmpty()) {
                    ok = false;
                } else {
                    company = etSaleCompany.getText().toString();
                }

                if (etSaleID.getText().toString().isEmpty()) {
                    ok = false;
                } else {
                    id = etSaleID.getText().toString();
                }

                if (etSaleSum.getText().toString().isEmpty()) {
                    ok = false;
                } else {
                    saleSum = Double.parseDouble(etSaleSum.getText().toString());
                }

                if (!theDate) {
                    Toast.makeText(SaleActivity.this, "יש למלא את התאריך ב6 ספרות", Toast.LENGTH_SHORT).show();
                } else if (ok) {
                    Intent intent = new Intent();
                    intent.putExtra("date", date);
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
