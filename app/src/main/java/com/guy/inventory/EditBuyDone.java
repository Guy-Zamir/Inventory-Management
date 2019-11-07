package com.guy.inventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.DecimalFormat;

public class EditBuyDone extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    Button btnBuyDoneSubmit;
    EditText etBuyEditWage2, etBuyEditDoneWeight2;
    Switch swBuyEditDoneWeight2;
    double doneWeight, wage;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buy_done);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etBuyEditWage2 = findViewById(R.id.etBuyEditWage2);
        etBuyEditDoneWeight2 = findViewById(R.id.etBuyEditDoneWeight2);
        swBuyEditDoneWeight2 = findViewById(R.id.swBuyEditDoneWeight2);
        btnBuyDoneSubmit = findViewById(R.id.btnBuyDoneSubmit);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("נתוני קניה");
        actionBar.setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("index", 0);

        swBuyEditDoneWeight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swBuyEditDoneWeight2.isChecked()) {
                    swBuyEditDoneWeight2.setText(" אחוז ליטוש  ");
                } else {
                    swBuyEditDoneWeight2.setText("  משקל גמור  ");
                }
            }
        });

        btnBuyDoneSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wage = Double.parseDouble(etBuyEditWage2.getText().toString().trim());
                if (swBuyEditDoneWeight2.isChecked()) {
                    doneWeight = (Double.parseDouble(etBuyEditDoneWeight2.getText().toString().trim())/100)*InventoryApp.buys.get(index).getWeight();
                } else {
                    doneWeight = Double.parseDouble(etBuyEditDoneWeight2.getText().toString().trim());
                }
                if (doneWeight == 0 || wage == 0 || doneWeight > InventoryApp.buys.get(index).getWeight()) {
                    Toast.makeText(EditBuyDone.this, "יש להזין את הנתונים הנכונים", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(EditBuyDone.this);
                    alert.setTitle("שינוי נתונים");
                    alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            InventoryApp.buys.get(index).setDoneWeight(doneWeight);
                            InventoryApp.buys.get(index).setWage(wage);
                            InventoryApp.buys.get(index).setWorkDepreciation(doneWeight / InventoryApp.buys.get(index).getWeight());

                            showProgress(true);
                            Backendless.Persistence.save(InventoryApp.buys.get(index), new AsyncCallback<Buy>() {
                                @Override
                                public void handleResponse(Buy response) {
                                    showProgress(false);
                                    Toast.makeText(EditBuyDone.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finishActivity(1);
                                    EditBuyDone.this.finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(EditBuyDone.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
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
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
