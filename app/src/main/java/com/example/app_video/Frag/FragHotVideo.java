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
import com.example.app_video.define.DefineURL;
import com.example.app_video.define.Define_Methods;
import com.example.app_video.define.Define_kw;
import com.example.app_video.Interface.IClickVideo;
import com.example.app_video.Main.StartVideo;
import com.example.app_video.R;
import com.example.app_video.Contact.Video;
import com.example.app_video.Adapter.VideoAdapter;
import com.example.app_video.SQLHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FragHotVideo extends Fragment {
    RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    ArrayList<Video> videoList;
    String url = DefineURL.HOT_VIDEO_URL;

    SQLHelper sqlHelper;
    Define_Methods define_methods = new Define_Methods();
    ArrayList<ItemCategory> arrayListSQL ;

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
        new dogetVideo(url).execute();
        videoAdapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.setClickVideo(new IClickVideo() {
            @Override
            public void onClickVideo(Video video) {
                ItemCategory item = new ItemCategory(video.getAvatar(),video.getNameVideo(),video.getTimeCreat(),video.getUrl());
                sqlHelper = new SQLHelper(getContext());
                arrayListSQL = sqlHelper.getAllItem();

                if (arrayListSQL.isEmpty()==false && define_methods.CHECK(item.getName(),arrayListSQL)){
                        sqlHelper.deleteItem(item.getName());
                }
                sqlHelper.insertItem(item);

                Intent intent = new Intent(getContext(), StartVideo.class);
                String url = video.getUrl();
                intent.putExtra(Define_kw.KW_URL, url);
                intent.putExtra(Define_kw.KW_NAME,video.getNameVideo());
                intent.putExtra(Define_kw.KW_TIME,video.getTimeCreat());
                intent.putExtra(Define_kw.KW_IMG,video.getAvatar());
                startActivity(intent);
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
                videoAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
