package com.example.welcome.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {


    public GoogleMap mMap;
    LatLng click;
    EditText search;
    Button go, satellite, normal, terrian;
    String incompleteurl = "https://googleapis.com/maps/api/geocode/json?address=";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.etsearch);
        go = findViewById(R.id.btngo);
        normal = findViewById(R.id.btnnrmal);
        satellite = findViewById(R.id.btnsatellite);
        terrian = findViewById(R.id.btnterrian);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                   String url=incompleteurl + search.getText().toString();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj1 = new JSONObject(response);
                            JSONArray array1 = obj1.getJSONArray("results");
                            JSONObject obj2 = array1.getJSONObject(0);
                            JSONObject obj3 = obj2.getJSONObject("geometry");
                            JSONObject obj4 = obj3.getJSONObject("location");
                            Double lat = obj4.getDouble("lat");
                            Double lng = obj4.getDouble("lng");
                            LatLng l= new LatLng(lat,lng);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l,15));
                            mMap.addMarker(new MarkerOptions().position(l));
                        } catch (Exception e) {

                        }
                    }
                }

                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(request);
            }

        });





        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override

    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        satellite.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
    });

    // Add a marker in Sydney, Australia, and move the camera.

    LatLng ktm = new LatLng(27.6755, 85.3659);
        mMap.addMarker(new

    MarkerOptions().

    position(ktm).

    title("Marker in bhaktapur"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ktm,10));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()

    {
        @Override
        public void onMapClick (LatLng latLng){
        click = latLng;
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
    }
    });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()

    {
        @Override
        public void onMapLongClick (LatLng latLng){
        PolylineOptions option = new PolylineOptions();
        option.add(click);
        option.add(latLng);
        googleMap.addPolyline(option);
        mMap.addMarker(new MarkerOptions().position(click));
    }
    });


}
}