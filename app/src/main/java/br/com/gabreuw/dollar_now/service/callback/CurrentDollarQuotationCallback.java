package br.com.gabreuw.dollar_now.service.callback;

import java.time.LocalDate;

public interface CurrentDollarQuotationCallback {
    void onQuotationReceived(Float quotation);

    void onFailure(Throwable t);
}
