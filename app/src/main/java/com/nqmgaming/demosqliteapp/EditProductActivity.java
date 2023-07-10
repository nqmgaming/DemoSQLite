package com.nqmgaming.demosqliteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nqmgaming.demosqliteapp.Adapter.ItemCategoryAdapter;
import com.nqmgaming.demosqliteapp.DAO.ProductDAO;
import com.nqmgaming.demosqliteapp.DTO.CategoryDTO;
import com.nqmgaming.demosqliteapp.DTO.ProductDTO;

import java.util.ArrayList;

public class EditProductActivity extends AppCompatActivity {

    EditText editProductID, editProductName, editProductPrice;
    AppCompatSpinner spinnerProductCategory;
    Button btnCanCel, btnSave;
    ImageButton btnBack;
    ProductDAO productDAO;
    ItemCategoryAdapter itemCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editProductID = findViewById(R.id.editTextEditIdProduct);
        editProductName = findViewById(R.id.editTextEditNameProduct);
        editProductPrice = findViewById(R.id.editTextEditPriceProduct);

        spinnerProductCategory = findViewById(R.id.spinnerEditCategory);

        btnCanCel = findViewById(R.id.btnCancelProduct);
        btnSave = findViewById(R.id.btnSubmitProduct);

        btnBack = findViewById(R.id.backEditProduct);

        productDAO = new ProductDAO(this);
        ArrayList<CategoryDTO> categoryList = productDAO.getAllCategories();
        itemCategoryAdapter = new ItemCategoryAdapter(this, categoryList);
        spinnerProductCategory.setAdapter(itemCategoryAdapter);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String category = intent.getStringExtra("category");

        editProductID.setText(id);
        editProductName.setText(name);
        editProductPrice.setText(price);
        int categoryId = Integer.parseInt(category);
        int spinnerSelection = getSpinnerSelection(categoryId);
        spinnerProductCategory.setSelection(spinnerSelection);

        btnCanCel.setOnClickListener(v -> {
            finish();
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = Integer.parseInt(editProductID.getText().toString());
                String name = editProductName.getText().toString();
                Double price;
                Integer category;
                try {
                    price = Double.parseDouble(editProductPrice.getText().toString());
                    category = ((CategoryDTO) spinnerProductCategory.getSelectedItem()).getId();
                } catch (NumberFormatException e) {
                    Toast.makeText(
                                    EditProductActivity.this,
                                    "Please enter the correct price format",
                                    Toast.LENGTH_SHORT)
                            .show();
                    return;
                } catch (NullPointerException e) {
                    Toast.makeText(
                                    EditProductActivity.this,
                                    "Please enter complete information",
                                    Toast.LENGTH_SHORT).
                            show();
                    return;
                } catch (Exception e) {
                    Toast.makeText(
                            EditProductActivity.this,
                            "Undefined error",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (name.isEmpty()) {
                    Toast.makeText(
                                    EditProductActivity.this,
                                    "Please enter the product name",
                                    Toast.LENGTH_SHORT).
                            show();
                    return;
                }
                if (price <= 0) {
                    Toast.makeText(
                                    EditProductActivity.this,
                                    "Please enter the product price",
                                    Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (category == null) {
                    Toast.makeText(
                                    EditProductActivity.this,
                                    "Please select the product category",
                                    Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(id);
                productDTO.setName(name);
                productDTO.setPrice(price);
                productDTO.setCat_id(category);
                int result = productDAO.UpdateProduct(productDTO);
                if (result > 0) {
                    Toast.makeText(
                                    EditProductActivity.this,
                                    "Edit successful", Toast.LENGTH_SHORT)
                            .show();
                    // Return to the product list screen and send a success message to reload the list
                    Intent intent = new Intent(
                            EditProductActivity.this,
                            ProductActivity.class);
                    intent.putExtra("success", true);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(
                                    EditProductActivity.this,
                                    "Edit failed", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private int getSpinnerSelection(int categoryId) {
        for (int i = 0; i < itemCategoryAdapter.getCount(); i++) {
            Object item = itemCategoryAdapter.getItem(i);
            if (item instanceof CategoryDTO) {
                CategoryDTO category = (CategoryDTO) item;
                if (category.getId() == categoryId) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}