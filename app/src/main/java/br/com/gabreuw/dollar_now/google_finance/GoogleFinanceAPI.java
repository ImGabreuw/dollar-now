package br.com.gabreuw.dollar_now.google_finance;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GoogleFinanceAPI {

    @GET("quote/USD-BRL")
    Call<String> getCurrentDollarQuotation();

}
