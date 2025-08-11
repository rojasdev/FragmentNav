package com.rhix.fragmentnav;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void updateData(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        }

        Product product = productList.get(position);

        ImageView imgProduct = convertView.findViewById(R.id.imgProduct);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);

        tvName.setText(product.getName());
        tvDescription.setText(product.getDescription());
        tvPrice.setText("₱" + product.getPrice());

        if (product.getImage() != null) {
            imgProduct.setImageBitmap(BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length));
        } else {
            imgProduct.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        return convertView;
    }
}
