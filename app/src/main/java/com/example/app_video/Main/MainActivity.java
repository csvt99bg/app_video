package com.example.app_video.Main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.app_video.Adapter.Categories.Categories;
import com.example.app_video.Adapter.HotVideo.Video;
import com.example.app_video.Adapter.ItemCategory.ItemCategory;
import com.example.app_video.Frag.FragCategories;
import com.example.app_video.Frag.FragHotVideo;
import com.example.app_video.Frag.FragItemCategory;
import com.example.app_video.InterOnClick;
import com.example.app_video.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements InterOnClick {
        ArrayList<Video> videoList;
        Button btFraghotVideo,btFragCategories;
        ImageView imgMenu;
        Video clickVideo;
        Categories clickCategories;
        ArrayList<Categories> categoriesArrayList;
        ProgressBar progressBar;
        LinearLayout lnProgess;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btFragCategories = findViewById(R.id.btFragCategories);
        btFraghotVideo = findViewById(R.id.btFraghotVideo);
        imgMenu = findViewById(R.id.imgMenu);

        progressBar = findViewById(R.id.progessBar);



        ActionBar sc = getSupportActionBar();
        sc.hide();


        videoList = new ArrayList<>();
        getFragment(FragHotVideo.newInstance());


        btFraghotVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragment(FragHotVideo.newInstance());
            }
        });
        btFragCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragment(FragCategories.newInstance());
            }
        });

    }

    public void getFragment (Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }

    }
    @Override
    public void onClickVideo(Video video) {
        clickVideo = video;
    }

    @Override
    public void oncLickCategory(Categories categories) {
             clickCategories = categories;

    }
    @Override
    public void onClickItem(ItemCategory itemCategory) {
            ItemCategory item = itemCategory;


    }


}

