package com.guy.inventory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.text.DecimalFormat;
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
                if (checkPositions().isEmpty()) {
                    Toast.makeText(BuyShowActivity.this, "יש לסמן את הפריט שברצונך לערוך", Toast.LENGTH_SHORT).show();
                } else if (checkPositions().size() > 1) {
                    Toast.makeText(BuyShowActivity.this, "יש לבחור פריט אחד בלבד לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(BuyShowActivity.this, BuyEditActivity.class);
                    intent.putExtra("position", checkPositions().get(0));
                    startActivityForResult(intent, 1);
                }
            }
        });

        btnBuyShowDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPositions().isEmpty()) {
                    Toast.makeText(BuyShowActivity.this, "יש לסמן על הפריטים שברצונך למחוק", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(BuyShowActivity.this);
                    alert.setTitle("התראת מחיקה");
                    alert.setMessage("האם אתה בטוח שברצונך למחוק את הנתונים המסומנים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);

                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i : checkPositions()) {
                                MainActivity.buyArray.remove(i);
                                tlBuy.removeAllViews();
                                displayTable();
                            }
                        }
                    });
                    alert.show();
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

                date.setText(String.valueOf(MainActivity.buyArray.get(i).getBuyDate()).substring(0, 2) + "/" + String.valueOf(MainActivity.buyArray.get(i).getBuyDate()).substring(2, 4));
                price.setText(nf.format(MainActivity.buyArray.get(i).getPrice()) + "$");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            tlBuy.removeAllViews();
            displayTable();
        }
    }
}
