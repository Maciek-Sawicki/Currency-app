package com.example.currencyapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final Map<String, LatLng> Capitals = new HashMap<>();
    static {
        Capitals.put("EUR", new LatLng(50.83, 4.33));
        Capitals.put("PLN", new LatLng(52.25, 21.00));
        Capitals.put("GBP", new LatLng(51.50, -0.08));
        Capitals.put("USD", new LatLng(38.90, -77.03));
        Capitals.put("JPY", new LatLng(35.68, 139.69));
        Capitals.put("THB", new LatLng(13.75, 100.50));
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

        LatLng capitalLatLng = Capitals.get(currencyCode);

        if (capitalLatLng == null) {
            capitalLatLng = new LatLng(0, 0);
        }
        mMap.addMarker(new MarkerOptions()
                .position(capitalLatLng)
                .title("Stolica dla " + currencyCode));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(capitalLatLng, 5));
    }
}
