package com.example.app_video;

import com.example.app_video.Adapter.Categories.Categories;
import com.example.app_video.Adapter.HotVideo.Video;
import com.example.app_video.Adapter.ItemCategory.ItemCategory;

import java.util.List;

public interface InterOnClick {

    void onClickVideo(Video video);
    void  oncLickCategory(Categories categories);
    void  onClickItem(ItemCategory itemCategory);

}
