package br.com.gabreuw.dollar_now.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.gabreuw.dollar_now.R;
import br.com.gabreuw.dollar_now.models.Dollar;
import br.com.gabreuw.dollar_now.utils.DateHelper;
import br.com.gabreuw.dollar_now.utils.NumberHelper;

public class DollarAdapter extends RecyclerView.Adapter<DollarAdapter.ViewHolder> {
    private final List<Dollar> items;

    public DollarAdapter(List<Dollar> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dollar_quotation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dollar dollar = items.get(position);

        if (dollar.getVariation() > 0) {
            holder.variationIcon.setImageResource(R.drawable.mdi_arrow_up_circle);
            holder.variation.setTextColor(0xFF66BB6A);
        }

        if (dollar.getVariation() < 0) {
            holder.variationIcon.setImageResource(R.drawable.mdi_arrow_down_circle);
            holder.variation.setTextColor(0xFFF44336);
        }

        holder.date.setText(DateHelper.format(dollar.getDate()));
        holder.quotation.setText(NumberHelper.format(dollar.getPrice()));
        holder.variation.setText(NumberHelper.format(dollar.getVariation()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView variationIcon;
        TextView date;
        TextView quotation;
        TextView variation;


        public ViewHolder(View view) {
            super(view);
            variationIcon = view.findViewById(R.id.variationIcon);
            date = view.findViewById(R.id.date);
            quotation = view.findViewById(R.id.quotation);
            variation = view.findViewById(R.id.traillingSupportingText);
        }
    }

}
