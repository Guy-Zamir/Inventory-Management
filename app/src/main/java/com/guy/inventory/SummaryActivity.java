package com.guy.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import java.text.DecimalFormat;

public class SummaryActivity extends AppCompatActivity {
    TextView tvAllSales, tvAllBuys, tvAllWork, tvAllWorkWeight;
    double allBuys, allSales, allWork, allWorkWeight;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summaryactivity);
        tvAllBuys = findViewById(R.id.tvAllBuys);
        tvAllSales = findViewById(R.id.tvAllSales);
        tvAllWork = findViewById(R.id.tvAllWork);
        tvAllWorkWeight = findViewById(R.id.tvAllWorkWeight);
        DecimalFormat nf = new DecimalFormat( "#,###,###,###,###,###.##" );
//
//        for (Buy buy : MainActivity.buyArray) {
//            allBuys += (buy.getPrice() * buy.getWeight());
//            if (!buy.isDone()) {
//                allWork += buy.getPrice()*buy.getWeight();
//                allWorkWeight += buy.getWeight();
//            }
//        }
//
//        for (Sale sale : MainActivity.saleArray) {
//            allSales += sale.getSaleSum();
//        }
//
//        tvAllBuys.setText(nf.format(allBuys)+"$");
//        tvAllSales.setText(nf.format(allSales)+"$");
//        tvAllWork.setText(nf.format(allWork)+"$");
//        tvAllWorkWeight.setText(nf.format(allWorkWeight) + "קראט ");
    }
}
