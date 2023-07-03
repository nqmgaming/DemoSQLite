package com.nqmgaming.demosqliteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
                    Toast.makeText(EditProductActivity.this, "Vui lòng nhập đúng định dạng giá", Toast.LENGTH_SHORT).show();
                    return;
                } catch (NullPointerException e) {
                    Toast.makeText(EditProductActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    Toast.makeText(EditProductActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (name.isEmpty()) {
                    Toast.makeText(EditProductActivity.this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }
               if (price <= 0) {
                    Toast.makeText(EditProductActivity.this, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (category == null) {
                    Toast.makeText(EditProductActivity.this, "Vui lòng chọn danh mục sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(id);
                productDTO.setName(name);
                productDTO.setPrice(price);
                productDTO.setCat_id(category);
                int result = productDAO.UpdateProduct(productDTO);
                if (result > 0) {
                    Toast.makeText(EditProductActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    // Trở về màn hình danh sách sản phẩm gửi dữ liệu check success để load lại danh sách
                    Intent intent = new Intent(EditProductActivity.this, ProductActivity.class);
                    intent.putExtra("success", true);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditProductActivity.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                }

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