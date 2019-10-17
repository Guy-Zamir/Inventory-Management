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
import android.widget.Toast;

public class SaleEditActivity extends AppCompatActivity {
    DatePicker dpSaleEditDate;
    EditText etSaleEditCompany, etSaleEditID, etSaleEditWeight, etSaleEditSum;
    Button btnSaleEditSubmit;
    int pos;

    String company = "", id = "", date = "";
    double weight = 0, saleSum = 0;
    boolean toast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_edit);
        dpSaleEditDate = findViewById(R.id.dpSaleEditDate);
        etSaleEditCompany = findViewById(R.id.etSaleEditCompany);
        etSaleEditID = findViewById(R.id.etSaleEditID);
        etSaleEditWeight = findViewById(R.id.etSaleEditWeight);
        etSaleEditSum = findViewById(R.id.etSaleEditSum);
        btnSaleEditSubmit = findViewById(R.id.btnSaleEditSubmit);

        pos = getIntent().getIntExtra("pos", 0);
        etSaleEditCompany.setText(MainActivity.saleArray.get(pos).getCompany());
        etSaleEditID.setText(MainActivity.saleArray.get(pos).getId());
        etSaleEditWeight.setText(String.valueOf(MainActivity.saleArray.get(pos).getWeight()));
        etSaleEditSum.setText(String.valueOf(MainActivity.saleArray.get(pos).getSaleSum()));

        int day = Integer.parseInt(MainActivity.saleArray.get(pos).getSaleDate().substring(0,2));
        int month = Integer.parseInt(MainActivity.saleArray.get(pos).getSaleDate().substring(2,4))-1;
        int year = Integer.parseInt(MainActivity.saleArray.get(pos).getSaleDate().substring(4,8));
        dpSaleEditDate.updateDate(year, month, day);

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

                if (etSaleEditCompany.getText().toString().isEmpty()) {
                    toast = false;
                } else {
                    company = etSaleEditCompany.getText().toString();
                }

                if (etSaleEditID.getText().toString().isEmpty()) {
                    toast = false;
                } else {
                    id = etSaleEditID.getText().toString();
                }

                if (etSaleEditWeight.getText().toString().isEmpty()) {
                    toast = false;
                } else {
                    weight = Double.parseDouble(etSaleEditWeight.getText().toString());
                }

                if (etSaleEditSum.getText().toString().isEmpty()) {
                    toast = false;
                } else {
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
                            MainActivity.saleArray.get(pos).setSaleDate(date);
                            MainActivity.saleArray.get(pos).setCompany(company);
                            MainActivity.saleArray.get(pos).setId(id);
                            MainActivity.saleArray.get(pos).setWeight(weight);
                            MainActivity.saleArray.get(pos).setSaleSum(saleSum);
                            setResult(RESULT_OK);
                            finishActivity(1);
                            finish();
                        }
                    });
                        alert.show();
                }
            }
        });
    }
}