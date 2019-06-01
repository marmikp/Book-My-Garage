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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText location_ed,message;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_activity);

        location_ed = (EditText) findViewById(R.id.location_edt);
        submit = (Button) findViewById(R.id.location_btn);
        message = (EditText) findViewById(R.id.message);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_request();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(22.258652, 71.192383);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
                mMap.clear();

                MarkerOptions mp = new MarkerOptions();

                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

                mp.title("my position");
//                try {
                String location_ext = location.getLatitude() + "," + location.getLongitude();
                Log.d("location", location_ext);
                location_ed.setText(location_ext);
//                }catch (Exception e){

//                }

                mMap.addMarker(mp);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);

            }
        });
    }

    public void send_request() {

        String url = getString(R.string.ip_address) + "send_request.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    Log.d("response: ",response);
                    if (jsonObj.getInt("response")==1){
                        Toast.makeText(RequestActivity.this, "Request Sent!!", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.request), MODE_PRIVATE).edit();
                        editor.putString("json", response);
                        editor.apply();
                        Intent intent = new Intent(RequestActivity.this,Request_Pending.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (jsonObj.getInt("response")==0){
                        Toast.makeText(RequestActivity.this, "Sorry,No Nearest Garage!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RequestActivity.this, "Error Occured!!", Toast.LENGTH_SHORT).show();
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


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("location", location_ed.getText().toString());
                SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
                params.put("username", prefs.getString("username",null));
                params.put("message", message.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(jsObjRequest);


    }


}