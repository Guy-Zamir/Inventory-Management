package com.guy.inventory.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Sort;

public class NewSortActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private EditText etNewSortName, etNewSortShape, etNewSortSize, etNewSortColor, etNewSortClarity;
    private String name, shape, size, color, clarity;

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
        Button btnNewSortSubmit = findViewById(R.id.btnNewSortSubmit);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("לקוח חדש");
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnNewSortSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNewSortName.getText().toString().isEmpty()) {
                    Toast.makeText(NewSortActivity.this, "יש להזין את שם המיון", Toast.LENGTH_SHORT).show();
                } else {

                    name = etNewSortName.getText().toString().trim();
                    shape = etNewSortShape.getText().toString().trim();
                    size = etNewSortSize.getText().toString().trim();
                    color = etNewSortColor.getText().toString().trim();
                    clarity = etNewSortClarity.getText().toString().trim();

                    final Sort sort = new Sort();
                    sort.setName(name);
                    sort.setShape(shape);
                    sort.setSize(size);
                    sort.setColor(color);
                    sort.setClarity(clarity);
                    sort.setSum(0.0);
                    sort.setPrice(0.0);
                    sort.setWeight(0.0);
                    sort.setSortCount(0);
                    sort.setUserEmail(InventoryApp.user.getEmail());
                    showProgress(true);
                    Backendless.Persistence.save(sort, new AsyncCallback<Sort>() {
                        @Override
                        public void handleResponse(Sort response) {
                            InventoryApp.sorts.add(sort);
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
