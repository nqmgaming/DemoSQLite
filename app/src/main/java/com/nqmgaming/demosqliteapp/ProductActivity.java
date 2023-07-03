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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nqmgaming.demosqliteapp.Adapter.ItemCategoryAdapter;
import com.nqmgaming.demosqliteapp.Adapter.ProductAdapter;
import com.nqmgaming.demosqliteapp.DAO.ProductDAO;
import com.nqmgaming.demosqliteapp.DTO.CategoryDTO;
import com.nqmgaming.demosqliteapp.DTO.ProductDTO;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    ListView listProduct;
    EditText editProductID, editProductName, editProductPrice;
    Spinner spinnerProductCategory;
    Button btnAddProduct;
    ArrayList<ProductDTO> listProductDTO;
    ProductDAO productDAO;
    ProductAdapter adapter;
    ItemCategoryAdapter itemCategoryAdapter;
    int selectedPosition = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        listProduct = findViewById(R.id.listViewProduct);

        editProductID = findViewById(R.id.editTextIdProduct);
        editProductName = findViewById(R.id.editTextNameProduct);
        editProductPrice = findViewById(R.id.editTextPriceProduct);

        spinnerProductCategory = findViewById(R.id.spinnerCategory);

        btnAddProduct = findViewById(R.id.btnAddProduct);

        // Khởi tạo ProductDAO
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

        // Lấy danh sách sản phẩm từ cơ sở dữ liệu
        listProductDTO = productDAO.GetAllProduct();
        adapter = new ProductAdapter(this, listProductDTO);
        listProduct.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editProductName.getText().toString();
                String priceText = editProductPrice.getText().toString();
                Double price;
                Integer cat_id;
                try {
                    price = Double.parseDouble(priceText);
                    cat_id = ((CategoryDTO) spinnerProductCategory.getSelectedItem()).getId();
                } catch (NumberFormatException e) {
                    Toast.makeText(ProductActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    Toast.makeText(ProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(ProductActivity.this, "Category ID: " + cat_id, Toast.LENGTH_SHORT).show();

                if (name.isEmpty()) {
                    editProductName.setError("Please enter product name");
                    editProductName.requestFocus();
                    return;
                }

                if (cat_id == null) {
                    Toast.makeText(ProductActivity.this, "Please select category", Toast.LENGTH_SHORT).show();
                    spinnerProductCategory.requestFocus();
                    return;
                }

                ProductDTO product = new ProductDTO();
                product.setName(name);
                product.setPrice(price);
                product.setCat_id(cat_id);

                long result = productDAO.AddNewProduct(product);

                if (result > 0) {
                    Toast.makeText(ProductActivity.this, "Add product successfully", Toast.LENGTH_SHORT).show();
                    editProductName.setText("");
                    editProductPrice.setText("");
                    editProductID.setText("");
                    spinnerProductCategory.setSelection(0);
                    refreshProductList();
                } else {
                    Toast.makeText(ProductActivity.this, "Add product failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Get intent from EditProductActivity
        Intent intentFromEditActivity= getIntent();
        //Get the value of the key "id" from the intent
        Boolean check = intentFromEditActivity.getBooleanExtra("success", false);
        if (check) {
            refreshProductList();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productDAO.close();
    }

    private void refreshProductList() {
        // Lấy danh sách sản phẩm từ cơ sở dữ liệu
        listProductDTO = productDAO.GetAllProduct();

        // Kiểm tra xem danh sách danh mục có rỗng không
        if (listProductDTO.isEmpty()) {
            return;
        }

        // Tạo ProductAdapter và gán danh sách sản phẩm
        adapter = new ProductAdapter(this, listProductDTO);

        // Gán adapter cho ListView
        listProduct.setAdapter(adapter);
    }

    private void showEmptyCategoryAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Empty Category");
        builder.setMessage("No categories found. Please create categories first.");

        // Nút "Create Category"
        builder.setPositiveButton("Create Category", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Chuyển sang màn hình CategoryActivity để tạo danh mục
                Intent intent = new Intent(ProductActivity.this, CategoryActivity.class);
                finish();
                startActivity(intent);
            }
        });

        // Nút "Go Back"
        builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Quay lại màn hình chính
                onBackPressed();
            }
        });

        // Hiển thị AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
