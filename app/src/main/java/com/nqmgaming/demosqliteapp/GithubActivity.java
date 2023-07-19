package com.nqmgaming.demosqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class GithubActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.loadUrl("https://github.com/nqmgaming");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}