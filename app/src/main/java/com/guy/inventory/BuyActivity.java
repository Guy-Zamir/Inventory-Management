package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class BuyActivity extends AppCompatActivity {

    EditText etBuyDate, etBuySupplier, etBuyID, etBuyKaratPrice, etBuyKaratWeight, etBuyDoneWeight, etBuyWage;
    CheckBox cbBuyPolish, cbBuyDone;
    Button btnBuySubmit;
    String supplier, id, date;
    boolean polish, done;
    double price, weight, doneWeight, wage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyactivity);

        etBuyDate = findViewById(R.id.etBuyDate);
        etBuySupplier = findViewById(R.id.etBuySupplier);
        etBuyID = findViewById(R.id.etBuyID);
        etBuyKaratPrice = findViewById(R.id.etBuyKaratPrice);
        etBuyKaratWeight = findViewById(R.id.etBuyKaratWeight);
        etBuyDoneWeight = findViewById(R.id.etBuyDoneWeight);
        etBuyWage = findViewById(R.id.etBuyWage);
        cbBuyPolish = findViewById(R.id.cbBuyPolish);
        cbBuyDone = findViewById(R.id.cbBuyDone);
        btnBuySubmit = findViewById(R.id.btnBuySubmit);

        etBuyDoneWeight.setVisibility(View.GONE);
        etBuyWage.setVisibility(View.GONE);

        cbBuyPolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbBuyPolish.isChecked()) {
                    cbBuyDone.setVisibility(View.GONE);
                    cbBuyDone.setChecked(false);
                } else {
                    cbBuyDone.setVisibility(View.VISIBLE);
                }
            }
        });

        cbBuyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbBuyDone.isChecked()) {
                    etBuyDoneWeight.setVisibility(View.VISIBLE);
                    etBuyWage.setVisibility(View.VISIBLE);
                    cbBuyPolish.setChecked(false);
                } else {
                    etBuyDoneWeight.setVisibility(View.GONE);
                    etBuyWage.setVisibility(View.GONE);
                }
            }
        });

        btnBuySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = true;
                boolean theDate = true;

                if (etBuyDate.getText().toString().isEmpty()) {
                    ok = false;
                } else if (etBuyDate.getText().toString().length() != 6) {
                    theDate = false;
                } else {
                    date = etBuyDate.getText().toString().substring(0,2);
                    date += ".";
                    date += etBuyDate.getText().toString().substring(2,4);
                    date += ".";
                    date += etBuyDate.getText().toString().substring(4,6);
                }

                if (etBuySupplier.getText().toString().isEmpty()) {
                    ok = false;
                } else {
                    supplier = etBuySupplier.getText().toString();
                }

                if (etBuyID.getText().toString().isEmpty()) {
                    ok = false;
                } else {
                    id = etBuyID.getText().toString();
                }

                if (etBuyKaratPrice.getText().toString().isEmpty()) {
                    ok = false;
                } else {
                    price = Double.parseDouble(etBuyKaratPrice.getText().toString());
                }

                if (etBuyKaratWeight.getText().toString().isEmpty()) {
                    ok = false;
                } else {
                    weight = Double.parseDouble(etBuyKaratWeight.getText().toString());
                }

                polish = cbBuyPolish.isChecked();
                done = cbBuyDone.isChecked();

                if (etBuyDoneWeight.getText().toString().isEmpty()) {
                    wage = -1;
                } else {
                    doneWeight = Double.parseDouble(etBuyDoneWeight.getText().toString());
                }

                if (etBuyWage.getText().toString().isEmpty()) {
                    wage = 50;
                } else {
                    wage = Double.parseDouble(etBuyWage.getText().toString());
                }

                if (!theDate) {
                    Toast.makeText(BuyActivity.this, "יש למלא את התאריך ב6 ספרות", Toast.LENGTH_SHORT).show();
                } else if (ok) {
                    Intent intent = new Intent();
                    intent.putExtra("date", date);
                    intent.putExtra("supplier", supplier);
                    intent.putExtra("id", id);
                    intent.putExtra("price", price);
                    intent.putExtra("weight", weight);
                    intent.putExtra("polish", polish);
                    intent.putExtra("doneWeight", doneWeight);
                    intent.putExtra("wage", wage);
                    setResult(RESULT_OK, intent);
                    finishActivity(MainActivity.buy);
                    finish();
                } else {
                    Toast.makeText(BuyActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
