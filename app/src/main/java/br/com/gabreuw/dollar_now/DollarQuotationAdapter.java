package br.com.gabreuw.dollar_now;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import br.com.gabreuw.dollar_now.entities.DollarQuotation;

public class DollarQuotationAdapter extends RecyclerView.Adapter<DollarQuotationAdapter.ViewHolder> {
    private final List<DollarQuotation> items;

    public DollarQuotationAdapter(List<DollarQuotation> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DollarQuotation dollarQuotation = items.get(position);

        if (dollarQuotation.getVariation() > 0) {
            holder.variationIcon.setImageResource(R.drawable.mdi_arrow_up_circle);
            holder.variation.setTextColor(0xFF66BB6A);
        }

        if (dollarQuotation.getVariation() < 0) {
            holder.variationIcon.setImageResource(R.drawable.mdi_arrow_down_circle);
            holder.variation.setTextColor(0xFFF44336);
        }

        holder.quotationDate.setText(
                DateTimeFormatter
                        .ofPattern("dd/MM/yyyy")
                        .format(dollarQuotation.getDate())
        );

        holder.quotation.setText(String.format(
                Locale.US,
                "R$ %.2f",
                dollarQuotation.getPrice()
        ).replace(".", ","));

        holder.variation.setText(
                String.format(
                        Locale.US,
                        "%.2f%%",
                        dollarQuotation.getVariation()
                ).replace(".", ",")
        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView variationIcon;
        TextView quotationDate;
        TextView quotation;
        TextView variation;


        public ViewHolder(View view) {
            super(view);
            variationIcon = view.findViewById(R.id.variationIcon);
            quotationDate = view.findViewById(R.id.quotationDate);
            quotation = view.findViewById(R.id.quotation);
            variation = view.findViewById(R.id.traillingSupportingText);
        }
    }

}
