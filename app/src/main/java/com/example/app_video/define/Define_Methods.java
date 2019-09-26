package com.example.app_video.define;

import com.example.app_video.Contact.ItemCategory;

import java.util.ArrayList;

public class Define_Methods {
    public  boolean CHECK (String title, ArrayList<ItemCategory> arrayList){
        for (ItemCategory itemCategory:arrayList){
            if (title.equals(itemCategory.getName())==true){
                return true;
            }
        }
        return false;
    }
}
