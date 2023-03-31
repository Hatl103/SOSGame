package com.example.sosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SOS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }
    public void play(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void exit(View view){
        this.finishAffinity();
        System.exit(0);
    }
}