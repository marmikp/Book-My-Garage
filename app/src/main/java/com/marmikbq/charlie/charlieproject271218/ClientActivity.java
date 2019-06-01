package com.marmikbq.charlie.charlieproject271218;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView request_btn,map_btn,profile_btn,current_req;
    Button logout;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity);
        request_btn = findViewById(R.id.newrequest_btn);
        map_btn = findViewById(R.id.map_btn);
        profile_btn = findViewById(R.id.profile_btn);
        current_req = findViewById(R.id.current_req);
        logout = (Button) findViewById(R.id.logout);


        request_btn.setOnClickListener(this);
        map_btn.setOnClickListener(this);
        profile_btn.setOnClickListener(this);
        current_req.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newrequest_btn:
                intent = new Intent(this,RequestActivity.class);
                startActivity(intent);
                break;

            case R.id.current_req:
                intent = new Intent(this,Request_Pending.class);
                startActivity(intent);
                break;

            case R.id.logout:
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE).edit();
                editor.putString("username", null);
                editor.putInt("status",3);
                editor.apply();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;

            case R.id.map_btn:
                startActivity(new Intent(this,map_grg.class));
                break;


            case R.id.profile_btn:
                startActivity(new Intent(ClientActivity.this,profile_grg.class));
                break;

        }
    }
}
