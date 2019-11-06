package com.guy.inventory;

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

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterBuys extends ArrayAdapter<Buy> {
    private Context context;
    private List<Buy> buys;
    private int selectedPosition = -1;

    public AdapterBuys(Context context, List<Buy> list) {
        super(context, R.layout.buy_row_layout, list);
        this.buys = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DecimalFormat nf = new DecimalFormat( "#,###,###,###.##" );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.buy_row_layout, parent, false);

        TextView tvBuyPrice = convertView.findViewById(R.id.tvBuyPrice);
        TextView tvBuySupplier = convertView.findViewById(R.id.tvBuySupplier);
        TextView tvBuyDate = convertView.findViewById(R.id.tvBuyDate);
        ImageView ivDone = convertView.findViewById(R.id.ivDone);

        TextView tvBuyDetailsBuyDate = convertView.findViewById(R.id.tvBuyDetailsBuyDate);
        TextView tvBuyDetailsPayDate = convertView.findViewById(R.id.tvBuyDetailsPayDate);
        TextView tvBuyDetailsID = convertView.findViewById(R.id.tvBuyDetailsID);
        TextView tvBuyDetailsPrice = convertView.findViewById(R.id.tvBuyDetailsPrice);
        TextView tvBuyDetailsWeight = convertView.findViewById(R.id.tvBuyDetailsWeight);
        TextView tvBuyDetailsDays = convertView.findViewById(R.id.tvBuyDetailsDays);
        TextView tvBuyDetailsSum = convertView.findViewById(R.id.tvBuyDetailsSum);
        TextView tvBuyDetailsDoneWeight = convertView.findViewById(R.id.tvBuyDetailsDoneWeight);
        TextView tvBuyDetailsWage = convertView.findViewById(R.id.tvBuyDetailsWage);
        TextView tvBuyDetailsWorkDe = convertView.findViewById(R.id.tvBuyDetailsWorkDe);

        LinearLayout llBuyDetails = convertView.findViewById(R.id.llBuyDetails);

        Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(buys.get(position).getBuyDate());
        String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH)+1);

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(buys.get(position).getPayDate());

        tvBuySupplier.setText(buys.get(position).getSupplierName());
        tvBuyDate.setText("תאריך קניה:  " + buyDays + "/" + buyMonth);
        tvBuyPrice.setText("מחיר:  " + nf.format(buys.get(position).getPrice()) + "$");

        payDate.setTime(InventoryApp.buys.get(position).getPayDate());
        @SuppressLint("DefaultLocale") String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
        @SuppressLint("DefaultLocale") String payMonth = String.format("%02d", payDate.get(Calendar.MONTH) + 1);

        tvBuyDetailsBuyDate.setText("תאריך קניה: " + buyDays + "/" + buyMonth);
        tvBuyDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth);

        tvBuyDetailsID.setText("מספר אסמכתא:  " + InventoryApp.buys.get(position).getId());
        tvBuyDetailsPrice.setText("מחיר לקראט:  " + nf.format(InventoryApp.buys.get(position).getPrice()) + "$");
        tvBuyDetailsWeight.setText("משקל חבילה:  " + nf.format(InventoryApp.buys.get(position).getWeight()) + " קראט ");
        tvBuyDetailsDays.setText("מספר ימים:  " + InventoryApp.buys.get(position).getDays());
        tvBuyDetailsSum.setText("סכום עסקה:  " + nf.format(InventoryApp.buys.get(position).getSum()) + "$");
        tvBuyDetailsDoneWeight.setText("משקל גמור:  " + nf.format(InventoryApp.buys.get(position).getDoneWeight()) + " קראט ");
        tvBuyDetailsWage.setText("שכר עבודה:  " + nf.format(InventoryApp.buys.get(position).getWage()) + "$" + " , " +
                nf.format(InventoryApp.buys.get(position).getWage() / InventoryApp.buys.get(position).getPrice() * 100) + "%" + " , " +
                nf.format(InventoryApp.buys.get(position).getWage() * InventoryApp.buys.get(position).getWeight()) + "$");
        tvBuyDetailsWorkDe.setText("אחוז ליטוש:  " + nf.format(InventoryApp.buys.get(position).getWorkDepreciation() * 100) + "%");

        if (InventoryApp.buys.get(position).isDone()) {
            ivDone.setImageResource(R.drawable.done1_icon);
        } else {
            ivDone.setImageResource(R.drawable.not_done1_icon);
        }

        if (position == selectedPosition) {
            llBuyDetails.setVisibility(View.VISIBLE);
        } else {
            llBuyDetails.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}
