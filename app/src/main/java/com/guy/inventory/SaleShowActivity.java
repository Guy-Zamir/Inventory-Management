package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class SaleShowActivity extends AppCompatActivity {
    TableLayout tlSale;
    TableLayout tlSaleHeader;
    Button btnSaleShowDelete, btnSaleShowDetails;
    ArrayList<CheckBox> checkBoxes;
    boolean firstDisplay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_show);
        tlSale = findViewById(R.id.tlSale);
        tlSaleHeader = findViewById(R.id.tlSaleHeader);
        btnSaleShowDelete = findViewById(R.id.btnSaleShowDelete);
        btnSaleShowDetails = findViewById(R.id.btnSaleShowDetails);

        displayTable();

        btnSaleShowDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPositions().isEmpty()) {
                    Toast.makeText(SaleShowActivity.this, "יש לסמן על הפריטים שברצונך למחוק", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i : checkPositions()) {
                        MainActivity.saleArray.remove(i);
                        tlSale.removeAllViews();
                        displayTable();
                    }
                }
            }
        });

        btnSaleShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void displayTable() {
        NumberFormat nf = new DecimalFormat("#.####");
        checkBoxes = new ArrayList<>();
        for (int i = MainActivity.saleArray.size() - 1; i >= 0; i--) {
            CheckBox cb;
            TextView date;
            TextView company;
            TextView saleSum;
            TableRow tableRow;

            tableRow = new TableRow(SaleShowActivity.this);
            date = new TextView(SaleShowActivity.this);
            company = new TextView(SaleShowActivity.this);
            saleSum = new TextView(SaleShowActivity.this);
            cb = new CheckBox(SaleShowActivity.this);

            cb.setHeight(150);
            checkBoxes.add(cb);

            date.setGravity(Gravity.CENTER);
            date.setWidth(225);
            date.setTextColor(getResources().getColor(R.color.colorBlack));
            date.setTypeface(Typeface.DEFAULT_BOLD);

            company.setGravity(Gravity.CENTER);
            company.setWidth(400);
            company.setTextColor(getResources().getColor(R.color.colorBlack));
            company.setTypeface(Typeface.DEFAULT_BOLD);

            saleSum.setGravity(Gravity.CENTER);
            saleSum.setWidth(250);
            saleSum.setTextColor(getResources().getColor(R.color.colorBlack));
            saleSum.setTypeface(Typeface.DEFAULT_BOLD);

            if (firstDisplay) {
                date.setText("תאריך");
                date.setPaintFlags(date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                date.setTextSize(18);

                company.setText("חברה");
                company.setPaintFlags(company.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                company.setTextSize(18);

                saleSum.setText("סכום");
                saleSum.setPaintFlags(saleSum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                saleSum.setTextSize(18);

                tableRow.addView(date);
                tableRow.addView(company);
                tableRow.addView(saleSum);
                tlSaleHeader.addView(tableRow);
                firstDisplay = false;
                i+=1;

            } else {
                date.setText(MainActivity.saleArray.get(i).getDate());
                company.setText(MainActivity.saleArray.get(i).getCompany());
                saleSum.setText((nf.format(MainActivity.saleArray.get(i).getSaleSum())) + "$");
                tableRow.addView(cb);
                tableRow.addView(date);
                tableRow.addView(company);
                tableRow.addView(saleSum);
                tlSale.addView(tableRow);
            }
        }
    }

    public ArrayList<Integer> checkPositions() {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                int position = checkBoxes.size() - 1 - i;
                positions.add(position);
            }
        }
        return positions;
    }
}
