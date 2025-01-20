package com.example.currencyapp.api;

import com.example.currencyapp.model.ExchangeRatesTable;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NBPApi {
    @GET("exchangerates/tables/A/?format=json")
    Call<List<ExchangeRatesTable>> getExchangeRates();
}