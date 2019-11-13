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

public class AdapterClient extends ArrayAdapter<Client> {
    private Context context;
    private List<Client> clients;
    private int selectedPosition = -1;

    AdapterClient(Context context, List<Client> list) {
        super(context, R.layout.client_row_layout, list);
        this.clients = list;
        this.context = context;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.client_row_layout, parent, false);

        TextView tvClientName = convertView.findViewById(R.id.tvClientName);
        TextView tvClientDetails = convertView.findViewById(R.id.tvClientDetails);
        TextView tvClientDetailsLocation = convertView.findViewById(R.id.tvClientDetailsLocation);
        TextView tvClientDetailsPhoneNumber = convertView.findViewById(R.id.tvClientDetailsPhoneNumber);
        TextView tvClientDetailsInsidePhone = convertView.findViewById(R.id.tvClientDetailsInsidePhone);
        TextView tvClientDetailsFax = convertView.findViewById(R.id.tvClientDetailsFax);
        TextView tvClientDetailsWebSite = convertView.findViewById(R.id.tvClientDetailsWebSite);

        LinearLayout llClientDetails = convertView.findViewById(R.id.llClientDetails);
        tvClientName.setText(InventoryApp.clients.get(position).getName());
        tvClientDetailsLocation.setText("כתובת:  " + InventoryApp.clients.get(position).getLocation());
        tvClientDetailsPhoneNumber.setText("טלפון:  " + InventoryApp.clients.get(position).getPhoneNumber());
        tvClientDetailsInsidePhone.setText("טלפון פנימי:  " + InventoryApp.clients.get(position).getInsidePhone());
        tvClientDetailsFax.setText("פקס:  " + InventoryApp.clients.get(position).getFax());
        tvClientDetailsWebSite.setText("כתובת אתר אינטרנט:  " + InventoryApp.clients.get(position).getWebsite());
        tvClientDetails.setText("פרטים נוספים:  " + clients.get(position).getDetails());

        if (position == selectedPosition) {
            llClientDetails.setVisibility(View.VISIBLE);
        } else {
            llClientDetails.setVisibility(View.GONE);
        }

        return convertView;
    }

    void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }
}
