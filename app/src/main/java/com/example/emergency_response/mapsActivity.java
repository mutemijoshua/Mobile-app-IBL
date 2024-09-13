package com.example.emergency_response;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class mapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mymap;
    private SearchView mapSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maps);
        mapSearchView = findViewById(R.id.mapsearch);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        /*SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.my_container, mapFragment)
                .commit();
        */
        //implementing the search functionality on the map
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null) {
                    Geocoder geocoder = new Geocoder(mapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mymap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mymap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //implementing the map functionality
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mymap = googleMap;
        LatLng nairobi = new LatLng(-1.286389, 36.817223);
        mymap.moveCamera(CameraUpdateFactory.newLatLng(nairobi));
        MarkerOptions options = new MarkerOptions().position(nairobi).title("Nairobi");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mymap.addMarker(options);
    }
    //initializing the maps menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    // setting up the menu options to the respective map type
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mapNone)
            mymap.setMapType(GoogleMap.MAP_TYPE_NONE);
        if (id == R.id.mapNormal)
            mymap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (id == R.id.mapSatelite)
            mymap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if (id == R.id.mapHybrid)
            mymap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (id == R.id.mapTerrain)
            mymap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        return super.onOptionsItemSelected(item);
    }
}