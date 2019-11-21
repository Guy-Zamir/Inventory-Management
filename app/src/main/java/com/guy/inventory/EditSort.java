package com.guy.inventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class EditSort extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etSortEditShape, etSortEditSize, etSortEditColor, etSortEditClarity;
    TextView tvSortEditName;
    Button btnSortEditSubmit;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sort);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etSortEditShape = findViewById(R.id.etSortEditShape);
        etSortEditSize = findViewById(R.id.etSortEditSize);
        etSortEditColor = findViewById(R.id.etSortEditColor);
        etSortEditClarity = findViewById(R.id.etSortEditClarity);
        tvSortEditName = findViewById(R.id.tvSortEditName);

        btnSortEditSubmit = findViewById(R.id.btnSortEditSubmit);

        index = getIntent().getIntExtra("index", 0);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("עריכת מיון");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvSortEditName.setText(InventoryApp.sorts.get(index).getName());
        etSortEditShape.setText(String.valueOf(InventoryApp.sorts.get(index).getShape()));
        etSortEditSize.setText(String.valueOf(InventoryApp.sorts.get(index).getSize()));
        etSortEditColor.setText(String.valueOf(InventoryApp.sorts.get(index).getColor()));
        etSortEditClarity.setText(String.valueOf(InventoryApp.sorts.get(index).getClarity()));

        btnSortEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String shape = etSortEditShape.getText().toString().trim();
                final String size = etSortEditSize.getText().toString().trim();
                final String color = etSortEditColor.getText().toString().trim();
                final String clarity = etSortEditClarity.getText().toString().trim();

                AlertDialog.Builder alert = new AlertDialog.Builder(EditSort.this);
                alert.setTitle("שינוי נתונים");
                alert.setMessage("האם אתה בטוח שברצונך לשנות את הנתונים?");
                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InventoryApp.sorts.get(index).setShape(shape);
                        InventoryApp.sorts.get(index).setSize(size);
                        InventoryApp.sorts.get(index).setColor(color);
                        InventoryApp.sorts.get(index).setClarity(clarity);

                        showProgress(true);
                        Backendless.Persistence.save(InventoryApp.sorts.get(index), new AsyncCallback<Sort>() {
                            @Override
                            public void handleResponse(Sort response) {
                                Toast.makeText(EditSort.this, "שונה בהצלחה", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finishActivity(1);
                                EditSort.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(EditSort.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alert.show();
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