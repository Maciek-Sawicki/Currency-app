package com.example.currencyapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyapp.api.NBPApi;
import com.example.currencyapp.model.ExchangeRatesTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllRatesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private NBPApi nbpApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rates);

        recyclerView = findViewById(R.id.all_rates_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nbp.pl/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nbpApi = retrofit.create(NBPApi.class);

        fetchAllRates();
    }

    private void fetchAllRates() {
        nbpApi.getExchangeRates().enqueue(new Callback<List<ExchangeRatesTable>>() {
            @Override
            public void onResponse(Call<List<ExchangeRatesTable>> call, Response<List<ExchangeRatesTable>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ExchangeRatesTable.Rate> rateList = response.body().get(0).getRates();
                    AllRatesAdapter adapter = new AllRatesAdapter(rateList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ExchangeRatesTable>> call, Throwable t) {
                Log.e("AllRatesActivity", "Error fetching all rates", t);
            }
        });
    }
}
