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

public class AdapterExports extends ArrayAdapter<Export> {
    private Context context;
    private List<Export> exports;
    private int selectedPosition = -1;

    public AdapterExports(Context context, List<Export> list) {
        super(context, R.layout.sale_row_layout, list);
        this.exports = list;
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

        LinearLayout llSaleDetails = convertView.findViewById(R.id.llSaleDetails);

        Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(exports.get(position).getSaleDate());
        saleDate.setTime(InventoryApp.exports.get(position).getSaleDate());
        String saleDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        String saleMonth = String.format("%02d", saleDate.get(Calendar.MONTH) + 1);
        String saleYear = String.format("%02d", saleDate.get(Calendar.YEAR));

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(InventoryApp.exports.get(position).getPayDate());
        String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
        String payMonth = String.format("%02d", payDate.get(Calendar.MONTH)+1);
        String payYear = String.format("%02d", payDate.get(Calendar.YEAR));

        tvSaleClientName.setText(exports.get(position).getClientName());
        tvSaleDate.setText("תאריך מכירה:  " + saleDays + "/" + saleMonth + "/" + saleYear);
        tvSaleSum.setText("סכום עסקה:  " + nf.format(exports.get(position).getSaleSum()) + "$");
        tvSaleDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth+ "/" + payYear);
        tvSaleDetailsID.setText("מספר חשבונית:  " + InventoryApp.exports.get(position).getId());
        tvSaleDetailsPrice.setText("מחיר ממוצע:  " + nf.format(InventoryApp.exports.get(position).getPrice()) + "$");
        tvSaleDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.exports.get(position).getWeight()));
        tvSaleDetailsDays.setText("מספר ימים:  " + nf.format(InventoryApp.exports.get(position).getDays()));

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

