package com.guy.inventory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    Button btnResult, btnBuy, btnSale, btnBuyShow, btnSaleShow;
    public static LinkedList<Buy> buyArray = new LinkedList<>();
    public static LinkedList<Sale> saleArray = new LinkedList<>();
    public final static int buy = 1;
    public final static int sale = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnResult = findViewById(R.id.btnResult);
        btnBuy = findViewById(R.id.btnBuy);
        btnSale = findViewById(R.id.btnSale);
        btnBuyShow = findViewById(R.id.btnBuyShow);
        btnSaleShow = findViewById(R.id.btnSaleShow);


        buyArray.add(new Buy(17101992, "יהלומי אלכס דניאל בעמ", "10016", 335, 26.61, false, false, 26.61, 50));
        buyArray.add(new Buy(17101992, "יהלומי דבש בעמ", "12312", 340, 123.2, false, true, 16.6, 50));
        buyArray.add(new Buy(17101992, "יהלומי חרא בעמ", "23234", 543, 564.2, false, true, 16.6, 50));
        buyArray.add(new Buy(17101992, "יהלומי קקי בעמ", "234231", 1543.00, 489.2, false, true, 16.6, 50));
        buyArray.add(new Buy(17101992, "יהלומי גמלים בעמ", "03434", 832.12, 152.2, false, false, 16.6, 50));
        buyArray.add(new Buy(17101992, "יהלומי דובשניות בעמ", "00234233", 1230, 14.2, false, true, 16.6, 50));
        buyArray.add(new Buy(17101992, "יהלומי קפאין בעמ", "0012313", 35, 1.2, false, true, 16.6, 50));

        saleArray.add(new Sale(17101992, "יהלומי ביצ'צ'י בעמ", "1239823", 1214234.23));
        saleArray.add(new Sale(17101992, "יהלומי ג'ינג'ר בעמ", "16516", 9432.26));
        saleArray.add(new Sale(17101992, "יהלומי חומייני בעמ", "1893", 45453.26));
        saleArray.add(new Sale(17101992, "יהלומי הגזמנו בעמ", "32186", 84513.84));
        saleArray.add(new Sale(17101992, "יהלומי ג'ון סנוו בעמ", "12318", 18532.20));
        saleArray.add(new Sale(17101992, "יהלומי ראגנאר בעמ", "16152", 18762.87));


        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                startActivity(intent);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuyActivity.class);
                startActivityForResult(intent, buy);
            }
        });

        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SaleActivity.class);
                startActivityForResult(intent, sale);
            }
        });

        btnBuyShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuyShowActivity.class);
                startActivity(intent);
            }
        });

        btnSaleShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SaleShowActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == buy) {
            if (data != null) {
                int date = (Integer.parseInt((data.getIntExtra("day", 0)) + (data.getIntExtra("month", 0)) + (String.valueOf(data.getIntExtra("year", 0)))));
                String supplier = data.getStringExtra("supplier");
                String id = data.getStringExtra("id");
                double price = data.getDoubleExtra("price", 0);
                double weight = data.getDoubleExtra("weight", 0);
                double doneWeight = data.getDoubleExtra("doneWeight", 0);
                double wage = data.getDoubleExtra("wage", 0);
                boolean polish = data.getBooleanExtra("polish", false);
                boolean done = data.getBooleanExtra("done", false);
                Buy buy = new Buy(date, supplier, id, price, weight, polish, done, doneWeight, wage);
                buyArray.add(buy);
            }
        } else if (requestCode == sale) {
            if (data != null) {
                int date = data.getIntExtra("date", 0);
                String company = data.getStringExtra("company");
                String id = data.getStringExtra("id");
                double saleSum = data.getDoubleExtra("saleSum", 0);
                Sale sale = new Sale(date, company, id, saleSum);
                saleArray.add(sale);
            }
        }
    }
}
