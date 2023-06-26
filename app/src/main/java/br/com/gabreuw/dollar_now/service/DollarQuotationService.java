package br.com.gabreuw.dollar_now.service;

import android.annotation.SuppressLint;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.time.LocalDate;

import br.com.gabreuw.dollar_now.database.DataBaseHelper;
import br.com.gabreuw.dollar_now.entities.DollarQuotation;
import br.com.gabreuw.dollar_now.google_finance.GoogleFinanceAPI;
import br.com.gabreuw.dollar_now.helper.NumberHelper;
import br.com.gabreuw.dollar_now.service.callback.CurrentDollarQuotationCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DollarQuotationService {

    private final GoogleFinanceAPI api;
    private final DataBaseHelper db;

    public DollarQuotationService(DataBaseHelper db) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.google.com/finance/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        api = retrofit.create(GoogleFinanceAPI.class);
        this.db = db;
    }

    @SuppressLint("NewApi")
    public void getCurrentDollarQuotation(CurrentDollarQuotationCallback callback) {
        Call<String> call = api.getCurrentDollarQuotation();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String html = response.body();

                    if (html == null) {
                        call.cancel();
                        return;
                    }

                    Document document = Jsoup.parse(html);
                    Elements quotationElements = document.getElementsByClass("YMlKec fxKbKc");
                    Elements variationElements = document.getElementsByClass("JwB6zf");

                    if (quotationElements.isEmpty() | variationElements.isEmpty()) {
                        call.cancel();
                        return;
                    }

                    Float quotation = NumberHelper.parse(quotationElements.get(0).text());
                    Float variation = NumberHelper.parse(
                            variationElements.get(0).text().replace("%", "")
                    );

                    db.insertOrUpdateDollarQuotation(new DollarQuotation(
                            LocalDate.now(),
                            quotation,
                            variation
                    ));

                    callback.onQuotationReceived(quotation);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
