package com.fiap.guilhermebigois.fun4pets.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.fiap.guilhermebigois.fun4pets.PrincipalActivity;
import com.fiap.guilhermebigois.fun4pets.R;

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = 2000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                
                if (checkSharedPreferences()) {
                    intent = new Intent(SplashActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    
    private boolean checkSharedPreferences() {
        boolean isActive = true;
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        
        if (!preferences.contains("ativo")) {
            isActive = false;
        } else if (!preferences.getBoolean("ativo", false)) {
            isActive = false;
        }
        
        return isActive;
    }
}