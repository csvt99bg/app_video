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

import com.example.app_video.Adapter.Categories.Categories;
import com.example.app_video.Adapter.HotVideo.Video;
import com.example.app_video.Adapter.ItemCategory.ItemCategory;
import com.example.app_video.Adapter.ItemCategory.ItemCategoryAdapter;
import com.example.app_video.InterOnClick;
import com.example.app_video.Main.StartVideo;
import com.example.app_video.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FragItemCategory extends Fragment {

    ArrayList<ItemCategory> arrayList;
    ItemCategoryAdapter adapter;
    RecyclerView recyclerView;
    String url = "https://demo5639557.mockable.io/getItemCategory";
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

        adapter = new ItemCategoryAdapter(arrayList);
        adapter.setItemCategoryAdapter(new InterOnClick() {
            @Override
            public void onClickVideo(Video video) {

            }
            @Override
            public void oncLickCategory(Categories categories) {

            }
            @Override
            public void onClickItem(ItemCategory itemCategory) {
                Intent intent =new Intent(getContext(), StartVideo.class);
                intent.putExtra("url",itemCategory.getUrl());
                intent.putExtra("name",itemCategory.getName());
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(adapter);


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
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
