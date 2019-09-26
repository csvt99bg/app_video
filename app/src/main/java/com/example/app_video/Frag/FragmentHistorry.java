package com.example.app_video.Frag;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_video.Adapter.ItemCategoryAdapter;
import com.example.app_video.Contact.ItemCategory;
import com.example.app_video.define.Define_Methods;
import com.example.app_video.define.Define_kw;
import com.example.app_video.Interface.Click_Item_CateGory;
import com.example.app_video.Main.StartVideo;
import com.example.app_video.R;
import com.example.app_video.SQLHelper;

import java.util.ArrayList;

public class FragmentHistorry extends Fragment {
    ArrayList<ItemCategory> categoryArrayList;
    ArrayList<ItemCategory> arrayList;
    ItemCategoryAdapter itemCategoryAdapter;
    RecyclerView recyclerView;
    SQLHelper sqlHelper;
    Button btDeleteAll;
    Define_Methods define_methods = new Define_Methods();
    public static FragmentHistorry newInstance() {
        Bundle args = new Bundle();
        FragmentHistorry fragment = new FragmentHistorry();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_history,container,false);
        recyclerView = view.findViewById(R.id.rcListHistory);
        btDeleteAll = view.findViewById(R.id.btDeleteAllItemHistory);

        categoryArrayList = new ArrayList<>();
        sqlHelper= new SQLHelper(getContext());
        categoryArrayList= sqlHelper.getAllItem();
        arrayList = new ArrayList<>();

        btDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlHelper.deleteAll();
                categoryArrayList = sqlHelper.getAllItem();
               itemCategoryAdapter = new ItemCategoryAdapter(categoryArrayList);
                recyclerView.setAdapter(itemCategoryAdapter);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);

            }
        });


        int size = categoryArrayList.size();
        for (int i=size-1;i>=0;i--){
            arrayList.add(categoryArrayList.get(i));
        }

        itemCategoryAdapter= new ItemCategoryAdapter(arrayList);
        itemCategoryAdapter.setItemCategoryAdapter(new Click_Item_CateGory() {
            @Override
            public void onClickItem(ItemCategory itemCategory) {
                if (arrayList.isEmpty()==false && define_methods.CHECK(itemCategory.getName(),arrayList) ){
                    sqlHelper.deleteItem(itemCategory.getName());
                }
                sqlHelper.insertItem(itemCategory);
                Intent intent =new Intent(getContext(), StartVideo.class);
                intent.putExtra(Define_kw.KW_URL,itemCategory.getUrl());
                intent.putExtra(Define_kw.KW_NAME,itemCategory.getName());
                intent.putExtra(Define_kw.KW_TIME,itemCategory.getDate());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(itemCategoryAdapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }
}
