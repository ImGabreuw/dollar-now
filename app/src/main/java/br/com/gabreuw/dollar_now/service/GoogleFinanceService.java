package br.com.gabreuw.dollar_now.service;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GoogleFinanceService {

    @GET("quote/USD-BRL")
    Call<String> getCurrentDollarQuotation();

}
