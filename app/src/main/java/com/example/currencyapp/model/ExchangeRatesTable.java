package com.example.currencyapp.model;

import java.util.List;

public class ExchangeRatesTable {
    private List<Rate> rates;

    public List<Rate> getRates() {
        return rates;
    }

    public static class Rate {
        private String currency;
        private String code;
        private double mid;

        public String getCurrency() {
            return currency;
        }

        public String getCode() {
            return code;
        }

        public double getMid() {
            return mid;
        }
    }
}
