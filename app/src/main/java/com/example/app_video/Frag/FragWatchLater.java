package com.example.app_video.Frag;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_video.Adapter.ItemCategoryAdapter;
import com.example.app_video.Contact.ItemCategory;
import com.example.app_video.Interface.Click_Item_CateGory;
import com.example.app_video.Main.StartVideo;
import com.example.app_video.R;
import com.example.app_video.SQLHelper;
import com.example.app_video.SQLHelperSave;
import com.example.app_video.define.Define_Methods;
import com.example.app_video.define.Define_kw;

import java.util.ArrayList;

public class FragWatchLater extends Fragment {
    RecyclerView recyclerView;
    Button btDeleteAllItem;
    ArrayList<ItemCategory> arrayListSQL;
    ArrayList<ItemCategory> arrayList;
    SQLHelperSave sqlHelperSave;
    SQLHelper sqlHelper;
    ItemCategoryAdapter adapter;
    Define_Methods define_methods = new Define_Methods();

    public static FragWatchLater newInstance() {
        Bundle args = new Bundle();
        FragWatchLater fragment = new FragWatchLater();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_watch_later,container,false);
        recyclerView = view.findViewById(R.id.rcListWatchLater);
        btDeleteAllItem = view.findViewById(R.id.btDeleteAllItemSave);

        arrayListSQL = new ArrayList<>();
        sqlHelperSave = new SQLHelperSave(getContext());
        arrayListSQL = sqlHelperSave.getALLItem();
        arrayList = new ArrayList<>();

        btDeleteAllItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlHelperSave.deleteAll();
                arrayListSQL= sqlHelperSave.getALLItem();
                adapter = new ItemCategoryAdapter(arrayListSQL);
                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);

            }
        });

        int size = arrayListSQL.size();
        for (int i=size-1;i>=0;i--){
            arrayList.add(arrayListSQL.get(i));
        }
        adapter = new ItemCategoryAdapter(arrayList);
        adapter.setItemCategoryAdapter(new Click_Item_CateGory() {
            @Override
            public void onClickItem(ItemCategory itemCategory) {
                final ItemCategory iTemNew = itemCategory;
//                sqlHelper = new SQLHelper(getContext());
//                if (arrayList.isEmpty()==false && define_methods.CHECK(iTemNew.getName(),arrayList) ){
//                    sqlHelper.deleteItem(iTemNew.getName());
//                }
//                sqlHelper.insertItem(iTemNew);
//                Intent intent =new Intent(getContext(), StartVideo.class);
//                intent.putExtra(Define_kw.KW_URL,iTemNew.getUrl());
//                intent.putExtra(Define_kw.KW_NAME,iTemNew.getName());
//                intent.putExtra(Define_kw.KW_TIME,iTemNew.getDate());
//                startActivity(intent);
                PopupMenu popupMenu = new PopupMenu(getContext(),view);
               popupMenu.inflate(R.menu.menu_item);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.mnPlay){
                            if (arrayList.isEmpty()==false && define_methods.CHECK(iTemNew.getName(),arrayList) ){
                                sqlHelperSave.deleteItem(iTemNew.getName());
                            }
                            sqlHelperSave.insertItem(iTemNew);
                           Intent intent =new Intent(getContext(), StartVideo.class);
                          intent.putExtra(Define_kw.KW_URL,iTemNew.getUrl());
                            intent.putExtra(Define_kw.KW_NAME,iTemNew.getName());
                            intent.putExtra(Define_kw.KW_TIME,iTemNew.getDate());
                           startActivity(intent);
                       }
                        if (item.getItemId()==R.id.mnDelete){
                            arrayList.remove( iTemNew);
                           recyclerView.setAdapter(adapter);
                      }
                       return false;
                   }
              });
               popupMenu.show();
            }
        });
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }
}