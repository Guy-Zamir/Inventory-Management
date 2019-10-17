package com.guy.inventory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

        btnSaleShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPositions().isEmpty()) {
                    Toast.makeText(SaleShowActivity.this, "יש לסמן את הפריט שברצונך לערוך", Toast.LENGTH_SHORT).show();
                } else if (checkPositions().size() > 1) {
                    Toast.makeText(SaleShowActivity.this, "יש לבחור פריט אחד בלבד לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SaleShowActivity.this, SaleEditActivity.class);
                    int pos = checkPositions().get(0);
                    intent.putExtra("pos", pos);
                    startActivityForResult(intent, 2);
                }
            }
        });

        btnSaleShowDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPositions().isEmpty()) {
                    Toast.makeText(SaleShowActivity.this, "יש לסמן על הפריטים שברצונך למחוק", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SaleShowActivity.this);
                    alert.setTitle("התראת מחיקה");
                    alert.setMessage("האם אתה בטוח שברצונך למחוק את הנתונים המסומנים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);

                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i : checkPositions()) {
                                MainActivity.saleArray.remove(i);
                                tlSale.removeAllViews();
                                displayTable();
                            }
                        }
                    });
                    alert.show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void displayTable() {
        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
        checkBoxes = new ArrayList<>();
        for (int i = MainActivity.saleArray.size() - 1; i >= 0; i--) {
            TextView date;
            TextView company;
            TextView weight;
            TextView saleSum;
            TableRow tableRow;

            tableRow = new TableRow(SaleShowActivity.this);
            date = new TextView(SaleShowActivity.this);
            company = new TextView(SaleShowActivity.this);
            weight = new TextView(SaleShowActivity.this);
            saleSum = new TextView(SaleShowActivity.this);


            date.setGravity(Gravity.CENTER);
            date.setWidth(170);
            date.setTextColor(getResources().getColor(R.color.colorBlack));
            date.setTypeface(Typeface.DEFAULT_BOLD);

            company.setGravity(Gravity.CENTER);
            company.setWidth(360);
            company.setTextColor(getResources().getColor(R.color.colorBlack));
            company.setTypeface(Typeface.DEFAULT_BOLD);

            weight.setGravity(Gravity.CENTER);
            weight.setWidth(150);
            weight.setTextColor(getResources().getColor(R.color.colorBlack));
            weight.setTypeface(Typeface.DEFAULT_BOLD);

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

                weight.setText("משקל");
                weight.setPaintFlags(saleSum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                weight.setTextSize(18);

                saleSum.setText("סכום");
                saleSum.setPaintFlags(saleSum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                saleSum.setTextSize(18);

                tableRow.addView(date);
                tableRow.addView(company);
                tableRow.addView(weight);
                tableRow.addView(saleSum);
                tlSaleHeader.addView(tableRow);
                firstDisplay = false;
                i+=1;

            } else {
                CheckBox cb;
                cb = new CheckBox(SaleShowActivity.this);

                cb.setWidth(75);
                cb.setHeight(125);
                checkBoxes.add(cb);

                date.setText(String.valueOf(MainActivity.saleArray.get(i).getSaleDate()).substring(0, 2) + "/" + String.valueOf(MainActivity.saleArray.get(i).getSaleDate()).substring(2, 4));
                company.setText(MainActivity.saleArray.get(i).getCompany());
                weight.setText(nf.format(MainActivity.saleArray.get(i).getWeight()));
                saleSum.setText((nf.format(MainActivity.saleArray.get(i).getSaleSum())) + "$");
                tableRow.addView(cb);
                tableRow.addView(date);
                tableRow.addView(company);
                tableRow.addView(weight);
                tableRow.addView(saleSum);
                tlSale.addView(tableRow);

                View underLine = new View(this);
                underLine.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2));
                underLine.setBackgroundColor(Color.rgb(51, 51, 51));
                tlSale.addView(underLine);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            tlSale.removeAllViews();
            displayTable();
        }
    }
}
