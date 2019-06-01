package com.marmikbq.charlie.charlieproject271218;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class map_grg extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gmap;
    MarkerOptions options = new MarkerOptions();
    ArrayList<String> latlngs = new ArrayList<>();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_grg);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_list);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(final GoogleMap mMap) {
        final String url = getString(R.string.ip_address) + "getAllLocation.php";
        Log.d("url", url);
        if (ActivityCompat.checkSelfPermission(map_grg.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);



        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);


                if (i == 0) {

                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    i++;
                }

            }
        });

        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    Log.d("latlang", response);
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getInt("status") == 1) {
                        for (int i = 0; i < jsonObj.length(); i++) {
                            String str = jsonObj.getString(String.valueOf(i));

                            String[] str1 = str.split(",");

                            double let = Double.parseDouble(str1[0]);
                            double lang = Double.parseDouble(str1[1]);
                            LatLng latLng = new LatLng(let, lang);
                            Log.d("latlang", String.valueOf(let));
                            options.position(latLng);
                            mMap.addMarker(options);


                        }

                        Log.d("latlang","abc");



                    }

                } catch (JSONException e) {
                    Log.d("response: ",e.toString());
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }


        });
        Volley.newRequestQueue(this).add(jsObjRequest);
    }
}
