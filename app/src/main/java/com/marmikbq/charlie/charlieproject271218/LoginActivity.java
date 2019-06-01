package com.marmikbq.charlie.charlieproject271218;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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


public class LoginActivity extends AppCompatActivity{

    EditText username,password;
    Button submit,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.email_sign_in_button);
        signup = (Button) findViewById(R.id.register);

        signup.setOnClickListener(new OnClickListener(){
           @Override
           public void onClick(View view){
               startActivity(new Intent(LoginActivity.this,Register.class));
           }
        });

        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().matches("")){
                    username.setError("Can't be Empty");
                }
                else if(password.getText().toString().matches("")){
                    password.setError("Can't be Empty");
                }
                loginRequest();
            }
        });

        SharedPreferences preferences = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);
        if (preferences.getString("username",null)!=null){
            if (preferences.getInt("status",3)==1) {
                startActivity(new Intent(this,GarageAdminActivity.class));
                finish();
            }
            else if (preferences.getInt("status",3)==0){
                startActivity(new Intent(this,ClientActivity.class));
                finish();
            }
        }

    }

    public void register(View view)
    {
         Intent i  =new Intent(getApplicationContext(),Register.class);
         startActivity(i);
    }

    private void loginRequest(){
        String url = getString(R.string.ip_address)+"login.php";
        Log.d("url",url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj.getString("response").equals("1")){
                        Intent intent = null;
                        if (jsonObj.getString("status").equals("0")) {
                            intent = new Intent(LoginActivity.this, ClientActivity.class);
                        }
                        else if (jsonObj.getString("status").equals("1")) {
                            intent = new Intent(LoginActivity.this, GarageAdminActivity.class);
                        }
                        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE).edit();
                        editor.putString("username", username.getText().toString());
                        editor.putInt("status",jsonObj.getInt("status"));
                        editor.apply();
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Invalid Login!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "No Internet!!", Toast.LENGTH_SHORT).show();
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

                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());

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

    public void onBackPressed() {
        finish();
    }

}

