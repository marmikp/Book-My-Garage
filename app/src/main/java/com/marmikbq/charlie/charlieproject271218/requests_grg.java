package com.marmikbq.charlie.charlieproject271218;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class requests_grg extends AppCompatActivity implements View.OnClickListener {

    ImageView img1;
    Button contact, cancel, open_m, completed;
    String username1,g_u;
    JSONObject jsonObj;
    TextView name1, distance,msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests_grg);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
        username1 = prefs.getString("username", null);

        img1 = (ImageView) findViewById(R.id.profile_img_grg);
        contact = (Button) findViewById(R.id.call_client);
        cancel = (Button) findViewById(R.id.cancle_clt);
        open_m = (Button) findViewById(R.id.open_map);
        name1 = (TextView) findViewById(R.id.client_name);
        distance = (TextView) findViewById(R.id.distance_grg);
        msg = (TextView) findViewById(R.id.msg_clt);
        completed = findViewById(R.id.completed);

        contact.setOnClickListener(this);
        open_m.setOnClickListener(this);
        cancel.setOnClickListener(this);
        completed.setOnClickListener(this);

        receive_req();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_client:
                try {
                    make_call();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.open_map:
                try {
                    open_map();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.cancle_clt:
                cancelReq();
                break;

            case R.id.completed:
                completeWork();
                break;

        }
    }

    private void completeWork() {

        final String url = getString(R.string.ip_address) + "work_complete.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("response11",response);
                if (response != null) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (jsonObj.getInt("status") == 1) {
                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.request), MODE_PRIVATE).edit();
                            editor.putString("json", null);
                            editor.apply();
                            Toast.makeText(requests_grg.this, "Asked for Completion!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),GarageAdminActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                params.put("username1", username1);
                params.put("g_u",g_u);
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


    public void cancelReq(){
        final String url = getString(R.string.ip_address) + "cancle_req.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("response11",response);
                if (response != null) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (jsonObj.getInt("status") == 1) {
                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.request), MODE_PRIVATE).edit();
                            editor.putString("json", null);
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(),GarageAdminActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                params.put("username1", username1);
                params.put("g_u",g_u);
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


    public void receive_req() {
        final String url = getString(R.string.ip_address) + "getReqGrg.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("response111",response);
                try {
                    if (!response.equals(null)) {
                        jsonObj = new JSONObject(response);
                        if (jsonObj.getInt("response") == 1) {
                            name1.setText(jsonObj.getString("name_clt"));
                            distance.setText(jsonObj.getString("distance"));
                            msg.setText(jsonObj.getString("msg"));
                            g_u = jsonObj.getString("uname_clt");
                            String url1 = getString(R.string.ip_address) + "profile_pic/" + jsonObj.getString("img_name");
                            try {
                                Picasso.get().load(url1).into(img1);
                            } catch (Exception e) {
                                Picasso.get().load(R.drawable.common_google_signin_btn_icon_dark).into(img1);
                            }
                        }else {
                            Toast.makeText(requests_grg.this, "No Request Found", Toast.LENGTH_SHORT).show();
                            Thread.sleep(2000);
                            Intent intent = new Intent(getApplicationContext(),GarageAdminActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } else {

                    }

                } catch (JSONException e) {
                    Toast.makeText(requests_grg.this, "No Internet!!", Toast.LENGTH_SHORT).show();
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

                params.put("username1", username1);
                params.put("req", String.valueOf(1));

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


    public void open_map() throws JSONException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com").appendPath("maps").appendPath("dir").appendPath("").appendQueryParameter("api", "1")
                .appendQueryParameter("destination", Double.parseDouble(jsonObj.getString("let"))+","+Double.parseDouble(jsonObj.getString("long")));
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void make_call() throws JSONException {
        String number = jsonObj.getString("mobile");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        startActivity(callIntent);

    }
}
