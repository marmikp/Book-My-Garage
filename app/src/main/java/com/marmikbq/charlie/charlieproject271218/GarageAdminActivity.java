package com.marmikbq.charlie.charlieproject271218;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GarageAdminActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView requests,profile_grg_j;
    Button logout;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garage_admin_activity);


        requests = findViewById(R.id.my_request);
        profile_grg_j = findViewById(R.id.profile_btn_grg);
        logout = (Button) findViewById(R.id.logout_grg);

        requests.setOnClickListener(this);
        profile_grg_j.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_request:
                intent = new Intent(this,requests_grg.class);
                startActivity(intent);
                break;

            case R.id.logout_grg:
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE).edit();
                editor.putString("username", null);
                editor.putInt("status",3);
                editor.apply();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;

            case R.id.profile_btn_grg:
                startActivity(new Intent(this,profile_grg.class));
                break;

        }
    }
}
