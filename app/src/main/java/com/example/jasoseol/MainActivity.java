package com.example.jasoseol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {//splash역할
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.parseColor("#ff7f50"));
        }

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 1500);

    }


    private class splashhandler implements Runnable{
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), RecommandActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }
}
