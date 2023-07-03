package com.nqmgaming.demosqliteapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nqmgaming.demosqliteapp.DTO.CategoryDTO;
import com.nqmgaming.demosqliteapp.DataBase.DataBaseHelper;

import java.util.ArrayList;

public class CategoryDAO {

    DataBaseHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public CategoryDAO(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }

    /*
     * Add new category if return > 0 is success and return < 0 is fail
     * @param ObjectCat
     * @return long
     */

    public long AddNewCategory(CategoryDTO ObjectCat) {
        ContentValues values = new ContentValues();
        values.put("name", ObjectCat.getName());
        // Insert into table tbl_category
        return sqLiteDatabase.insert("tbl_category", null, values);
    }

    /*
     * Update category if return > 0 is success and return < 0 is fail
     * @param ObjectCat
     * @return int
     */

    public int UpdateCategory(CategoryDTO ObjectCat) {
        ContentValues values = new ContentValues();
        values.put("name", ObjectCat.getName());
        // Condition where id = ?
        String[] condition = new String[]{String.valueOf(ObjectCat.getId())};
        // Update table tbl_category
        return sqLiteDatabase.update("tbl_category", values, "id = ?", condition);
    }

    /*
     * Delete category if return > 0 is success and return < 0 is fail
     * @param ObjectCat
     * @return int
     */

    public int DeleteCategory(CategoryDTO ObjectCat) {
        // Condition where id = ?
        String[] condition = new String[]{String.valueOf(ObjectCat.getId())};
        // Delete table tbl_category
        return sqLiteDatabase.delete("tbl_category", "id = ?", condition);
    }

    /*
     * Get all category
     * @return ArrayList<CategoryDTO>
     */

    public ArrayList<CategoryDTO> GetAllCategory() {
        ArrayList<CategoryDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_category";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setId(cursor.getInt(0));
                categoryDTO.setName(cursor.getString(1));
                list.add(categoryDTO);
            } while (cursor.moveToNext());
        }
        return list;
    }
}
