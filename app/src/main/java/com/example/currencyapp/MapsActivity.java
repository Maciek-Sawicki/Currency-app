package com.example.currencyapp;

import android.content.Intent; // <--- import!
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final Map<String, LatLng> CAPITALS_MAP = new HashMap<>();
    static {
        CAPITALS_MAP.put("EUR", new LatLng(50.8503, 4.3517));   // Bruksela (Belgia) - siedziba UE
        CAPITALS_MAP.put("PLN", new LatLng(52.2297, 21.0122)); // Warszawa (Polska)
        CAPITALS_MAP.put("GBP", new LatLng(51.5074, -0.1278)); // Londyn (Wielka Brytania)
        CAPITALS_MAP.put("CHF", new LatLng(46.9479, 7.4474));  // Berno (Szwajcaria)
        CAPITALS_MAP.put("CZK", new LatLng(50.0755, 14.4378)); // Praga (Czechy)
        CAPITALS_MAP.put("DKK", new LatLng(55.6761, 12.5683)); // Kopenhaga (Dania)
        CAPITALS_MAP.put("HUF", new LatLng(47.4979, 19.0402)); // Budapeszt (Węgry)
        CAPITALS_MAP.put("ISK", new LatLng(64.1466, -21.9426));// Reykjavik (Islandia)
        CAPITALS_MAP.put("NOK", new LatLng(59.9139, 10.7522)); // Oslo (Norwegia)
        CAPITALS_MAP.put("SEK", new LatLng(59.3293, 18.0686)); // Sztokholm (Szwecja)
        CAPITALS_MAP.put("BGN", new LatLng(42.6977, 23.3219)); // Sofia (Bułgaria)
        CAPITALS_MAP.put("RON", new LatLng(44.4268, 26.1025)); // Bukareszt (Rumunia)
        CAPITALS_MAP.put("TRY", new LatLng(39.9208, 32.8541)); // Ankara (Turcja)

        CAPITALS_MAP.put("USD", new LatLng(38.9072, -77.0369)); // Waszyngton (USA)
        CAPITALS_MAP.put("CAD", new LatLng(45.4215, -75.6972)); // Ottawa (Kanada)
        CAPITALS_MAP.put("MXN", new LatLng(19.4326, -99.1332)); // Meksyk (Meksyk)
        CAPITALS_MAP.put("CLP", new LatLng(-33.4489, -70.6693));// Santiago (Chile)
        CAPITALS_MAP.put("BRL", new LatLng(-15.7939, -47.8828));// Brasilia (Brazylia)
        CAPITALS_MAP.put("ZAR", new LatLng(-25.7479, 28.2293)); // Pretoria (RPA, stolica administracyjna)

        CAPITALS_MAP.put("AUD", new LatLng(-35.2820, 149.1286));// Canberra (Australia)
        CAPITALS_MAP.put("NZD", new LatLng(-41.2865, 174.7762));// Wellington (Nowa Zelandia)
        CAPITALS_MAP.put("SGD", new LatLng(1.3521, 103.8198));  // Singapur (miasto-państwo)
        CAPITALS_MAP.put("HKD", new LatLng(22.3193, 114.1694)); // Hongkong (Specjalny Region Adm.)
        CAPITALS_MAP.put("JPY", new LatLng(35.6895, 139.6917)); // Tokio (Japonia)
        CAPITALS_MAP.put("CNY", new LatLng(39.9042, 116.4074)); // Pekin (Chiny)
        CAPITALS_MAP.put("THB", new LatLng(13.7563, 100.5018)); // Bangkok (Tajlandia)
        CAPITALS_MAP.put("MYR", new LatLng(3.1390, 101.6869));  // Kuala Lumpur (Malezja)
        CAPITALS_MAP.put("PHP", new LatLng(14.5995, 120.9842)); // Manila (Filipiny)
        CAPITALS_MAP.put("IDR", new LatLng(-6.2088, 106.8456)); // Dżakarta (Indonezja)
        CAPITALS_MAP.put("INR", new LatLng(28.6139, 77.2090));  // Nowe Delhi (Indie)
        CAPITALS_MAP.put("KRW", new LatLng(37.5665, 126.9780)); // Seul (Korea Południowa)
        CAPITALS_MAP.put("ILS", new LatLng(31.7683, 35.2137));  // Jerozolima (Izrael; uwaga: kwestia polityczna)
    }

    private GoogleMap mMap;
    private String currencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        currencyCode = intent.getStringExtra("currency_code");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng capitalLatLng = CAPITALS_MAP.get(currencyCode);

        if (capitalLatLng == null) {
            capitalLatLng = new LatLng(52.2297, 21.0122);
        }
        mMap.addMarker(new MarkerOptions()
                .position(capitalLatLng)
                .title("Stolica dla " + currencyCode));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(capitalLatLng, 10));
    }
}
