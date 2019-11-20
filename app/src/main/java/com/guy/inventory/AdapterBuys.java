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

    AdapterBuys(Context context, List<Buy> list) {
        super(context, R.layout.buy_row_layout, list);
        this.buys = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DecimalFormat numberFormat = new DecimalFormat( "#,###,###,###.##" );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.buy_row_layout, parent, false);

        // Defining the views in the layouts
        TextView tvBuyPrice = convertView.findViewById(R.id.tvBuyPrice);
        TextView tvBuySupplier = convertView.findViewById(R.id.tvBuySupplier);
        TextView tvBuyDate = convertView.findViewById(R.id.tvBuyDate);
        ImageView ivDone = convertView.findViewById(R.id.ivDone);
        ImageView ivPolish = convertView.findViewById(R.id.ivWorkPolish);

        TextView tvBuyDetailsPayDate = convertView.findViewById(R.id.tvBuyDetailsPayDate);
        TextView tvBuyDetailsID = convertView.findViewById(R.id.tvBuyDetailsID);
        TextView tvBuyDetailsWeight = convertView.findViewById(R.id.tvBuyDetailsWeight);
        TextView tvBuyDetailsDays = convertView.findViewById(R.id.tvBuyDetailsDays);
        TextView tvBuyDetailsSum = convertView.findViewById(R.id.tvBuyDetailsSum);
        TextView tvBuyDetailsDoneWeight = convertView.findViewById(R.id.tvBuyDetailsDoneWeight);
        TextView tvBuyDetailsWage = convertView.findViewById(R.id.tvBuyDetailsWage);
        TextView tvBuyDetailsWorkDe = convertView.findViewById(R.id.tvBuyDetailsWorkDe);

        LinearLayout llBuyDetails = convertView.findViewById(R.id.llBuyDetails);

        // Setting the values to the views
        Calendar saleDate = Calendar.getInstance();
        saleDate.setTime(buys.get(position).getBuyDate());
        String buyDays = String.format("%02d", saleDate.get(Calendar.DAY_OF_MONTH));
        String buyMonth = String.format("%02d", saleDate.get(Calendar.MONTH)+1);
        String buyYear = String.format("%02d", saleDate.get(Calendar.YEAR));

        Calendar payDate = Calendar.getInstance();
        payDate.setTime(buys.get(position).getPayDate());
        payDate.setTime(InventoryApp.buys.get(position).getPayDate());
        String payDays = String.format("%02d", payDate.get(Calendar.DAY_OF_MONTH));
        String payMonth = String.format("%02d", payDate.get(Calendar.MONTH) + 1);
        String payYear = String.format("%02d", payDate.get(Calendar.YEAR));

        tvBuySupplier.setText(buys.get(position).getSupplierName());
        tvBuyDate.setText("תאריך קניה: " + buyDays + "/" + buyMonth+ "/" + buyYear);
        tvBuyPrice.setText("מחיר לקראט: " + numberFormat.format(buys.get(position).getPrice()) + "$");
        tvBuyDetailsPayDate.setText("תאריך פקיעה: " + payDays + "/" + payMonth+ "/" + payYear);
        tvBuyDetailsID.setText("מספר אסמכתא: " + InventoryApp.buys.get(position).getId());
        tvBuyDetailsWeight.setText("משקל חבילה: " + numberFormat.format(InventoryApp.buys.get(position).getWeight()) + " קראט ");
        tvBuyDetailsDays.setText("מספר ימים: " + InventoryApp.buys.get(position).getDays());
        tvBuyDetailsSum.setText("סכום עסקה: " + numberFormat.format(InventoryApp.buys.get(position).getSum()) + "$");
        tvBuyDetailsDoneWeight.setText("משקל גמור: " + numberFormat.format(InventoryApp.buys.get(position).getDoneWeight()) + " קראט ");
        tvBuyDetailsWage.setText("שכר עבודה: " + numberFormat.format(InventoryApp.buys.get(position).getWage()) + "$" + " , " +
                numberFormat.format(InventoryApp.buys.get(position).getWage() / InventoryApp.buys.get(position).getPrice() * 100) + "%" + " , " +
                numberFormat.format(InventoryApp.buys.get(position).getWage() * InventoryApp.buys.get(position).getWeight()) + "$");
        tvBuyDetailsWorkDe.setText("אחוז ליטוש: " + numberFormat.format(InventoryApp.buys.get(position).getWorkDepreciation() * 100) + "%");

        ivDone.setImageResource((InventoryApp.buys.get(position).isDone()) ? R.drawable.done1_icon : R.drawable.not_done1_icon);

        ivPolish.setVisibility((InventoryApp.buys.get(position).isPolish()) ? View.VISIBLE: View.INVISIBLE);

        llBuyDetails.setVisibility((position == selectedPosition) ? View.VISIBLE : View.GONE);


        return convertView;
    }

    void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}
