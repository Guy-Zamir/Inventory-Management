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

public class BuyShowActivity extends AppCompatActivity {
    TableLayout tlBuy;
    TableLayout tlBuyHeader;
    Button btnBuyShowDelete, btnBuyShowDetails;
    ArrayList<CheckBox> checkBoxes;
    boolean firstDisplay = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_show);
        tlBuy = findViewById(R.id.tlBuy);
        tlBuyHeader = findViewById(R.id.tlBuyHeader);
        btnBuyShowDelete = findViewById(R.id.btnBuyShowDelete);
        btnBuyShowDetails = findViewById(R.id.btnBuyShowDetails);

        displayTable();

        btnBuyShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnBuyShowDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPositions().isEmpty()) {
                    Toast.makeText(BuyShowActivity.this, "יש לסמן על הפריטים שברצונך למחוק", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i : checkPositions()) {
                        MainActivity.buyArray.remove(i);
                        tlBuy.removeAllViews();
                        displayTable();
                    }
                }
            }
        });
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

    @SuppressLint("SetTextI18n")
    public void displayTable() {
        NumberFormat nf = new DecimalFormat("#.####");
        checkBoxes = new ArrayList<>();
        for (int i = MainActivity.buyArray.size() - 1; i >= 0; i--) {
            CheckBox cb;
            TextView date;
            TextView supplier;
            TextView price;
            TextView weight;
            TableRow tableRow;

            tableRow = new TableRow(BuyShowActivity.this);
            date = new TextView(BuyShowActivity.this);
            supplier = new TextView(BuyShowActivity.this);
            price = new TextView(BuyShowActivity.this);
            weight = new TextView(BuyShowActivity.this);
            cb = new CheckBox(BuyShowActivity.this);

            cb.setHeight(150);
            checkBoxes.add(cb);

            date.setGravity(Gravity.CENTER);
            date.setWidth(225);
            date.setTextColor(getResources().getColor(R.color.colorBlack));
            date.setTypeface(Typeface.DEFAULT_BOLD);

            supplier.setGravity(Gravity.CENTER);
            supplier.setWidth(300);
            supplier.setTextColor(getResources().getColor(R.color.colorBlack));
            supplier.setTypeface(Typeface.DEFAULT_BOLD);

            price.setGravity(Gravity.CENTER);
            price.setWidth(175);
            price.setTextColor(getResources().getColor(R.color.colorBlack));
            price.setTypeface(Typeface.DEFAULT_BOLD);

            weight.setTypeface(Typeface.DEFAULT_BOLD);
            weight.setGravity(Gravity.CENTER);
            weight.setWidth(175);
            weight.setTextColor(getResources().getColor(R.color.colorBlack));

            if (firstDisplay) {
                date.setText("תאריך");
                date.setPaintFlags(date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                date.setTextSize(18);

                supplier.setText("ספק");
                supplier.setPaintFlags(supplier.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                supplier.setTextSize(18);

                price.setText("מחיר");
                price.setPaintFlags(price.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                price.setTextSize(18);

                weight.setText("משקל");
                weight.setPaintFlags(weight.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                weight.setTextSize(18);

                tableRow.addView(date);
                tableRow.addView(supplier);
                tableRow.addView(price);
                tableRow.addView(weight);
                tlBuyHeader.addView(tableRow);
                firstDisplay = false;
                i+=1;

            } else {
                date.setText(MainActivity.buyArray.get(i).getDate());
                supplier.setText(MainActivity.buyArray.get(i).getSupplier());
                price.setText((nf.format(MainActivity.buyArray.get(i).getPrice())) + "$");
                weight.setText((nf.format(MainActivity.buyArray.get(i).getWeight())));
                tableRow.addView(cb);
                tableRow.addView(date);
                tableRow.addView(supplier);
                tableRow.addView(price);
                tableRow.addView(weight);
                tlBuy.addView(tableRow);
            }
        }
    }
}
