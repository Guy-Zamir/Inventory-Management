package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.guy.inventory.R;

public class BrokerMainSortActivity extends AppCompatActivity {
    LinearLayout llPR, llAS, llRA, llEM, llCU, llBA, llALL;
    boolean memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_main_sort);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        llPR = findViewById(R.id.llPR);
        llAS = findViewById(R.id.llAS);
        llRA = findViewById(R.id.llRA);
        llEM = findViewById(R.id.llEM);
        llCU = findViewById(R.id.llCU);
        llBA = findViewById(R.id.llBA);
        llALL = findViewById(R.id.llALL);

        memo = getIntent().getBooleanExtra("memo", false);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(memo ? "מיונים - ממו" : "מיונים - ניב");
        actionBar.setDisplayHomeAsUpEnabled(true);

        llALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "כל המיונים");
                intent.putExtra("memo", memo);
                startActivity(intent);
            }
        });

        llAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "אשרים");
                intent.putExtra("memo", memo);
                startActivity(intent);
            }
        });

        llEM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "אמרלדים");
                intent.putExtra("memo", memo);
                startActivity(intent);
            }
        });

        llPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "פרינססים");
                intent.putExtra("memo", memo);
                startActivity(intent);
            }
        });

        llRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "רדיאנים");
                intent.putExtra("memo", memo);
                startActivity(intent);
            }
        });

        llCU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "קושונים");
                intent.putExtra("memo", memo);
                startActivity(intent);
            }
        });

        llBA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerMainSortActivity.this, TableBrokerActivity.class);
                intent.putExtra("kind", "בגטים");
                intent.putExtra("memo", memo);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
