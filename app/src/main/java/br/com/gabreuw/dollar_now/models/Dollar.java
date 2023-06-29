package br.com.gabreuw.dollar_now.models;

import java.time.LocalDate;
import java.util.Objects;

public class Dollar {

    private LocalDate date;
    private Float price;
    private Float variation;

    public Dollar(LocalDate date, Float price, Float variation) {
        this.date = date;
        this.price = price;
        this.variation = variation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getVariation() {
        return variation;
    }

    public void setVariation(Float variation) {
        this.variation = variation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dollar that = (Dollar) o;
        return Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate());
    }
}
