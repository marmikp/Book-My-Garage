
package com.marmikbq.charlie.charlieproject271218;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class profile_grg extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    ImageView prof_img,upload_it;
    TextView name1, email, phone1, location1,loc2;
    int i=0;
    SharedPreferences sharedPreferences;
    private static final int PICK_FILE_REQUEST = 1;
    private String selectedFilePath;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_grg);
        sharedPreferences = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);
        prof_img = (ImageView) findViewById(R.id.prof_pic);
        name1 = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone1 = (TextView) findViewById(R.id.phone);
        location1 = (TextView) findViewById(R.id.location);
        loc2 = findViewById(R.id.location_txt_view);
        upload_it = findViewById(R.id.upload_it);



        try {
            setAttributes();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        prof_img.setOnClickListener(this);
        name1.setOnClickListener(this);
        email.setOnClickListener(this);
        phone1.setOnClickListener(this);
        location1.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.name:
                dialogCreate("name", R.id.name);
                break;


            case R.id.email:
                Toast.makeText(this, "Sorry, Email can't be changed!!", Toast.LENGTH_SHORT).show();
                break;


            case R.id.phone:
                dialogCreate("mobile", R.id.phone);
                break;


            case R.id.location:
                i=0;
                dialogForMap();
                break;

            case R.id.prof_pic:
                uploadProfPic(view);
                break;

            case R.id.upload_it:
                uploadProfPic(view);
                upload_it.setVisibility(View.INVISIBLE);
                break;
        }
    }



    public void uploadProfPic(View v) {
        if (v.getId() == R.id.prof_pic) {

            //on attachment icon click
            showFileChooser();


        }
        if (v.getId() == R.id.upload_it) {

            //on upload button Click
            if (selectedFilePath != null) {



                if (!isFinishing())
//                    dialog = ProgressDialog.show(this, "", "Uploading File...", true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //creating new thread to handle Http Operations
                        uploadFile(selectedFilePath);
                    }
                }).start();
            } else {
                Toast.makeText(getApplicationContext(), "Please choose a File First", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    Picasso.get().load(selectedFileUri).into(prof_img);
                    upload_it.setVisibility(View.VISIBLE);
                    upload_it.setOnClickListener(this);

                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //android upload file to server
    public int uploadFile(final String selectedFilePath) {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024 *100;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(profile_grg.this, "File Not Exist", Toast.LENGTH_SHORT).show();
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);
                String user = sharedPreferences.getString("username",null);
                URL url = new URL(getString(R.string.ip_address) + "upload_file.php?user="+user);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("GET");

                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", selectedFilePath);

                /*PrintStream ps = new PrintStream(connection.getOutputStream());
                ps.print("&user=marmik");*/
                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
/*
                dataOutputStream.writeBytes("&user=marmik");*/
                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];


                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.d("hello", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(profile_grg.this, "File Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
//            dialog.dismiss();
            return serverResponseCode;
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    int j = 0;
    public void dialogCreate(final String field_name, final int id) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.text_change);

        // set the custom dialog components - text, image and button
        TextView text = dialog.findViewById(R.id.input_field);
        final EditText value_field = dialog.findViewById(R.id.filed_value_lay);
        text.setText(field_name);
        value_field.setHint("Value");
        Button dialogButton = (Button) dialog.findViewById(R.id.change_submit);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextOnDb(field_name, value_field.getText().toString());
                switch (id) {
                    case R.id.name:
                        name1.setText(value_field.getText());
                        break;


                    case R.id.phone:
                        phone1.setText(value_field.getText());
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    String location_ext;


    Dialog dialog1;
    public void dialogForMap() {
        try {
            MapFragment f = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map_change_loc);
            if (f != null)
                getFragmentManager().beginTransaction().remove(f).commit();
        }catch (Exception e){

        }
        dialog1 = new Dialog(this);
            dialog1.setContentView(R.layout.map_layout_grg);
            j++;



        // set the custom dialog components - text, image and button

        final EditText edit_location = dialog1.findViewById(R.id.edit_location_change);
        Button dialogButton = (Button) dialog1.findViewById(R.id.submit_map);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_change_loc);

        mapFragment.getMapAsync(profile_grg.this);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ltlng = edit_location.getText().toString();
                if (ltlng.equals(null)){
                    changeTextOnDb("location", location_ext);
                    location1.setText(location_ext);
                }
                else{
                    changeTextOnDb("location", ltlng);
                    location1.setText(ltlng);
                }
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }




    @Override
    public void onMapReady(final GoogleMap googleMap) {
        final GoogleMap mMap = googleMap;

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
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);


                mMap.clear();

                MarkerOptions mp = new MarkerOptions();

                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

                mp.title("my position");
                location_ext = location.getLatitude() + "," + location.getLongitude();
                Log.d("location", location_ext);


                mMap.addMarker(mp);
                if (i==0){
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    i++;
                }

            }
        });

        final EditText editText = dialog1.findViewById(R.id.edit_location_change);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
           @Override
           public void onMapLongClick (LatLng latLng){
               Marker marker = null;

               Geocoder geocoder =
                       new Geocoder(profile_grg.this);
               List<Address> list;
               try {
                   list = geocoder.getFromLocation(latLng.latitude,
                           latLng.longitude, 1);
               } catch (IOException e) {
                   return;
               }
               Address address = list.get(0);
               if (marker != null) {
                   marker.remove();
               }

               MarkerOptions options = new MarkerOptions()
                       .title(address.getLocality())
                       .position(new LatLng(latLng.latitude,
                               latLng.longitude));

               marker = mMap.addMarker(options);
               LatLng ltlng = marker.getPosition();
               editText.setText(ltlng.latitude+","+ltlng.longitude);

           }
       });

    }







    public void changeTextOnDb(final String field_name, final String value) {
        String url = getString(R.string.ip_address) + "updateText.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getInt("response") == 1) {
                        Toast.makeText(profile_grg.this, "Entry Updated!!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(profile_grg.this, "No Internet!!", Toast.LENGTH_SHORT).show();
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
                SharedPreferences pref = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);

                params.put("username1", pref.getString("username", null));


                params.put("field", field_name);
                params.put("value", value);


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

    public void setAttributes() throws AuthFailureError {
        String url = getString(R.string.ip_address) + "getReqGrg.php";
        Log.d("url", url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
            Log.d("response1111",response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (!jsonObj.getString("img_name").equals(null)) {
                        try {

                            String img_url = getString(R.string.ip_address) + "profile_pic/" + jsonObj.getString("img_name");
                            Picasso.get().load(img_url).into(prof_img);
                        } catch (Exception e) {
                            Picasso.get().load(R.drawable.profile).into(prof_img);
                        }
                    } else {
                        Picasso.get().load(R.drawable.profile).into(prof_img);
                    }
                    name1.setText(jsonObj.getString("name_clt"));
                    email.setText(jsonObj.getString("email"));
                    phone1.setText(jsonObj.getString("mobile"));
                    if (sharedPreferences.getInt("status",3)==1) {
                        location1.setText(jsonObj.getString("let") + "," + jsonObj.getString("let"));
                    }else{
                        location1.setVisibility(View.INVISIBLE);
                        loc2.setVisibility(View.INVISIBLE);
                    }

                } catch (JSONException e) {
                    Toast.makeText(profile_grg.this, "No Internet!!", Toast.LENGTH_SHORT).show();
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
                SharedPreferences pref = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
                params.put("username1", pref.getString("username", null));
                params.put("req", "0");

                if (sharedPreferences.getInt("status",3) == 1) {
                    params.put("adm", "1");
                }else {
                    params.put("adm","0");
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


}
