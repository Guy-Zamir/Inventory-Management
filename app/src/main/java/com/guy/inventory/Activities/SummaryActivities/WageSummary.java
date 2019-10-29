package com.guy.inventory.Activities.SummaryActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.guy.inventory.R;

import java.text.DecimalFormat;

public class WageSummary extends AppCompatActivity {
    TextView tvSummaryWageSum, tvSummaryWageWeight, tvSummaryWagePre, tvSummaryWagePrice;

    double wageSum;
    double wageWeight;
    double wagePer;
    double wagePrice;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wage_summary);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvSummaryWageSum = findViewById(R.id.tvSummaryWageSum);
        tvSummaryWageWeight = findViewById(R.id.tvSummaryWageWeight);
        tvSummaryWagePre = findViewById(R.id.tvSummaryWagePre);
        tvSummaryWagePrice = findViewById(R.id.tvSummaryWagePrice);

        wageSum = getIntent().getDoubleExtra("wageSum", 0);
        wageWeight = getIntent().getDoubleExtra("wageWeight", 0);
        wagePer = getIntent().getDoubleExtra("wagePer", 0);
        wagePrice = getIntent().getDoubleExtra("wagePrice", 0);

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

        tvSummaryWageSum.setText("סכום:  " + nf.format(wageSum) + "$");
        tvSummaryWageWeight.setText("משקל פחת:  " + nf.format(wageWeight));
        tvSummaryWagePre.setText("אחוז פחת ממוצע:  " + nf.format((1-wagePer)*100) + "%");
        tvSummaryWagePrice.setText("מחיר ממוצע:  " + nf.format(wagePrice) + "$");
    }
}
