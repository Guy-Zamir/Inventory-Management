package com.guy.inventory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterSales extends ArrayAdapter<Sale> {
    private Context context;
    private List<Sale> sales;
    private int selectedPosition = -1;

    AdapterSales(Context context, List<Sale> list) {
        super(context, R.layout.sale_row_layout, list);
        this.sales = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DecimalFormat nf = new DecimalFormat("#,###,###,###.##");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.sale_row_layout, parent, false);

        TextView tvSaleClientName = convertView.findViewById(R.id.tvSaleClientName);
        TextView tvSaleDate = convertView.findViewById(R.id.tvSaleDate);
        TextView tvSaleSum = convertView.findViewById(R.id.tvSaleSum);

        TextView tvSaleDetailsPayDate = convertView.findViewById(R.id.tvSaleDetailsPayDate);
        TextView tvSaleDetailsID = convertView.findViewById(R.id.tvSaleDetailsID);
        TextView tvSaleDetailsPrice = convertView.findViewById(R.id.tvSaleDetailsPrice);
        TextView tvSaleDetailsWeight = convertView.findViewById(R.id.tvSaleDetailsWeight);
        TextView tvSaleDetailsDays = convertView.findViewById(R.id.tvSaleDetailsDays);
        TextView tvSaleDetailsNum = convertView.findViewById(R.id.tvSaleDetailsNum);

        LinearLayout llSaleDetails = convertView.findViewById(R.id.llSaleDetails);

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
        tvSaleDate.setText("תאריך מכירה:  " + saleDays + "/" + saleMonth + "/" + saleYear);
        tvSaleSum.setText("סכום עסקה:  " + nf.format(sales.get(position).getSaleSum()) + "$");
        tvSaleDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth+ "/" + payYear);
        tvSaleDetailsID.setText("מספר חשבונית:  " + InventoryApp.sales.get(position).getId());
        tvSaleDetailsPrice.setText("מחיר ממוצע:  " + nf.format(InventoryApp.sales.get(position).getPrice()) + "$");
        tvSaleDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.sales.get(position).getWeight()));
        tvSaleDetailsDays.setText("מספר ימים:  " + nf.format(InventoryApp.sales.get(position).getDays()));
        tvSaleDetailsNum.setText("מספר מכירה במערכת:  " + (position+1));

        if (position == selectedPosition) {
            llSaleDetails.setVisibility(View.VISIBLE);
        } else {
            llSaleDetails.setVisibility(View.GONE);
        }

        return convertView;
    }

    void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}

