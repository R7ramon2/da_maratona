package com.dev.da.maratona;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;


public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Intent it = new Intent(InicioActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
            }
        }, 800);
    }
}
