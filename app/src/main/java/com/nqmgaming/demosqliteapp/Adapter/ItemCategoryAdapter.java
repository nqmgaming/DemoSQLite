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

public class ItemCategoryAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<CategoryDTO> categoryList;

    public ItemCategoryAdapter(Context context, ArrayList<CategoryDTO> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }


    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_spinner, parent, false);
        } else {
            view = convertView;
        }
        TextView txtCategoryName = view.findViewById(R.id.nameCategory);
        try {
            CategoryDTO categoryDTO = categoryList.get(position);
            txtCategoryName.setText(categoryDTO.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
