package com.example.currencyapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;


import androidx.appcompat.app.AppCompatActivity;

import com.example.currencyapp.api.NBPApi;
import com.example.currencyapp.model.ExchangeRatesTable;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 15.0f;
    private long lastShakeTime = 0;
    private static final int SHAKE_COOLDOWN_MS = 1000;

    private Spinner spinnerSource;
    private Spinner spinnerTarget;
    private EditText amountInput;
    private Button convertButton;
    private TextView outputResult;

    private Retrofit retrofit;
    private NBPApi nbpApi;

    private Map<String, Double> ratesMap = new HashMap<>();
    private List<String> spinnerItems = new ArrayList<>();
    private String selectedSourceCurrency;
    private String selectedTargetCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerSource = findViewById(R.id.spinner_source_currency);
        spinnerTarget = findViewById(R.id.spinner_target_currency);
        amountInput = findViewById(R.id.amount_input);
        convertButton = findViewById(R.id.convert_button);
        outputResult = findViewById(R.id.output_result);
        Button showMapButton = findViewById(R.id.show_map_button);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nbp.pl/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nbpApi = retrofit.create(NBPApi.class);

        fetchExchangeRates();

        convertButton.setOnClickListener(v -> {
            String amountText = amountInput.getText().toString();
            if (amountText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Wprowadź kwotę!", Toast.LENGTH_SHORT).show();
            } else if (selectedSourceCurrency == null || selectedTargetCurrency == null) {
                Toast.makeText(MainActivity.this, "Wybierz walutę źródłową i docelową!", Toast.LENGTH_SHORT).show();
            } else {
                double amount = Double.parseDouble(amountText);
                double result = convertCurrency(amount, selectedSourceCurrency, selectedTargetCurrency);
                outputResult.setText(String.format("Wynik: %.2f %s", result, selectedTargetCurrency));
            }
        });

        showMapButton.setOnClickListener(v -> {
            String targetCode = selectedTargetCurrency; // np. "EUR"
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("currency_code", targetCode);
            startActivity(intent);
        });

        Button showAllRatesButton = findViewById(R.id.show_all_rates_button);
        showAllRatesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllRatesActivity.class);
            startActivity(intent);
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double magnitude = Math.sqrt(x*x + y*y + z*z);

            if (magnitude > SHAKE_THRESHOLD) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShakeTime > SHAKE_COOLDOWN_MS) {
                    lastShakeTime = currentTime;
                    resetInputs();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void resetInputs() {
        amountInput.setText("");
        outputResult.setText("Wynik konwersji pojawi się tutaj");
        spinnerSource.setSelection(0);
        spinnerTarget.setSelection(0);
        Toast.makeText(this, "Wyczyszczono dane", Toast.LENGTH_SHORT).show();
    }

    private void fetchExchangeRates() {
        nbpApi.getExchangeRates().enqueue(new Callback<List<ExchangeRatesTable>>() {
            @Override
            public void onResponse(Call<List<ExchangeRatesTable>> call, Response<List<ExchangeRatesTable>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ExchangeRatesTable.Rate> rates = response.body().get(0).getRates();
                    for (ExchangeRatesTable.Rate rate : rates) {
                        String code = rate.getCode();
                        String name = rate.getCurrency();
                        double mid = rate.getMid();
                        ratesMap.put(code, mid);

                        String displayText = code + " (" + name + ")";
                        spinnerItems.add(displayText);
                    }

                    // Dodaj też PLN
                    ratesMap.put("PLN", 1.0);
                    spinnerItems.add("PLN (złoty polski)");

                    initSpinners();
                }
            }

            @Override
            public void onFailure(Call<List<ExchangeRatesTable>> call, Throwable t) {
                Log.e("NBP", "Błąd podczas pobierania kursów", t);
                Toast.makeText(MainActivity.this, "Błąd sieci: nie mogę pobrać kursów", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                spinnerItems
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSource.setAdapter(adapter);
        spinnerTarget.setAdapter(adapter);

        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = spinnerItems.get(position);
                selectedSourceCurrency = parseCode(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = spinnerItems.get(position);
                selectedTargetCurrency = parseCode(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private String parseCode(String fullText) {
        int spaceIndex = fullText.indexOf(" ");
        if (spaceIndex > 0) {
            return fullText.substring(0, spaceIndex);
        }
        return fullText;
    }

    private double convertCurrency(double amount, String sourceCode, String targetCode) {
        Double sourceRate = ratesMap.get(sourceCode);
        Double targetRate = ratesMap.get(targetCode);
        if (sourceRate == null || targetRate == null) {
            return 0.0;
        }
        double amountInPLN = amount * sourceRate;
        return amountInPLN / targetRate;
    }

}
