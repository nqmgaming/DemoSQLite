package com.nqmgaming.demosqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnCategory, btnProduct;
    ImageButton btnGithub;

    private Handler handler;
    private Runnable runnable;
    private int backPressedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCategory = findViewById(R.id.btnCategory);
        btnProduct = findViewById(R.id.btnProduct);

        btnGithub = findViewById(R.id.btnGithub);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                backPressedCount = 0;
            }
        };

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });

        btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GithubActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (backPressedCount < 1) {
            backPressedCount++;
            Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show();

            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 2000);
        } else if (backPressedCount == 1) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

}