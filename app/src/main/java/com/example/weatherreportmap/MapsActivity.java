package com.example.weatherreportmap;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherreportmap.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng Narsipatnam = new LatLng(17.671718, 82.612199);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Narsipatnam));
        mMap.setOnMapClickListener(latLng -> {
            Double selectedlat = latLng.latitude;
            Double selectedlon = latLng.longitude;
            getTemperture(selectedlat, selectedlon);
        });
    }
    private void getTemperture(Double selectedlat,Double selectedlon) {
        String URL = "https://api.openweathermap.org/data/2.5/weather?lat=" + selectedlat + "&lon=" + selectedlon + "&units=metric&appid=201ac1b1456985494f5e9f7c1c258d45";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        JSONObject main = response.getJSONObject("main");
                        Object temp = main.getString("temp");
                        Toast.makeText(this, "Temperature: " + temp + " degrees.", Toast.LENGTH_LONG).show();
                    } catch (JSONException ignored) {
                    }
                },
                error -> Log.e("Rest Response", error.toString())
        );
        requestQueue.add(objectRequest);
    }
}