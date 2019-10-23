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
import com.guy.inventory.Classes.Client;
import com.guy.inventory.R;
import java.util.List;

public class ClientAdapter extends ArrayAdapter<Client> {
    private Context context;
    private List<Client> clients;

    public ClientAdapter(Context context, List<Client> list) {
        super(context, R.layout.client_row_layout, list);
        this.clients = list;
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.client_row_layout, parent, false);

        TextView tvClientName = convertView.findViewById(R.id.tvClientName);
        TextView tvClientDetails = convertView.findViewById(R.id.tvClientDetails);
        ImageView ivClientTableHome = convertView.findViewById(R.id.ivClientTableHome);

        tvClientName.setText(clients.get(position).getName());
        tvClientDetails.setText(clients.get(position).getDetails());

        if (clients.get(position).isHome()) {
            ivClientTableHome.setImageResource(R.drawable.home_icon);
        } else {
            ivClientTableHome.setImageResource(R.drawable.export_icon);
        }

        return convertView;
    }
}
