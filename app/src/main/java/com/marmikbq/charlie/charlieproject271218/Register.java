package com.marmikbq.charlie.charlieproject271218;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


    EditText Fname,Lname,User,Mobile,Pass,Cpass;
    String fname;
    String lname;
    String user;
    String mobile;
    String pass;
    String cpass;
    Button Register;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Fname = (EditText) findViewById(R.id.fname);
        Lname = (EditText) findViewById(R.id.lname);
        Mobile = (EditText) findViewById(R.id.mobile);
        User = (EditText) findViewById(R.id.username);
        Pass = (EditText) findViewById(R.id.password);
        Cpass= (EditText) findViewById(R.id.confirmpassword);
        rg = (RadioGroup) findViewById(R.id.status_for_admin);
        Register = (Button) findViewById(R.id.regsiter);

        User.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!User.getText().toString().matches("")) {
                    if (checkUsername()){
                        User.setError("Already username exist");
                    }
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = 0;
                id = rg.getCheckedRadioButtonId();

                fname =  Fname.getText().toString();
                lname =  Lname.getText().toString();
                mobile =  Mobile.getText().toString();
                user =  User.getText().toString();
                pass = Pass.getText().toString();
                cpass = Cpass.getText().toString();

                if (id == 0){
                    Toast.makeText(Register.this, "Select any One Category", Toast.LENGTH_SHORT).show();
                }
                else if(fname.matches("")){
                    Fname.setError("Enter First Name");
                }
                else if(lname.matches("")){
                    Lname.setError("Enter Last Name");
                }
                else if(user.matches("")){
                    User.setError("Enter Username");
                }
                else if(!user.contains("@")){
                    User.setError("Enter Vaild Email Id");
                }
                else if(mobile.matches("")){
                    Mobile.setError("Enter Mobile Number");
                }
                else if(mobile.length() < 10){
                    Mobile.setError("Enter Vaild Mobile Number");
                }
                else if(pass.matches("")){
                    Pass.setError("Enter Password");
                }
                else if(cpass.matches("")){
                    Cpass.setError("Enter Confirm Password");
                }
                else if(!pass.equals(cpass)){
                    Cpass.setError("Enter Confirm Password");
                    Pass.setText("");
                    Cpass.setText("");
                }
                else if (checkUsername()){
                    User.setError("Already username exist");
                }
                else {
                    register();
                }

            }
        });

    }

    boolean return_val;
    private boolean checkUsername() {
        String url = getString(R.string.ip_address)+"checkUsername.php";
        Log.d("url",url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("response111",response);
                if (!response.equals("Valid")){
                    return_val = true;
                }
                else{
                    return_val = false;
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }


        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user",User.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(jsObjRequest);
        return return_val;
    }

    private void register(){
        String url = getString(R.string.ip_address)+"register.php";
        Log.d("url",url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    Log.d("respons",response);

                    int status = jsonObj.getInt("status");

                    if(status == 1)
                    {
                        Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Registered Not Successful",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("responese",response);

               //     Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();


                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }


        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("fname", fname);
                params.put("lname", lname);
                params.put("user", user);
                params.put("mobile", mobile);
                params.put("pass", pass);
                int id = rg.getCheckedRadioButtonId();
                if (id == R.id.garage){
                    params.put("status", String.valueOf(1));
                }else if (id == R.id.customer){
                    params.put("status", String.valueOf(0));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(jsObjRequest);

    }

}
