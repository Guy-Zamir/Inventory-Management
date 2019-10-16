package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class BuyActivity extends AppCompatActivity {

    DatePicker dpBuyDate;
    EditText etBuySupplier, etBuyID, etBuyKaratPrice, etBuyKaratWeight, etBuyDoneWeight, etBuyWage;
    CheckBox cbBuyPolish, cbBuyDone;
    Button btnBuySubmit;
    String supplier, id, date;
    int day, month, year;
    boolean polish, done;
    double price, weight, doneWeight, wage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyactivity);

        dpBuyDate = findViewById(R.id.dpBuyDate);
        etBuySupplier = findViewById(R.id.etBuySupplier);
        etBuyID = findViewById(R.id.etBuyID);
        etBuyKaratPrice = findViewById(R.id.etBuyKaratPrice);
        etBuyKaratWeight = findViewById(R.id.etBuyKaratWeight);
        etBuyDoneWeight = findViewById(R.id.etBuyDoneWeight);
        etBuyWage = findViewById(R.id.etBuyWage);
        cbBuyPolish = findViewById(R.id.cbBuyPolish);
        cbBuyDone = findViewById(R.id.cbBuyDone);
        btnBuySubmit = findViewById(R.id.btnBuySubmit);

        cbBuyPolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbBuyPolish.isChecked()) {
                    cbBuyDone.setChecked(true);
                    etBuyWage.setVisibility(View.GONE);
                    etBuyDoneWeight.setVisibility(View.GONE);
                } else {
                    etBuyWage.setVisibility(View.VISIBLE);
                    etBuyDoneWeight.setVisibility(View.VISIBLE);
                }
            }
        });

        btnBuySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean toast = false;

                day = dpBuyDate.getDayOfMonth();
                month = dpBuyDate.getMonth();
                year = dpBuyDate.getYear();

                if (etBuySupplier.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    supplier = etBuySupplier.getText().toString();
                }

                if (etBuyID.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    id = etBuyID.getText().toString();
                }

                if (etBuyKaratPrice.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    price = Double.parseDouble(etBuyKaratPrice.getText().toString());
                }

                if (etBuyKaratWeight.getText().toString().isEmpty()) {
                    toast = true;
                } else {
                    weight = Double.parseDouble(etBuyKaratWeight.getText().toString());
                }

                polish = cbBuyPolish.isChecked();
                done = cbBuyDone.isChecked();

                if (polish) {
                    wage = 0;
                    doneWeight = weight;
                } else if (done){
                    if (etBuyWage.getText().toString().isEmpty()) {
                        wage = 50;
                    } else {
                        wage = Double.valueOf(etBuyWage.getText().toString());
                    }

                    if (etBuyDoneWeight.getText().toString().isEmpty()) {
                        toast = true;
                    } else {
                        doneWeight = Double.valueOf(etBuyDoneWeight.getText().toString());
                    }
                } else {
                    if (etBuyWage.getText().toString().isEmpty()) {
                        wage = 50;
                    } else {
                        wage = Double.valueOf(etBuyWage.getText().toString());
                    }
                    if (etBuyDoneWeight.getText().toString().isEmpty()) {
                        doneWeight = 0;
                    } else {
                        done = true;
                        doneWeight = Double.valueOf(etBuyDoneWeight.getText().toString());
                    }
                }


                if (toast) {
                    Toast.makeText(BuyActivity.this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("day", day);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("supplier", supplier);
                    intent.putExtra("id", id);
                    intent.putExtra("price", price);
                    intent.putExtra("weight", weight);
                    intent.putExtra("polish", polish);
                    intent.putExtra("done", done);
                    intent.putExtra("doneWeight", doneWeight);
                    intent.putExtra("wage", wage);
                    setResult(RESULT_OK, intent);
                    finishActivity(MainActivity.buy);
                    finish();
                }
            }
        });
    }
}
