package com.guy.inventory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guy.inventory.Classes.Supplier;
import com.guy.inventory.R;
import java.util.List;

public class SupplierAdapter extends ArrayAdapter<Supplier> {
    private Context context;
    private List<Supplier> suppliers;

    public SupplierAdapter(Context context, List<Supplier> list) {
        super(context, R.layout.supplier_row_layout, list);
        this.suppliers = list;
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.supplier_row_layout, parent, false);

        TextView tvSupplierName = convertView.findViewById(R.id.tvSupplierName);
        TextView tvSupplierDetails = convertView.findViewById(R.id.tvSupplierDetails);
        ImageView ivSupplierTableHome = convertView.findViewById(R.id.ivSupplierTableHome);

        tvSupplierName.setText(suppliers.get(position).getName());
        tvSupplierDetails.setText(suppliers.get(position).getDetails());

        if (suppliers.get(position).isHome()) {
            ivSupplierTableHome.setImageResource(R.drawable.home_icon);
        } else {
            ivSupplierTableHome.setImageResource(R.drawable.export_icon);
        }
        return convertView;
    }
}