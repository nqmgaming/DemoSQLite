package com.nqmgaming.demosqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nqmgaming.demosqliteapp.Adapter.CategoryAdapter;
import com.nqmgaming.demosqliteapp.DAO.CategoryDAO;
import com.nqmgaming.demosqliteapp.DTO.CategoryDTO;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    //Create variables
    ListView listCategory;
    EditText editCategoryId, editCategoryName;
    Button btnAddCategory, btnEditCategory, btnDeleteCategory;
    CategoryDAO categoryDAO;
    ArrayList<CategoryDTO> listCategoryDTO;
    CategoryAdapter adapter;
    int selectedPosition = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Assign variables to the UI elements
        listCategory = findViewById(R.id.listViewCategory);
        editCategoryId = findViewById(R.id.editTextIdCategory);
        editCategoryName = findViewById(R.id.editTextNameCategory);

        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnEditCategory = findViewById(R.id.btnEditCategory);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);

        //Create a new instance of the CategoryDAO class
        categoryDAO = new CategoryDAO(CategoryActivity.this);
        listCategoryDTO = categoryDAO.GetAllCategory();
        adapter = new CategoryAdapter(CategoryActivity.this, listCategoryDTO);
        listCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //ListView click event
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryDTO cat = listCategoryDTO.get(position);
                String name = cat.getName();
                editCategoryName.setText(name);
                editCategoryId.setText(String.valueOf(cat.getId()));

                // Set the clicked item as checked
                listCategory.setItemChecked(position, true);
                selectedPosition = position;
            }
        });

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editCategoryName.getText().toString();
                if (!name.isEmpty()) {
                    try {
                        CategoryDTO cat = new CategoryDTO();
                        cat.setName(name);
                        long result = categoryDAO.AddNewCategory(cat);
                        if (result > 0) {
                            refreshCatList();
                            editCategoryId.setText("");
                            editCategoryName.setText("");
                            Toast.makeText(CategoryActivity.this, "Add Category Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CategoryActivity.this, "Add Category Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(CategoryActivity.this, "An error occurred while adding category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editCategoryName.setError("Please enter a category name");
                    editCategoryName.requestFocus();
                    return;
                }
            }
        });

        btnEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == ListView.INVALID_POSITION) {
                    Toast.makeText(CategoryActivity.this, "Please select a category to edit", Toast.LENGTH_SHORT).show();
                    return;
                }

                CategoryDTO categoryDTO = listCategoryDTO.get(selectedPosition);
                String name = editCategoryName.getText().toString();
                if (name.trim().isEmpty()) {
                    editCategoryName.setError("Please enter a category name");
                    editCategoryName.requestFocus();
                    return;
                }

                try {
                    categoryDTO.setName(name);
                    long result = categoryDAO.UpdateCategory(categoryDTO);
                    if (result > 0) {
                        refreshCatList();
                        editCategoryId.setText("");
                        editCategoryName.setText("");
                        Toast.makeText(CategoryActivity.this, "Update Category Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CategoryActivity.this, "Update Category Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CategoryActivity.this, "An error occurred while updating category", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == ListView.INVALID_POSITION) {
                    Toast.makeText(CategoryActivity.this, "Please select a category to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                CategoryDTO categoryDTO = listCategoryDTO.get(selectedPosition);
                try {
                    long result = categoryDAO.DeleteCategory(categoryDTO);
                    if (result > 0) {
                        refreshCatList();
                        editCategoryId.setText("");
                        editCategoryName.setText("");
                        Toast.makeText(CategoryActivity.this, "Delete Category Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CategoryActivity.this, "Delete Category Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CategoryActivity.this, "An error occurred while deleting category", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void refreshCatList() {
        listCategoryDTO.clear();
        listCategoryDTO.addAll(categoryDAO.GetAllCategory());
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}