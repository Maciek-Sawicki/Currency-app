package com.example.currencyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyapp.model.ExchangeRatesTable;

import java.util.List;

public class AllRatesAdapter extends RecyclerView.Adapter<AllRatesAdapter.ViewHolder> {

    private final List<ExchangeRatesTable.Rate> rateList;

    public AllRatesAdapter(List<ExchangeRatesTable.Rate> rateList) {
        this.rateList = rateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExchangeRatesTable.Rate rate = rateList.get(position);
        // Ustawiamy tekst w prostym układzie listy
        holder.text1.setText(rate.getCode());       // Kod waluty
        holder.text2.setText(String.valueOf(rate.getMid())); // Kurs (mid)
    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // simple_list_item_2 ma dwa TextView o ID: text1 i text2
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
