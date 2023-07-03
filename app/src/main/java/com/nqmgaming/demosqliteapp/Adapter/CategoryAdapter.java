package com.nqmgaming.demosqliteapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nqmgaming.demosqliteapp.DTO.CategoryDTO;
import com.nqmgaming.demosqliteapp.R;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
    // Declare variables
    private final Context context;
    private final ArrayList<CategoryDTO> categoryDTOArrayList;

    // Constructor
    public CategoryAdapter(Context context, ArrayList<CategoryDTO> categoryDTOArrayList) {
        this.context = context;
        this.categoryDTOArrayList = categoryDTOArrayList;
    }

    // Override methods
    @Override
    public int getCount() {
        return categoryDTOArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryDTOArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categoryDTOArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.item_category, parent, false);

        } else {
            row = convertView;
        }

        // Get the current category
        CategoryDTO categoryDTO = categoryDTOArrayList.get(position);

        // Get the TextViews
        TextView txtCatId = row.findViewById(R.id.txtViewID);
        TextView txtCatName = row.findViewById(R.id.txtViewName);

        // Set the TextViews
        txtCatId.setText(String.valueOf(categoryDTO.getId()));
        txtCatName.setText(categoryDTO.getName());

        // Return the row
        return row;
    }
}
