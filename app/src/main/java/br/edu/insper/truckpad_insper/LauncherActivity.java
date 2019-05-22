package br.edu.insper.truckpad_insper;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        }, 2000);

    }


    private void startApp(){
        Intent intentStart = new Intent(LauncherActivity.this, MainActivity.class);
        startActivity(intentStart);
    }
}
