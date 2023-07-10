package com.nqmgaming.demosqliteapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nqmgaming.demosqliteapp.DTO.CategoryDTO;
import com.nqmgaming.demosqliteapp.DTO.ProductDTO;
import com.nqmgaming.demosqliteapp.DataBase.DataBaseHelper;

import java.util.ArrayList;

public class ProductDAO {

    // Create object
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;

    // Constructor
    public ProductDAO(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }

    public long AddNewProduct(ProductDTO ObjectPro) {
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("name", ObjectPro.getName());
            values.put("price", ObjectPro.getPrice());
            values.put("category_id", ObjectPro.getCat_id());
            // Insert into table tbl_product
            result = sqLiteDatabase.insert("tbl_product", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public int UpdateProduct(ProductDTO ObjectPro) {
        int result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("name", ObjectPro.getName());
            values.put("price", ObjectPro.getPrice());
            values.put("category_id", ObjectPro.getId());
            // Condition where id = ?
            String[] condition = new String[]{String.valueOf(ObjectPro.getId())};
            // Update table tbl_product
            result = sqLiteDatabase.update("tbl_product", values, "id = ?",
                    condition);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int DeleteProduct(ProductDTO ObjectPro) {
        int result = -1;

        try {
            // Condition where id = ?
            String[] condition = new String[]{String.valueOf(ObjectPro.getId())};
            // Delete table tbl_product
            result = sqLiteDatabase.delete("tbl_product", "id = ?", condition);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<ProductDTO> GetAllProduct() {
        ArrayList<ProductDTO> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM tbl_product";
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setId(cursor.getInt(0));
                    productDTO.setName(cursor.getString(1));
                    productDTO.setPrice(cursor.getDouble(2));
                     productDTO.setCat_id(cursor.getInt(3));
                    list.add(productDTO);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<CategoryDTO> getAllCategories() {
        ArrayList<CategoryDTO> categoryList = new ArrayList<>();

        try {
            String sql = "SELECT * FROM tbl_category";
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setId(cursor.getInt(0));
                    categoryDTO.setName(cursor.getString(1));
                    categoryList.add(categoryDTO);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryList;
    }

    public void close() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    //Delete all products
    public int DeleteAllProduct() {
        int result = -1;

        try {
            // Delete table tbl_product
            result = sqLiteDatabase.delete("tbl_product", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
