package com.example.currencyapp.model;

import java.util.List;

public class ExchangeRatesTable {
    private String table;
    private String no;
    private String effectiveDate;
    private List<Rate> rates;

    public String getTable() {
        return table;
    }

    public String getNo() {
        return no;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

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
