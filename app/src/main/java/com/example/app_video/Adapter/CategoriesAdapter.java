package com.example.app_video.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_video.Contact.Categories;
import com.example.app_video.Interface.IClick_Category;
import com.example.app_video.R;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    ArrayList<Categories> arrayList;
    Context context;
    IClick_Category clickCategory;

    public void setClickCategory(IClick_Category clickCategory) {
        this.clickCategory = clickCategory;
    }

    public CategoriesAdapter(ArrayList<Categories> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.getcategory, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
        final Categories categories = arrayList.get(position);

        holder.nameCategory.setText(categories.getName());
        Glide.with(context).load(categories.getImgCategory()).into(holder.imgCategory);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCategory.oncLickCategory(categories);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameCategory;
        ImageView imgCategory;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View view) {
            super(view);
            nameCategory = view.findViewById(R.id.tvNameCategories);
            imgCategory = view.findViewById(R.id.imgCategories);
            relativeLayout = view.findViewById(R.id.itemCatecory);
        }
    }
}
