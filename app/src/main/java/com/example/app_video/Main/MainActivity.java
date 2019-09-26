package com.example.app_video.Main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.app_video.Contact.Video;
import com.example.app_video.Contact.ItemCategory;
import com.example.app_video.Frag.FragWatchLater;
import com.example.app_video.Frag.FragmentHistorry;
import com.example.app_video.Interface.Click_Item_CateGory;
import com.example.app_video.Frag.FragCategories;
import com.example.app_video.Frag.FragHotVideo;
import com.example.app_video.Interface.IClickVideo;
import com.example.app_video.R;
import com.example.app_video.SQLHelper;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements IClickVideo, Click_Item_CateGory {

    Button btFraghotVideo, btFragCategories;
    ImageView btFragmenu;
    Video clickVideo;
    LinearLayout lnProgressBar;
    ItemCategory item;
    private static final String TAG = "MainActivity";
    SQLHelper sqL;
    Disposable internetDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

        btFragCategories = findViewById(R.id.btFragCategories);
        btFraghotVideo = findViewById(R.id.btFraghotVideo);
        btFragmenu = findViewById(R.id.imgMenu);

       // progressBar = findViewById(R.id.progessBar);
        lnProgressBar = findViewById(R.id.lnProgessbar);

        ActionBar sc = getSupportActionBar();
        sc.hide();

        getFragment(FragHotVideo.newInstance());
        lnProgressBar.setVisibility(View.VISIBLE);


        btFraghotVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragment(FragHotVideo.newInstance());
                lnProgressBar.setVisibility(View.VISIBLE);

            }
        });
        btFragCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragment(FragCategories.newInstance());
                lnProgressBar.setVisibility(View.VISIBLE);
            }
        });
        btFragmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(getBaseContext(), view);
                popupMenu.inflate(R.menu.activity_main_drawer);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.nav_home) {
                            getFragment(FragHotVideo.newInstance());
                            lnProgressBar.setVisibility(View.VISIBLE);
                        }
                        if (item.getItemId() == R.id.nav_History) {
                            getFragment(FragmentHistorry.newInstance());
                            lnProgressBar.setVisibility(View.INVISIBLE);

                        }
                        if (item.getItemId() == R.id.nav_WatchLater) {
                            getFragment(FragWatchLater.newInstance());
                            lnProgressBar.setVisibility(View.INVISIBLE);
                        }
                        return false;
                    }
                });

               popupMenu.show();
            }
        });

    }
// checking internet
    @Override
    protected void onResume() {
        super.onResume();
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnected) throws Exception {
                        if (isConnected==false) {
                            loadDialog();
                        }
                        else {
                            getFragment(FragHotVideo.newInstance());
                            lnProgressBar.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }

    public void getFragment(Fragment fragment) {
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
    public void onClickItem(ItemCategory itemCategory) {
        item = itemCategory;
        sqL = new SQLHelper(getBaseContext());
        sqL.insertItem(itemCategory);
    }

    public void loadDialog(){
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Lost Internet");
        dialog.setMessage("Checking again");
        dialog.setPositiveButton("Go to Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

}
