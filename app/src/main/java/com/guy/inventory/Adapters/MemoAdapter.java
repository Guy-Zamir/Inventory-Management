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
import com.guy.inventory.R;
import com.guy.inventory.Tables.Memo;
import java.text.DecimalFormat;
import java.util.List;

public class MemoAdapter extends ArrayAdapter<Memo> {
    private Context context;
    private List<Memo> memos;
    private int selectedPosition = -1;
    private boolean all;

    public MemoAdapter(Context context, List<Memo> list) {
        super(context, R.layout.broker_sort_row_layout, list);
        this.memos = list;
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final DecimalFormat numberFormat = new DecimalFormat( "#,###,###,###.##" );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        convertView = inflater.inflate(R.layout.broker_sort_row_layout, parent, false);

        // Defining the views in the layouts
        TextView tvSortName = convertView.findViewById(R.id.tvSortName);
        TextView tvSortPrice = convertView.findViewById(R.id.tvSortPrice);
        TextView tvSortPriceINV = convertView.findViewById(R.id.tvSortPriceINV);
        TextView tvSortWeight = convertView.findViewById(R.id.tvSortWeight);
        TextView tvSortSum = convertView.findViewById(R.id.tvSortSum);
        TextView tvSortSumINV = convertView.findViewById(R.id.tvSortSumINV);
        ImageView ivMemo = convertView.findViewById(R.id.ivMemo);

        Memo memo = memos.get(position);

        tvSortName.setText(all ? memo.getKind() + " " + memo.getName() + " ל" + memo.getClientName(): memo.getName() + " ל" + memo.getClientName());
        tvSortPrice.setText("מחיר: " + numberFormat.format(memo.getPrice()) + "$");
        tvSortPriceINV.setText("מחיר במלאי: " + numberFormat.format(memo.getPriceINV()) + " $");
        tvSortWeight.setText("משקל: " + numberFormat.format(memo.getWeight()) + " קראט ");
        tvSortSum.setText("סכום: " + numberFormat.format(memo.getSum()) + "$");
        tvSortSumINV.setText("סכום במלאי: " + numberFormat.format(memo.getSumINV()) + "$");

        convertView.setBackgroundResource((position == selectedPosition) ? R.drawable.table_row_selected : R.drawable.table_row);
        ivMemo.setVisibility(View.GONE);
        return convertView;
    }

    public void setSelectedPosition(int pos) {
        selectedPosition = pos;
    }

    public void setAll(boolean all) {
        this.all = all;
    }
}
