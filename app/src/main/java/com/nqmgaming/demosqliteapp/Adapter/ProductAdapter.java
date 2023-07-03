package com.nqmgaming.demosqliteapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nqmgaming.demosqliteapp.DAO.ProductDAO;
import com.nqmgaming.demosqliteapp.DTO.ProductDTO;
import com.nqmgaming.demosqliteapp.EditProductActivity;
import com.nqmgaming.demosqliteapp.ProductActivity;
import com.nqmgaming.demosqliteapp.R;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    ProductDAO productDAO;
    //Create a private final Context and ArrayList<ProductDTO> variables
    private final Context context;
    private ArrayList<ProductDTO> productList = new ArrayList<>();


    //Create a constructor with Context and ArrayList<ProductDTO> parameters
    public ProductAdapter(Context context, ArrayList<ProductDTO> productList) {
        this.context = context;
        this.productList = productList;
    }


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.item_product, parent, false);

        } else {
            row = convertView;
        }

        ProductDTO product = productList.get(position);

        TextView txtProdId = row.findViewById(R.id.textViewIdProduct);
        TextView txtProdName = row.findViewById(R.id.textViewNameProduct);
        TextView txtProdPrice = row.findViewById(R.id.textViewPriceProduct);
        TextView txtProdCatId = row.findViewById(R.id.textViewCategoryProduct);

        Button btnDelete = row.findViewById(R.id.buttonDeleteProduct);
        Button btnEdit = row.findViewById(R.id.buttonEditProduct);

        txtProdId.setText("ID: " + String.valueOf(product.getId()));
        txtProdName.setText("Name: " + product.getName());
        txtProdPrice.setText("Price: " + String.valueOf(product.getPrice()));
        txtProdCatId.setText("Category: " + String.valueOf(product.getCat_id()));



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDTO productDTO = productList.get(position);
                productDAO = new ProductDAO(context);
                try {
                    long result = productDAO.DeleteProduct(productDTO);
                    if (result > 0) {
                        refreshProductList(); // Cập nhật danh sách sản phẩm
                        Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Product not deleted", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error deleting product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ProductDTO productDTO = productList.get(position);
                    String id = String.valueOf(productDTO.getId());
                    String name = productDTO.getName();
                    String price = String.valueOf(productDTO.getPrice());
                    String cat_id = String.valueOf(productDTO.getCat_id());

                    //Intent
                    Intent intent = new Intent(context, EditProductActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("price", price);
                    intent.putExtra("category", cat_id);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return row;
    }

    private void refreshProductList() {
        productList.clear(); // Xóa danh sách sản phẩm hiện tại
        productList.addAll(productDAO.GetAllProduct()); // Lấy danh sách sản phẩm mới từ cơ sở dữ liệu
        notifyDataSetChanged(); // Thông báo cho adapter biết dữ liệu đã thay đổi
    }

}
