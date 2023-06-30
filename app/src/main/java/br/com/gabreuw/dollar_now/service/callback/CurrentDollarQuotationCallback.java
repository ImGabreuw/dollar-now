package br.com.gabreuw.dollar_now.service.callback;

public interface CurrentDollarQuotationCallback {
    void onQuotationReceived(Float quotation, Float variation);

    void onFailure(Throwable t);
}
