package com.guy.inventory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guy.inventory.InventoryApp;
import com.guy.inventory.R;
import com.guy.inventory.Tables.Sale;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class SalesAdapter extends ArrayAdapter<Sale> {
    private Context context;
    private List<Sale> sales;
    private int selectedPosition = -1;

    public SalesAdapter(Context context, List<Sale> list) {
        super(context, R.layout.sale_row_layout, list);
        this.sales = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DecimalFormat numberFormat = new DecimalFormat("#,###,###,###.##");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.sale_row_layout, parent, false);

        // Defining the views in the layouts
        TextView tvSaleClientName = convertView.findViewById(R.id.tvSaleClientName);
        TextView tvSaleDate = convertView.findViewById(R.id.tvSaleDate);
        TextView tvSaleSum = convertView.findViewById(R.id.tvSaleSum);

        ImageView ivRough = convertView.findViewById(R.id.ivRough);
        TextView tvSaleDetailsPayDate = convertView.findViewById(R.id.tvSaleDetailsPayDate);
        TextView tvSaleDetailsID = convertView.findViewById(R.id.tvSaleDetailsID);
        TextView tvSaleDetailsPrice = convertView.findViewById(R.id.tvSaleDetailsPrice);
        TextView tvSaleDetailsWeight = convertView.findViewById(R.id.tvSaleDetailsWeight);
        TextView tvSaleDetailsDays = convertView.findViewById(R.id.tvSaleDetailsDays);
        TextView tvSaleDetailsNum = convertView.findViewById(R.id.tvSaleDetailsNum);

        LinearLayout llSaleDetails = convertView.findViewById(R.id.llSaleDetails);

        // Setting the values to the views
        Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(InventoryApp.sales.get(position).getSaleDate());
        String saleDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        String saleMonth = String.format("%02d", saleDate.get(Calendar.MONTH) + 1);
        String saleYear = String.format("%02d", saleDate.get(Calendar.YEAR));

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(InventoryApp.sales.get(position).getPayDate());
        String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
        String payMonth = String.format("%02d", payDate.get(Calendar.MONTH)+1);
        String payYear = String.format("%02d", payDate.get(Calendar.YEAR));

        tvSaleClientName.setText(sales.get(position).getClientName());
        tvSaleDate.setText("תאריך מכירה: " + saleDays + "/" + saleMonth + "/" + saleYear);
        tvSaleSum.setText("סכום עסקה: " + numberFormat.format(sales.get(position).getSaleSum()) + "$");
        tvSaleDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth+ "/" + payYear);
        tvSaleDetailsID.setText("מספר חשבונית: " + InventoryApp.sales.get(position).getId());
        tvSaleDetailsPrice.setText("מחיר ממוצע: " + numberFormat.format(InventoryApp.sales.get(position).getPrice()) + "$");
        tvSaleDetailsWeight.setText("משקל חבילה: " + numberFormat.format(InventoryApp.sales.get(position).getWeight()));
        tvSaleDetailsDays.setText("מספר ימים: " + numberFormat.format(InventoryApp.sales.get(position).getDays()));
        tvSaleDetailsNum.setText("מספר מכירה במערכת: " + (position+1));

        // When the sale is selected from the list
        llSaleDetails.setVisibility((position == selectedPosition) ? View.VISIBLE : View.GONE);
        // When the sale is a rough sale
        ivRough.setVisibility((InventoryApp.sales.get(position).isPolish()) ? View.INVISIBLE : View.VISIBLE);

        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}

