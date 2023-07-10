package com.nqmgaming.demosqliteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;
    private int backPressedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCategory = findViewById(R.id.btnCategory);
        Button btnProduct = findViewById(R.id.btnProduct);
        ImageButton btnGithub = findViewById(R.id.btnGithub);
        SwitchCompat switchChangeLanguage = findViewById(R.id.switchChangeLanguage);

        Drawable vietnameseIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_vietnamese, null);
        Drawable englishIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_english, null);

        if (vietnameseIcon != null && englishIcon != null) {
            int width = englishIcon.getIntrinsicWidth();
            int height = englishIcon.getIntrinsicHeight();

            StateListDrawable thumbDrawable = new StateListDrawable();
            thumbDrawable.addState(new int[]{android.R.attr.state_checked}, vietnameseIcon);
            thumbDrawable.addState(new int[]{}, englishIcon);
            thumbDrawable.setBounds(0, 0, width, height);

            switchChangeLanguage.setThumbDrawable(thumbDrawable);
        } else {
            // Xử lý trường hợp vietnameseIcon hoặc englishIcon == null nếu cần
        }
        Handler handler = new Handler(Looper.getMainLooper());
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

        switchChangeLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setLocale("vi");
                } else {
                    setLocale("en");
                }
                updateTexts(); // Thay thế hàm recreate() bằng hàm updateTexts()
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
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();

        config.setLocale(locale);
        Context context = createConfigurationContext(config);
        getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    private void updateTexts() {
        // Cập nhật văn bản cho các nút và các thành phần giao diện người dùng khác
        Button btnCategory = findViewById(R.id.btnCategory);
        Button btnProduct = findViewById(R.id.btnProduct);
        btnCategory.setText(R.string.category);
        btnProduct.setText(R.string.product);
    }
}
