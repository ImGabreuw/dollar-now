package br.com.gabreuw.dollar_now;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.gabreuw.dollar_now.database.DataBaseHelper;
import br.com.gabreuw.dollar_now.entities.DollarQuotation;
import br.com.gabreuw.dollar_now.helper.NumberHelper;
import br.com.gabreuw.dollar_now.service.DollarQuotationService;
import br.com.gabreuw.dollar_now.service.callback.CurrentDollarQuotationCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView cardLabel = findViewById(R.id.cardLabel);

        DataBaseHelper db = new DataBaseHelper(this);
        DollarQuotationService service = new DollarQuotationService(db);

        service.getCurrentDollarQuotation(new CurrentDollarQuotationCallback() {
            @Override
            public void onQuotationReceived(Float quotation) {
                cardLabel.setText(String.format(
                        "%s Real brasileiro",
                        NumberHelper.format(quotation)
                ));
            }

            @Override
            public void onFailure(Throwable t) {
                cardLabel.setText("0,00 Real brasileiro");
                Log.e("Google Finance", "Error during fetch dollar quotation", t);
            }
        });

        List<DollarQuotation> items = db.getAllDollarQuotations();

        RecyclerView recyclerView = findViewById(R.id.previousQuotationsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DollarQuotationAdapter adapter = new DollarQuotationAdapter(items);
        recyclerView.setAdapter(adapter);
    }

}