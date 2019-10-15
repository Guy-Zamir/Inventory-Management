package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
        checkBoxes = new ArrayList<>();
        for (int i = MainActivity.buyArray.size() - 1; i >= 0; i--) {
            TextView date, price, weight, doneWeight;
            final TableRow tableRow;

            tableRow = new TableRow(BuyShowActivity.this);
            date = new TextView(BuyShowActivity.this);
            doneWeight = new TextView(BuyShowActivity.this);
            price = new TextView(BuyShowActivity.this);
            weight = new TextView(BuyShowActivity.this);

            date.setGravity(Gravity.CENTER);
            date.setWidth(175);
            date.setTextColor(getResources().getColor(R.color.colorBlack));
            date.setTypeface(Typeface.DEFAULT_BOLD);

            price.setGravity(Gravity.CENTER);
            price.setWidth(225);
            price.setTextColor(getResources().getColor(R.color.colorBlack));
            price.setTypeface(Typeface.DEFAULT_BOLD);

            weight.setTypeface(Typeface.DEFAULT_BOLD);
            weight.setGravity(Gravity.CENTER);
            weight.setWidth(175);
            weight.setTextColor(getResources().getColor(R.color.colorBlack));

            doneWeight.setTypeface(Typeface.DEFAULT_BOLD);
            doneWeight.setGravity(Gravity.CENTER);
            doneWeight.setWidth(250);
            doneWeight.setTextColor(getResources().getColor(R.color.colorBlack));

            if (firstDisplay) {
                TextView doneText;
                doneText = new TextView(BuyShowActivity.this);

                date.setText("תאריך");
                date.setTextSize(16);

                price.setText("מחיר");
                price.setTextSize(16);

                weight.setText("משקל");
                weight.setTextSize(16);

                doneText.setText("גמור");
                doneText.setGravity(Gravity.CENTER);
                doneText.setWidth(100);
                doneText.setTextColor(getResources().getColor(R.color.colorBlack));
                doneText.setTypeface(Typeface.DEFAULT_BOLD);
                doneText.setTextSize(16);

                doneWeight.setText("משקל גמור");
                doneWeight.setTextSize(16);

                tableRow.addView(date);
                tableRow.addView(price);
                tableRow.addView(weight);
                tableRow.addView(doneWeight);
                tableRow.addView(doneText);
                tlBuyHeader.addView(tableRow);
                firstDisplay = false;
                i+=1;

            } else {
                CheckBox cb;
                RadioButton done;
                cb = new CheckBox(BuyShowActivity.this);
                done = new RadioButton(BuyShowActivity.this);

                cb.setHeight(125);
                cb.setWidth(75);
                checkBoxes.add(cb);

                done.setWidth(100);
                done.setGravity(Gravity.CENTER);
                done.setClickable(false);

                date.setText(String.valueOf(MainActivity.buyArray.get(i).getDate()).substring(0, 2) + "/" + String.valueOf(MainActivity.buyArray.get(i).getDate()).substring(2, 4));
                price.setText((nf.format(MainActivity.buyArray.get(i).getPrice())) + "$");
                weight.setText((nf.format(MainActivity.buyArray.get(i).getWeight())));
                doneWeight.setText(nf.format(MainActivity.buyArray.get(i).getDoneWeight()));

                if (MainActivity.buyArray.get(i).isDone()) {
                    done.toggle();
                }

                tableRow.addView(cb);
                tableRow.addView(date);
                tableRow.addView(price);
                tableRow.addView(weight);
                tableRow.addView(doneWeight);
                tableRow.addView(done);
                tlBuy.addView(tableRow);

                View underLine = new View(this);
                underLine.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2));
                underLine.setBackgroundColor(Color.rgb(51, 51, 51));
                tlBuy.addView(underLine);
            }
        }
    }
}
