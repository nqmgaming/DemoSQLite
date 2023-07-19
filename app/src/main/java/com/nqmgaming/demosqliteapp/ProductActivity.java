package com.nqmgaming.demosqliteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nqmgaming.demosqliteapp.Adapter.ItemCategoryAdapter;
import com.nqmgaming.demosqliteapp.Adapter.ProductAdapter;
import com.nqmgaming.demosqliteapp.DAO.ProductDAO;
import com.nqmgaming.demosqliteapp.DTO.CategoryDTO;
import com.nqmgaming.demosqliteapp.DTO.ProductDTO;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    ListView listProduct;
    ImageButton btnBack;
    ArrayList<ProductDTO> listProductDTO;
    ProductDAO productDAO;
    ProductAdapter adapter;
    FloatingActionButton fabProduct;
    int selectedPosition = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        listProduct = findViewById(R.id.listViewProduct);
        fabProduct = findViewById(R.id.fabProduct);

        btnBack = findViewById(R.id.backProduct);

        // Khởi tạo ProductDAO
        productDAO = new ProductDAO(this);

        // Lấy danh sách sản phẩm từ cơ sở dữ liệu
        listProductDTO = productDAO.GetAllProduct();
        adapter = new ProductAdapter(this, listProductDTO);
        listProduct.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        fabProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        ProductActivity.this, AddProductActivity.class
                );
                startActivity(intent);
            }
        });

        //Get intent from EditProductActivity
        Intent intentFromEditActivity = getIntent();
        //Get the value of the key "id" from the intent
        Boolean check = intentFromEditActivity
                .getBooleanExtra("success", false);
        if (check) {
            refreshProductList();
        }

        Intent intentFromAddProductActivity = getIntent();
        Boolean checkAdd = intentFromAddProductActivity
                .getBooleanExtra("ok", false);
        if (checkAdd) {
            refreshProductList();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productDAO.close();
    }

    private void refreshProductList() {
        listProductDTO = productDAO.GetAllProduct();

        if (listProductDTO.isEmpty()) {
            return;
        }

        adapter = new ProductAdapter(this, listProductDTO);
        listProduct.setAdapter(adapter);
    }

}
