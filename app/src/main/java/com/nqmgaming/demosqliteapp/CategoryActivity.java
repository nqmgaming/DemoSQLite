package com.nqmgaming.demosqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.nqmgaming.demosqliteapp.Adapter.CategoryAdapter;
import com.nqmgaming.demosqliteapp.DAO.CategoryDAO;
import com.nqmgaming.demosqliteapp.DTO.CategoryDTO;

import java.util.ArrayList;

import io.github.cutelibs.cutedialog.CuteDialog;

public class CategoryActivity extends AppCompatActivity {

    //Create variables
    ListView listCategory;
    EditText editCategoryId, editCategoryName;
    ImageButton btnEditCategory, btnDeleteCategory, btnAddCategory, btnBackCategory;
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

        btnBackCategory = findViewById(R.id.backCategory);

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
                            new CuteDialog.withAnimation(CategoryActivity.this)
                                    .setTitle("Add Category Successful")
                                    .setAnimation(R.raw.successfull)
                                    .setPositiveButtonText("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .hideNegativeButton(true)
                                    .show();
                        } else {
                            Toast.makeText(
                                            CategoryActivity.this,
                                            "Add Category Failed",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(
                                        CategoryActivity.this,
                                        "An error occurred while adding category",
                                        Toast.LENGTH_SHORT)
                                .show();
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
                    new CuteDialog.withAnimation(CategoryActivity.this)
                            .setTitle("Please Select Category First")
                            .setPadding(10)
                            .hideCloseIcon(true)
                            .setAnimation(R.raw.error)
                            .setPositiveButtonText("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .hideNegativeButton(true)
                            .show();
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
                        new CuteDialog.withAnimation(CategoryActivity.this)
                                .setTitle("Edit Category Successful")
                                .setAnimation(R.raw.successfull)
                                .setPositiveButtonText("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .hideNegativeButton(true)
                                .show();
                    } else {
                        Toast.makeText(
                                        CategoryActivity.this,
                                        "Update Category Failed",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(
                                    CategoryActivity.this,
                                    "An error occurred while updating category",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
                selectedPosition = ListView.INVALID_POSITION;
            }

        });

        btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == ListView.INVALID_POSITION) {
                    new CuteDialog.withAnimation(CategoryActivity.this)
                            .setTitle("Please Select Category First")
                            .setPadding(10)
                            .hideCloseIcon(true)
                            .setAnimation(R.raw.error)
                            .setPositiveButtonText("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .hideNegativeButton(true)
                            .show();
                    return;
                }
                CategoryDTO categoryDTO = listCategoryDTO.get(selectedPosition);
                new CuteDialog.withAnimation(CategoryActivity.this)
                        .setTitle("Delete Category Successful")
                        .setAnimation(R.raw.successfull)
                        .setPositiveButtonText("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    long result = categoryDAO.DeleteCategory(categoryDTO);
                                    if (result > 0) {
                                        refreshCatList();
                                        editCategoryId.setText("");
                                        editCategoryName.setText("");

                                    } else {
                                        Toast.makeText(
                                                CategoryActivity.this,
                                                "Delete Category Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(
                                                    CategoryActivity.this,
                                                    "An error occurred while deleting category",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        })
                        .setNegativeButtonText("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();

                selectedPosition = -1;
            }
        });

        btnBackCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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