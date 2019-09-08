package com.example.app_video.Frag;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_video.Adapter.Categories.Categories;
import com.example.app_video.Adapter.ItemCategory.ItemCategory;
import com.example.app_video.DefineURL;
import com.example.app_video.InterOnClick;
import com.example.app_video.Main.StartVideo;
import com.example.app_video.Checking_Internet;
import com.example.app_video.R;
import com.example.app_video.Adapter.HotVideo.Video;
import com.example.app_video.Adapter.HotVideo.VideoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FragHotVideo extends Fragment {
    RecyclerView recyclerView;
    VideoAdapter adapter;
    ArrayList<Video> videoList;

    String url = DefineURL.HOT_VIDEO_URL;
    String json;
    Checking_Internet checkingInternet = new Checking_Internet();
    private static final String TAG = "FragHotVideo";

    public static FragHotVideo newInstance() {

        Bundle args = new Bundle();

        FragHotVideo fragment = new FragHotVideo();
        fragment.setArguments(args);
        return fragment;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fraghotvideo, container, false);

        videoList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rvListHotVideo);

        if(checkingInternet.checkConnectInternet(getContext())==false)
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_LONG).show();
        else
            new dogetVideo(url).execute();

        adapter = new VideoAdapter(videoList);

        recyclerView.setAdapter(adapter);
        adapter.setClickVideo(new InterOnClick() {
            @Override
            public void onClickVideo(Video video) {

                Intent intent = new Intent(getContext(), StartVideo.class);
                String url = video.getUrl();

                intent.putExtra("url", url);
                intent.putExtra("name",video.getNameVideo());
                intent.putExtra("time",video.getTimeCreat());

                startActivity(intent);
            }

            @Override
            public void oncLickCategory(Categories categories) {

            }

            @Override
            public void onClickItem(ItemCategory itemCategory) {

            }

        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
    public class dogetVideo extends AsyncTask<Void, Void, Void> {
        String urlnew;
        String json = "";

        public dogetVideo(String urlnew) {
            this.urlnew = urlnew;
        }
        @Override
        protected Void doInBackground(Void... voids) {


            try {
                URL url = new URL(urlnew);
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
                String timePlay = object.getString("price");
                String timeCreat = object.getString("date_created");
                String name = object.getString("title");
                String url = object.getString("file_mp4");
                videoList.add(new Video(avt, "10:00", timeCreat, name, url));
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
