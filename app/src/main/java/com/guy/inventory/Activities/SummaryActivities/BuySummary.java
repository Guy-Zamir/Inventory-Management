package com.guy.inventory.Activities.SummaryActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;
import com.guy.inventory.R;
import java.text.DecimalFormat;

public class BuySummary extends AppCompatActivity {
    TextView tvSummaryBuySum, tvSummaryBuyWeight, tvSummaryBuyPrice, tvSummaryBuyRoughSum,
            tvSummaryBuyRoughWeight, tvSummaryBuyRoughPrice, tvSummaryBuyPolishSum,
            tvSummaryBuyPolishWeight, tvSummaryBuyPolishPrice;

    double buySum;
    double buyWeight;
    double buyPrice;
    double buyRoughSum;
    double buyRoughWeight;
    double buyRoughPrice;
    double buyPolishSum;
    double buyPolishWeight;
    double buyPolishPrice;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_summary);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvSummaryBuySum = findViewById(R.id.tvSummaryBuySum);
        tvSummaryBuyWeight = findViewById(R.id.tvSummaryBuyWeight);
        tvSummaryBuyPrice = findViewById(R.id.tvSummaryBuyPrice);
        tvSummaryBuyRoughSum = findViewById(R.id.tvSummaryBuyRoughSum);
        tvSummaryBuyRoughWeight = findViewById(R.id.tvSummaryBuyRoughWeight);
        tvSummaryBuyRoughPrice = findViewById(R.id.tvSummaryBuyRoughPrice);
        tvSummaryBuyPolishSum = findViewById(R.id.tvSummaryBuyPolishSum);
        tvSummaryBuyPolishWeight = findViewById(R.id.tvSummaryBuyPolishWeight);
        tvSummaryBuyPolishPrice = findViewById(R.id.tvSummaryBuyPolishPrice);

        buySum = getIntent().getDoubleExtra("buySum", 0);
        buyWeight = getIntent().getDoubleExtra("buyWeight", 0);
        buyPrice = getIntent().getDoubleExtra("buyPrice", 0);
        buyRoughSum = getIntent().getDoubleExtra("buyRoughSum", 0);
        buyRoughWeight = getIntent().getDoubleExtra("buyRoughWeight", 0);
        buyRoughPrice = getIntent().getDoubleExtra("buyRoughPrice", 0);
        buyPolishSum = getIntent().getDoubleExtra("buyPolishSum", 0);
        buyPolishWeight = getIntent().getDoubleExtra("buyPolishWeight", 0);
        buyPolishPrice = getIntent().getDoubleExtra("buyPolishPrice", 0);

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

        tvSummaryBuySum.setText("סכום:  " + nf.format(buySum) + "$");
        tvSummaryBuyWeight.setText("משקל:  " + nf.format(buyWeight));
        tvSummaryBuyPrice.setText("מחיר ממוצע:  " + nf.format(buyPrice) + "$");
        tvSummaryBuyRoughSum.setText("סכום:  " + nf.format(buyRoughSum) + "$");
        tvSummaryBuyRoughWeight.setText("משקל:  " + nf.format(buyRoughWeight));
        tvSummaryBuyRoughPrice.setText("מחיר ממוצע:  " + nf.format(buyRoughPrice) + "$");
        tvSummaryBuyPolishSum.setText("סכום:  " + nf.format(buyPolishSum) + "$");
        tvSummaryBuyPolishWeight.setText("משקל:  " + nf.format(buyPolishWeight));
        tvSummaryBuyPolishPrice.setText("מחיר ממוצע:  " + nf.format(buyPolishPrice) + "$");
    }
}
