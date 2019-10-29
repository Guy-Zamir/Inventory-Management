package com.guy.inventory.Activities.SummaryActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;
import com.guy.inventory.R;
import java.text.DecimalFormat;

public class SaleSummary extends AppCompatActivity {
    TextView tvSummarySaleSum, tvSummarySaleWeight, tvSummarySalePrice, tvSummarySaleRoughSum,
            tvSummarySaleRoughWeight, tvSummarySaleRoughPrice, tvSummarySalePolishSum,
            tvSummarySalePolishWeight, tvSummarySalePolishPrice;

    double saleSum;
    double saleWeight;
    double salePrice;
    double saleRoughSum;
    double saleRoughWeight;
    double saleRoughPrice;
    double salePolishSum;
    double salePolishWeight;
    double salePolishPrice;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_summary);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvSummarySaleSum = findViewById(R.id.tvSummarySaleSum);
        tvSummarySaleWeight = findViewById(R.id.tvSummarySaleWeight);
        tvSummarySalePrice = findViewById(R.id.tvSummarySalePrice);
        tvSummarySaleRoughSum = findViewById(R.id.tvSummarySaleRoughSum);
        tvSummarySaleRoughWeight = findViewById(R.id.tvSummarySaleRoughWeight);
        tvSummarySaleRoughPrice = findViewById(R.id.tvSummarySaleRoughPrice);
        tvSummarySalePolishSum = findViewById(R.id.tvSummarySalePolishSum);
        tvSummarySalePolishWeight = findViewById(R.id.tvSummarySalePolishWeight);
        tvSummarySalePolishPrice = findViewById(R.id.tvSummarySalePolishPrice);

        saleSum = getIntent().getDoubleExtra("saleSum", 0);
        saleWeight = getIntent().getDoubleExtra("saleWeight", 0);
        salePrice = getIntent().getDoubleExtra("salePrice", 0);
        saleRoughSum = getIntent().getDoubleExtra("saleRoughSum", 0);
        saleRoughWeight = getIntent().getDoubleExtra("saleRoughWeight", 0);
        saleRoughPrice = getIntent().getDoubleExtra("saleRoughPrice", 0);
        salePolishSum = getIntent().getDoubleExtra("salePolishSum", 0);
        salePolishWeight = getIntent().getDoubleExtra("salePolishWeight", 0);
        salePolishPrice = getIntent().getDoubleExtra("salePolishPrice", 0);

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );

        tvSummarySaleSum.setText("סכום:  " + nf.format(saleSum) + "$");
        tvSummarySaleWeight.setText("משקל:  " + nf.format(saleWeight));
        tvSummarySalePrice.setText("מחיר ממוצע:  " + nf.format(salePrice) + "$");
        tvSummarySaleRoughSum.setText("סכום:  " + nf.format(saleRoughSum) + "$");
        tvSummarySaleRoughWeight.setText("משקל:  " + nf.format(saleRoughWeight));
        tvSummarySaleRoughPrice.setText("מחיר ממוצע:  " + nf.format(saleRoughPrice) + "$");
        tvSummarySalePolishSum.setText("סכום:  " + nf.format(salePolishSum) + "$");
        tvSummarySalePolishWeight.setText("משקל:  " + nf.format(salePolishWeight));
        tvSummarySalePolishPrice.setText("מחיר ממוצע:  " + nf.format(salePolishPrice) + "$");
    }
}
