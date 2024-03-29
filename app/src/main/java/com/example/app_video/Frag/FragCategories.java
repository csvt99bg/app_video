package com.example.app_video.Frag;

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

import com.example.app_video.Contact.Categories;
import com.example.app_video.Adapter.CategoriesAdapter;
import com.example.app_video.define.Define_kw;
import com.example.app_video.Interface.IClick_Category;
import com.example.app_video.define.DefineURL;
import com.example.app_video.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FragCategories extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Categories> categoriesArrayList;
    CategoriesAdapter categoriesAdapter;
    String url = DefineURL.CATEGORY_URL;

    private static final String TAG = "FragCategories";
    public static FragCategories newInstance() {
        Bundle args = new Bundle();
        FragCategories fragment = new FragCategories();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragcategories, container, false);
        categoriesArrayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rvListCategories);
        new dogetCategory(url).execute();
        categoriesAdapter = new CategoriesAdapter(categoriesArrayList);
        categoriesAdapter.setClickCategory(new IClick_Category() {

            @Override
            public void oncLickCategory(Categories categories) {
                    getFragmentManager().beginTransaction().replace(R.id.container, new FragItemCategory()).commit();
            }
        });
        recyclerView.setAdapter(categoriesAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }
    class dogetCategory extends AsyncTask<Void, Void, Void> {

        String newUrl;
        String json = "";
        public dogetCategory(String newUrl) {
            this.newUrl = newUrl;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(newUrl);
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
                    String avt = object.getString(Define_kw.KW_THUMB);
                    String name = object.getString(Define_kw.KW_TITLE);
                    categoriesArrayList.add(new Categories(avt, name));
                }
                categoriesAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
