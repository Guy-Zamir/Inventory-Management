package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Sort;
import com.guy.inventory.Tables.SortInfo;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewSortActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private Switch swSortOpen;
    private EditText etNewSortName, etNewSortShape, etNewSortSize, etNewSortColor, etNewSortClarity,
            etNewSortWeight, etNewSortPrice;

    private boolean open;
    private double weight, price;
    private String name, shape, size, color, clarity;
    private Date theDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sort);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etNewSortName = findViewById(R.id.etNewSortName);
        etNewSortShape = findViewById(R.id.etNewSortShape);
        etNewSortSize = findViewById(R.id.etNewSortSize);
        etNewSortColor = findViewById(R.id.etNewSortColor);
        etNewSortClarity = findViewById(R.id.etNewSortClarity);
        etNewSortWeight = findViewById(R.id.etNewSortWeight);
        etNewSortPrice = findViewById(R.id.etNewSortPrice);
        swSortOpen = findViewById(R.id.swSortOpen);

        Button btnNewSortSubmit = findViewById(R.id.btnNewSortSubmit);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("מיון חדש");
        actionBar.setDisplayHomeAsUpEnabled(true);

        etNewSortWeight.setVisibility(View.GONE);
        etNewSortPrice.setVisibility(View.GONE);

        swSortOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNewSortWeight.setVisibility(swSortOpen.isChecked() ? View.VISIBLE : View.GONE);
                etNewSortPrice.setVisibility(swSortOpen.isChecked() ? View.VISIBLE : View.GONE);
            }
        });

        btnNewSortSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open = swSortOpen.isChecked();

                if (etNewSortName.getText().toString().isEmpty()) {
                    Toast.makeText(NewSortActivity.this, "יש להזין את שם המיון", Toast.LENGTH_SHORT).show();

                } else if (open && (etNewSortWeight.getText().toString().isEmpty() || etNewSortPrice.getText().toString().isEmpty())) {
                    Toast.makeText(NewSortActivity.this, "יש להזין את המחיר והמשקל", Toast.LENGTH_SHORT).show();

                } else {
                    name = etNewSortName.getText().toString().trim();
                    shape = etNewSortShape.getText().toString().isEmpty() ? "null" : etNewSortShape.getText().toString().trim();
                    size = etNewSortSize.getText().toString().isEmpty() ? "null" : etNewSortSize.getText().toString().trim();
                    color = etNewSortColor.getText().toString().isEmpty() ? "null" : etNewSortColor.getText().toString().trim();
                    clarity = etNewSortClarity.getText().toString().isEmpty() ? "null" : etNewSortClarity.getText().toString().trim();
                    price = etNewSortPrice.getText().toString().isEmpty() ? 0.0 : Double.valueOf(etNewSortPrice.getText().toString().trim());
                    weight = etNewSortWeight.getText().toString().isEmpty() ? 0.0 : Double.valueOf(etNewSortWeight.getText().toString().trim());
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DATE, 1);
                    calendar.set(Calendar.MONTH, 0);
                    theDate = open ? new GregorianCalendar(2020, 0, 1).getTime() : Calendar.getInstance().getTime();

                    final Sort sort = new Sort();
                    sort.setName(name);
                    sort.setShape(shape);
                    sort.setSize(size);
                    sort.setColor(color);
                    sort.setClarity(clarity);
                    sort.setPrice(open ? price : 0.0);
                    sort.setWeight(open ? weight : 0.0);
                    sort.setSum(open ? price*weight : 0.0);
                    sort.setSortCount(0);
                    sort.setLast(true);
                    sort.setTheDate(theDate);
                    sort.setUserEmail(InventoryApp.user.getEmail());

                    showProgress(true);
                    Backendless.Persistence.save(sort, new AsyncCallback<Sort>() {
                        @Override
                        public void handleResponse(Sort response) {
                            InventoryApp.sorts.add(sort);
                            if (open) {
                                SortInfo sortInfo = new SortInfo();
                                sortInfo.setFromName("מלאי פתיחה");
                                sortInfo.setToName(response.getName());
                                sortInfo.setToId(response.getObjectId());
                                sortInfo.setSortCount(0);
                                sortInfo.setFromId(null);
                                sortInfo.setPrice(response.getPrice());
                                sortInfo.setWeight(response.getWeight());
                                sortInfo.setSum(response.getSum());
                                sortInfo.setKind("open");
                                sortInfo.setTheDate(Calendar.getInstance().getTime());
                                sortInfo.setUserEmail(InventoryApp.user.getEmail());
                                Backendless.Persistence.save(sortInfo, new AsyncCallback<SortInfo>() {
                                    @Override
                                    public void handleResponse(SortInfo response) {
                                        Toast.makeText(NewSortActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finishActivity(1);
                                        NewSortActivity.this.finish();
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(NewSortActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }
                                });

                            } else {
                                Toast.makeText(NewSortActivity.this, "נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finishActivity(1);
                                NewSortActivity.this.finish();
                                showProgress(false);
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(NewSortActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
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
