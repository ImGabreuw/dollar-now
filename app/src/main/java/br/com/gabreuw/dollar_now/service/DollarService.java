package br.com.gabreuw.dollar_now.service;

import android.annotation.SuppressLint;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import br.com.gabreuw.dollar_now.models.Dollar;
import br.com.gabreuw.dollar_now.service.callback.CurrentDollarQuotationCallback;
import br.com.gabreuw.dollar_now.utils.NumberHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DollarService {

    @SuppressLint("NewApi")
    public void getCurrentDollarQuotation(CurrentDollarQuotationCallback callback) {
        Thread thread = new Thread(() -> {
            try {
                Document document = Jsoup.connect("https://br.investing.com/currencies/usd-brl").get();

                Element quotationElement = document
                        .selectXpath("//*[@id=\"__next\"]/div[2]/div/div/div[2]/main/div/div[1]/div[2]/div[1]/span")
                        .first();

                Element variationElement = document
                        .selectXpath("//*[@id=\"__next\"]/div[2]/div/div/div[2]/main/div/div[1]/div[2]/div[1]/div[2]/span[2]")
                        .first();

                if (quotationElement == null | variationElement == null) {
                    Thread.currentThread().interrupt();
                    return;
                }

                Float quotation = NumberHelper.parse(
                        quotationElement
                                .text()
                                .replace(",", ".")
                );
                Float variation = NumberHelper.parse(
                        variationElement
                                .text()
                                .replace(",", ".")
                                .replace("%)", "")
                                .replace("(", "")
                );

                callback.onQuotationReceived(quotation, variation);
            } catch (IOException e) {
                callback.onFailure(e);
            } finally {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
    }
}
