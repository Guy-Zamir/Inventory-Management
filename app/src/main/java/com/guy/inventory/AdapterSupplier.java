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
import java.util.List;

public class AdapterSupplier extends ArrayAdapter<Supplier> {
    private Context context;
    private List<Supplier> suppliers;
    private int selectedPosition = -1;

    public AdapterSupplier(Context context, List<Supplier> list) {
        super(context, R.layout.supplier_row_layout, list);
        this.suppliers = list;
        this.context = context;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.supplier_row_layout, parent, false);

        TextView tvSupplierName = convertView.findViewById(R.id.tvSupplierName);
        TextView tvSupplierDetails = convertView.findViewById(R.id.tvSupplierDetails);
        TextView tvSupplierDetailsLocation = convertView.findViewById(R.id.tvSupplierDetailsLocation);
        TextView tvSupplierDetailsPhoneNumber = convertView.findViewById(R.id.tvSupplierDetailsPhoneNumber);
        TextView tvSupplierDetailsInsidePhone = convertView.findViewById(R.id.tvSupplierDetailsInsidePhone);
        TextView tvSupplierDetailsFax = convertView.findViewById(R.id.tvSupplierDetailsFax);
        TextView tvSupplierDetailsWebSite = convertView.findViewById(R.id.tvSupplierDetailsWebSite);

        LinearLayout llSupplierDetails = convertView.findViewById(R.id.llSupplierDetails);

        tvSupplierName.setText(InventoryApp.suppliers.get(position).getName());
        tvSupplierDetailsLocation.setText("כתובת:  " + InventoryApp.suppliers.get(position).getLocation());
        tvSupplierDetailsPhoneNumber.setText("טלפון:  " + InventoryApp.suppliers.get(position).getPhoneNumber());
        tvSupplierDetailsInsidePhone.setText("טלפון פנימי:  " + InventoryApp.suppliers.get(position).getInsidePhone());
        tvSupplierDetailsFax.setText("פקס:  " + InventoryApp.suppliers.get(position).getFax());
        tvSupplierDetailsWebSite.setText("כתובת אתר אינטרנט:  " + InventoryApp.suppliers.get(position).getWebsite());
        tvSupplierDetails.setText("פרטים נוספים:  " + suppliers.get(position).getDetails());

        if (position == selectedPosition) {
            llSupplierDetails.setVisibility(View.VISIBLE);
        } else {
            llSupplierDetails.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}