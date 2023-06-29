package br.com.gabreuw.dollar_now.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.com.gabreuw.dollar_now.R;
import br.com.gabreuw.dollar_now.adapters.DollarAdapter;
import br.com.gabreuw.dollar_now.database.DollarDbHelper;
import br.com.gabreuw.dollar_now.models.Dollar;
import br.com.gabreuw.dollar_now.service.DollarService;
import br.com.gabreuw.dollar_now.service.callback.CurrentDollarQuotationCallback;
import br.com.gabreuw.dollar_now.utils.NumberHelper;

public class HomeActivity extends AppCompatActivity {

    private List<Dollar> items = new ArrayList<>();

    private DollarDbHelper db;
    private DollarService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        db = new DollarDbHelper(this);
        service = new DollarService();

        fetchDollarPrice();
        loadDollarPriceFromDB();

        RecyclerView recyclerView = findViewById(R.id.previousQuotationsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DollarAdapter adapter = new DollarAdapter(items);
        recyclerView.setAdapter(adapter);

        updateCardDollarPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void fetchDollarPrice() {
        service.getCurrentDollarQuotation(new CurrentDollarQuotationCallback() {
            @SuppressLint("NewApi")
            @Override
            public void onQuotationReceived(Float quotation, Float variation) {
                db.insertOrUpdateDollarQuotation(new Dollar(
                        LocalDate.now(),
                        quotation,
                        variation
                ));
            }

            @Override
            public void onFailure(Throwable t) {
                String message = "ERRO: " + t.getMessage();

                if (t.getMessage() != null &&
                        t.getMessage().contains("Unable to resolve host \"www.google.com\": No address associated with hostname")
                ) {
                    message = "Por favor habilite a internet!";
                }

                Toast.makeText(
                        getApplicationContext(),
                        message,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void loadDollarPriceFromDB() {
        items = db.getAllDollarQuotations();
        items.sort(Comparator.comparing(Dollar::getDate).reversed());
    }

    private void updateCardDollarPrice() {
        if (items.isEmpty()) {
            return;
        }

        TextView quotationTV = findViewById(R.id.dollarPrice);
        quotationTV.setText(String.format(
                "%s Real brasileiro",
                NumberHelper.format(items.get(0).getPrice())
        ));
    }

}