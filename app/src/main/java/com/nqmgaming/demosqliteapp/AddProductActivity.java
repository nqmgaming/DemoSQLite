package com.nqmgaming.demosqliteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nqmgaming.demosqliteapp.Adapter.ItemCategoryAdapter;
import com.nqmgaming.demosqliteapp.Adapter.ProductAdapter;
import com.nqmgaming.demosqliteapp.DAO.ProductDAO;
import com.nqmgaming.demosqliteapp.DTO.CategoryDTO;
import com.nqmgaming.demosqliteapp.DTO.ProductDTO;

import java.util.ArrayList;

import io.github.cutelibs.cutedialog.CuteDialog;

public class AddProductActivity extends AppCompatActivity {
    EditText editProductID, editProductName, editProductPrice;
    Spinner spinnerProductCategory;
    Button btnAddProduct, btnCancelProduct;
    ImageButton btnBack;
    ArrayList<ProductDTO> listProductDTO;
    ProductDAO productDAO;
    ProductAdapter adapter;
    ItemCategoryAdapter itemCategoryAdapter;
    int selectedPosition = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        editProductID = findViewById(R.id.editTextIdProduct);
        editProductName = findViewById(R.id.editTextNameProduct);
        editProductPrice = findViewById(R.id.editTextPriceProduct);

        spinnerProductCategory = findViewById(R.id.spinnerCategory);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnCancelProduct = findViewById(R.id.btnCancelProduct);

        btnBack = findViewById(R.id.backProduct);

        productDAO = new ProductDAO(this);

        // Lấy danh sách category từ cơ sở dữ liệu
        ArrayList<CategoryDTO> categoryList = productDAO.getAllCategories();
        if (categoryList.isEmpty()) {
            // Hiển thị AlertDialog
            showEmptyCategoryAlertDialog();
            return;
        }

        // Tạo ItemCategoryAdapter và gán danh sách category
        itemCategoryAdapter = new ItemCategoryAdapter(this, categoryList);

        // Gán adapter cho Spinner
        spinnerProductCategory.setAdapter(itemCategoryAdapter);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editProductName.getText().toString();
                String priceText = editProductPrice.getText().toString();
                double price;
                Integer cat_id;
                try {
                    price = Double.parseDouble(priceText);
                    cat_id = ((CategoryDTO) spinnerProductCategory.getSelectedItem()).getId();
                } catch (NumberFormatException e) {
                    Toast.makeText(
                                    AddProductActivity.this,
                                    "Invalid price format",
                                    Toast.LENGTH_SHORT)
                            .show();
                    return;
                } catch (Exception e) {
                    Toast.makeText(
                                    AddProductActivity.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Toast.makeText(
                        AddProductActivity.this,
                        "Category ID: " + cat_id,
                        Toast.LENGTH_SHORT).show();

                if (name.isEmpty()) {
                    editProductName.setError("Please enter product name");
                    editProductName.requestFocus();
                    return;
                }

                if (cat_id == null) {
                    Toast.makeText(AddProductActivity.this,
                            "Please select category",
                            Toast.LENGTH_SHORT).show();
                    spinnerProductCategory.requestFocus();
                    return;
                }

                ProductDTO product = new ProductDTO();
                product.setName(name);
                product.setPrice(price);
                product.setCat_id(cat_id);

                long result = productDAO.AddNewProduct(product);

                if (result > 0) {
                    new CuteDialog.withAnimation(AddProductActivity.this)
                            .setTitle("Add product successfully")
                            .setPadding(10)
                            .hideCloseIcon(true)
                            .setAnimation(R.raw.successfull)
                            .setPositiveButtonText("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                                    intent.putExtra("ok", true);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .hideNegativeButton(true)
                            .show();

                } else {
                    Toast.makeText(AddProductActivity.this,
                            "Add product failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnCancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void showEmptyCategoryAlertDialog() {
        new CuteDialog.withAnimation(AddProductActivity.this)
                .setTitle("Empty Category")
                .setDescription("No categories found. Please create categories first.")
                .setPadding(10)
                .setAnimation(R.raw.error)
                .setNegativeButtonText("Go Back", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .setPositiveButtonText("Create Category", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddProductActivity.this,
                                CategoryActivity.class);
                        finish();
                        startActivity(intent);
                    }
                })
                .show();
    }
}