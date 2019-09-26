package com.example.app_video.Frag;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_video.Contact.ItemCategory;
import com.example.app_video.Adapter.ItemCategoryAdapter;
import com.example.app_video.define.Define_Methods;
import com.example.app_video.define.Define_kw;
import com.example.app_video.Interface.Click_Item_CateGory;
import com.example.app_video.define.DefineURL;
import com.example.app_video.Main.StartVideo;
import com.example.app_video.R;
import com.example.app_video.SQLHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FragItemCategory extends Fragment {

    ArrayList<ItemCategory> arrayList;
    ItemCategoryAdapter itemCategoryAdapter;
    RecyclerView recyclerView;
    String url = DefineURL.ITEM_CATEGORY_URL;

    SQLHelper sqlHelper;
    Define_Methods define_methods = new Define_Methods();
    ArrayList<ItemCategory> arrayListSQL ;

    private static final String TAG = "FragItemCategory";

        public static FragItemCategory newInstance() {
        Bundle args = new Bundle();
        FragItemCategory fragment = new FragItemCategory();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragitemcategory,container,false);
            arrayList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rvListItemCategoty);
        new dogetItem(url).execute();
        itemCategoryAdapter = new ItemCategoryAdapter(arrayList);

        itemCategoryAdapter.setItemCategoryAdapter(new Click_Item_CateGory() {
            @Override
            public void onClickItem(ItemCategory itemCategory) {
                sqlHelper = new SQLHelper(getContext());
                arrayListSQL = sqlHelper.getAllItem();
                if (arrayListSQL.isEmpty()==false && define_methods.CHECK(itemCategory.getName(),arrayListSQL)){
                    sqlHelper.deleteItem(itemCategory.getName());
                }
                sqlHelper.insertItem(itemCategory);

                Intent intent =new Intent(getContext(), StartVideo.class);
                intent.putExtra(Define_kw.KW_URL,itemCategory.getUrl());
                intent.putExtra(Define_kw.KW_NAME,itemCategory.getName());
                intent.putExtra(Define_kw.KW_TIME,itemCategory.getDate());
                intent.putExtra(Define_kw.KW_IMG,itemCategory.getDate());
                startActivity(intent);


            }
        });

        recyclerView.setAdapter(itemCategoryAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }
    public  class  dogetItem extends AsyncTask<Void,Void,Void>{
            String newURL;
            String json="";
        public dogetItem(String newURL) {
            this.newURL = newURL;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url =new URL(newURL);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                int character;
                while ((character = inputStream.read()) != -1) {
                    json += (char) character;
                }
                Log.d(TAG, "doInBackground: " + json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String avt = object.getString("avatar");
                    String date_created = object.getString("date_created");
                    String name = object.getString("title");
                    String url = object.getString("file_mp4");
                    arrayList.add(new ItemCategory(avt,name,date_created,url));
                }
                itemCategoryAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
