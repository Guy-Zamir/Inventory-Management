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

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

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

        pos = getIntent().getIntExtra("checkIndex", 0);
        etSaleEditCompany.setText(InventoryApp.sales.get(pos).getCompany());
        etSaleEditID.setText(InventoryApp.sales.get(pos).getId());
        etSaleEditWeight.setText(String.valueOf(InventoryApp.sales.get(pos).getWeight()));
        etSaleEditSum.setText(String.valueOf(InventoryApp.sales.get(pos).getSaleSum()));

        int day = Integer.parseInt(InventoryApp.sales.get(pos).getSaleDate().substring(0,2));
        int month = Integer.parseInt(InventoryApp.sales.get(pos).getSaleDate().substring(2,4))-1;
        int year = Integer.parseInt(InventoryApp.sales.get(pos).getSaleDate().substring(4,8));
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
                            InventoryApp.sales.get(pos).setSaleDate(date);
                            InventoryApp.sales.get(pos).setCompany(company);
                            InventoryApp.sales.get(pos).setId(id);
                            InventoryApp.sales.get(pos).setWeight(weight);
                            InventoryApp.sales.get(pos).setSaleSum(saleSum);
                            Backendless.Persistence.save(InventoryApp.sales.get(pos), new AsyncCallback<Sale>() {
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
}