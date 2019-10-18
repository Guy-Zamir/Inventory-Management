package com.guy.inventory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import java.util.ArrayList;
import java.util.List;


public class SaleShowActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    ListView lvSaleList;
    Button btnSaleShowDelete, btnSaleShowDetails;
    SalesAdapter adapter;
    public static boolean[] checkBoxes;
    int checkIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_show);
        lvSaleList = findViewById(R.id.lvSaleList);
        btnSaleShowDelete = findViewById(R.id.btnSaleShowDelete);
        btnSaleShowDetails = findViewById(R.id.btnSaleShowDetails);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        String whereClause = "userEmail = '" + InventoryApp.user.getEmail() + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("created");

        showProgress(true);
        tvLoad.setText("טוען נתונים, אנא המתן...");

        Backendless.Data.of(Sale.class).find(queryBuilder, new AsyncCallback<List<Sale>>() {
            @Override
            public void handleResponse(List<Sale> response) {
                InventoryApp.sales = response;
                adapter = new SalesAdapter(SaleShowActivity.this, InventoryApp.sales);
                lvSaleList.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(SaleShowActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });


        btnSaleShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPositions().isEmpty()) {
                    Toast.makeText(SaleShowActivity.this, "יש לסמן את הפריט שברצונך לערוך", Toast.LENGTH_SHORT).show();
                } else if (checkPositions().size() > 1) {
                    Toast.makeText(SaleShowActivity.this, "יש לבחור פריט אחד בלבד לעריכה", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SaleShowActivity.this, SaleEditActivity.class);
                    checkIndex = checkPositions().get(0);
                    intent.putExtra("checkIndex", checkIndex);
                    startActivityForResult(intent, 1);
                }
            }
        });

        btnSaleShowDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPositions().isEmpty()) {
                    Toast.makeText(SaleShowActivity.this, "יש לסמן על הפריטים שברצונך למחוק", Toast.LENGTH_SHORT).show();
                } else if (checkPositions().size() > 1) {
                    Toast.makeText(SaleShowActivity.this, "יש לסמן פריט אחד בלבד", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SaleShowActivity.this);
                    alert.setTitle("התראת מחיקה");
                    alert.setMessage("האם אתה בטוח שברצונך למחוק את הנתונים המסומנים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);

                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            showProgress(true);
                            for (final int i : checkPositions()) {
                                checkIndex = i;
                            }

                            Backendless.Persistence.of(Sale.class).remove(InventoryApp.sales.get(checkIndex), new AsyncCallback<Long>() {
                                @Override
                                public void handleResponse(Long response) {
                                    InventoryApp.sales.remove(checkIndex);
                                    showProgress(false);
                                    adapter.notifyDataSetChanged();
                                    checkBoxes[checkIndex] = false;
                                    Toast.makeText(SaleShowActivity.this, "נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(SaleShowActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            }
                    });
                    alert.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            adapter.notifyDataSetChanged();
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public ArrayList<Integer> checkPositions() {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i]) {
                positions.add(i);
            }
        }
        return positions;
    }
}
