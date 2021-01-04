package com.apso.markerwithaddress;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsMarkers extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    Geocoder geo;
    TextView txtMarkers;
    Button btn_satellite,btn_normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapsmarkers);
        txtMarkers = (TextView) findViewById(R.id.txtMarkerText);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_satellite = (Button) findViewById(R.id.btn_satellite);
        btn_normal = (Button) findViewById(R.id.btn_normal);
        btn_satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null)
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        btn_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null)
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            geo = new Geocoder(MapsMarkers.this, Locale.getDefault());

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (mMap != null) {
                        mMap.clear();
                    }
                    try {
                        if (geo == null)
                            geo = new Geocoder(MapsMarkers.this, Locale.getDefault());
                        List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (address.size() > 0) {
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Name:" + address.get(0).getCountryName()
                                    + ". Address:" + address.get(0).getAddressLine(0)));
                            txtMarkers.setText("Name:" + address.get(0).getCountryName()
                                    + ". Address:" + address.get(0).getAddressLine(0));
                        }
                    } catch (IOException ex) {
                        if (ex != null)
                            Toast.makeText(MapsMarkers.this, "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (mMap != null) {
                        mMap.clear();
                    }
                    txtMarkers.setText(marker.getTitle().toString() + " Lat:" + marker.getPosition().latitude + " Long:" + marker.getPosition().longitude);
                    return false;
                }
            });
        }

    }
}
