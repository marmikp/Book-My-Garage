package com.marmikbq.charlie.charlieproject271218;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Request_Pending extends AppCompatActivity implements OnMapReadyCallback {

    TextView garage_name,distance_text_view;
    Button call,cancle;
    ImageView profile_img;
    JSONObject jsonObject;
    RequestQueue requestQueue;
    SupportMapFragment mapFragment;
    long let,longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request__pending);
        getRequests();

        garage_name = (TextView) findViewById(R.id.garage_name);
        cancle = (Button) findViewById(R.id.cancle);
        call = (Button) findViewById(R.id.call_garage);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        distance_text_view = findViewById(R.id.distance_text_view);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.garage_client_location);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancle_req();
            }
        });



    }

    private boolean isMyServiceNotRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager)context. getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service already","not running");
                return false;
            }
        }
        Log.i("Service","running");
        return true;
    }

    public void operationExec(){
        setAttributes(jsonObject);
        try {
            set_location();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void getRequests(){
        String url = getString(R.string.ip_address) + "getRequest.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    Log.d("response111",response);
                    if (jsonObj.getInt("response") == 1) {
                        jsonObject = jsonObj;
                        operationExec();
                    }
                    else{
                        Toast.makeText(Request_Pending.this, "No Request Found!!", Toast.LENGTH_SHORT).show();
                        Thread.sleep(2000);
                        Intent intent = new Intent(getApplicationContext(),ClientActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {

                } catch (InterruptedException e) {
                    e.printStackTrace();
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

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);

                params.put("user",sharedPreferences.getString("username",null));
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

    public void set_location() throws InterruptedException {
        String url = getString(R.string.ip_address) + "set_location.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("response_loc",response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getInt("status")==1) {

                        let = jsonObj.getLong("let");
                        longi = jsonObj.getLong("long");

                        mapFragment.getMapAsync(Request_Pending.this);
                    }

                } catch (JSONException e) {

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

                try {
                    params.put("username1", jsonObject.getString("uname"));
                    Log.d("username11",jsonObject.getString("uname"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public void cancle_req(){
        String url = getString(R.string.ip_address) + "cancle_req.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getInt("status")==1) {
                        Intent intent = new Intent(getApplicationContext(),ClientActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                   }

                } catch (JSONException e) {

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

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
               try {


                    params.put("username1", jsonObject.getString("uname"));
                    params.put("g_u",sharedPreferences.getString("username",null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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



    public void dialogCreate() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.request_ask_complete);

        // set the custom dialog components - text, image and button
        final RadioGroup radioGroup = dialog.findViewById(R.id.work_completed_radio);

        Button dialogButton = (Button) dialog.findViewById(R.id.change_submit_work);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.completed_yes) {
                    updateWorkComplete("1");
                    dialog.dismiss();

                }else{
                    updateWorkComplete("0");
                }
            }
        });

        dialog.show();


    }

    private void updateWorkComplete(final String s) {

        String url = getString(R.string.ip_address) + "updateWorkComplete.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response22",response);
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getInt("status")==1) {
                        Toast.makeText(Request_Pending.this, jsonObj.getString("response"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ClientActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {

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

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);

                try {
                    Log.d("response23",s);
                    params.put("username1", jsonObject.getString("uname"));
                    params.put("g_u",sharedPreferences.getString("username",null));
                    params.put("asked",s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


    public void setAttributes(final JSONObject jsonObject2) {
        try {
            garage_name.setText(jsonObject2.getString("nameg"));
            String url = getString(R.string.ip_address) + "profile_pic/" + jsonObject2.getString("img_name");
            distance_text_view.setText(jsonObject2.getString("distance"));
            Log.d("response", url);
            Picasso.get().load(url).into(profile_img);
            Log.d("response222",jsonObject2.toString());
            if (jsonObject2.getString("asked").equals("1")){
                dialogCreate();
            }
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = null;
                    try {
                        String phone = jsonObject2.getString("mobile");
                        callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ActivityCompat.checkSelfPermission(Request_Pending.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }
            });
        } catch (Exception e) {
            Log.d("response", e.toString());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        LatLng sydney = new LatLng(let, longi);
        CameraUpdate center = CameraUpdateFactory.newLatLng(sydney);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Mechanic"));

        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
    }

    @Override
    public void onBackPressed() {
        try {
            requestQueue.cancelAll(true);
        }catch (Exception e){

        }
        super.onBackPressed();
    }
}

