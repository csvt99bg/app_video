package com.example.app_video.Adapter.ItemCategory;

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
import com.example.app_video.InterOnClick;
import com.example.app_video.R;

import java.util.ArrayList;

public class ItemCategoryAdapter extends RecyclerView.Adapter<ItemCategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<ItemCategory> arrayList;
    InterOnClick onClick;

    public ItemCategoryAdapter(ArrayList<ItemCategory> arrayList) {
        this.arrayList = arrayList;
    }

    public void setItemCategoryAdapter(InterOnClick onClick) {
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.itemcategory,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ItemCategory itemCategory = arrayList.get(position);

        holder.name.setText(itemCategory.name);
        holder.date.setText(itemCategory.date);
        Glide.with(context).load(itemCategory.getAvatar()).into(holder.avatar);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickItem(itemCategory);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name,date;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View view) {
            super(view);
            avatar = view.findViewById(R.id.imgItemCategory);
            name = view.findViewById(R.id.tvNameItem);
            date = view.findViewById(R.id.tvDateCreat);
            relativeLayout = view.findViewById(R.id.relativeItem);
        }
    }
}
