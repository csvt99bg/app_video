package com.example.app_video.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_video.Contact.Video;
import com.example.app_video.Interface.IClickVideo;
import com.example.app_video.R;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    Context context;
    ArrayList<Video> videoList;
    IClickVideo clickVideo;
    public void setClickVideo(IClickVideo clickVideo) {
        this.clickVideo = clickVideo;
    }
    public VideoAdapter(ArrayList<Video> videoList) {
        this.videoList = videoList;
    }
    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context=parent.getContext();

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        final  Video video =videoList.get(position);
        holder.tvNameVideo.setText(video.getNameVideo());
        //holder.tvTimePlay.setText(video.timePlay);
        holder.tvTimeCreat.setText(video.getTimeCreat());
        Glide.with(context).load(video.getAvatar()).into(holder.avatar);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    clickVideo.onClickVideo(video);
            }
        });
    }
    @Override
    public int getItemCount() {
        return videoList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameVideo, tvTimePlay, tvTimeCreat;
        ImageView btStart;
        RelativeLayout relativeLayout;
        ImageView avatar;
        public ViewHolder(@NonNull View view) {
            super(view);
            tvNameVideo = view.findViewById(R.id.tvNameVideo);
            tvTimeCreat = view.findViewById(R.id.tvTimeCreat);
           // tvTimePlay = view.findViewById(R.id.tvTimePlay);
            btStart = view.findViewById(R.id.btStart);
            avatar = view.findViewById(R.id.imgAvatar);
            relativeLayout=view.findViewById(R.id.videorelative);
        }
    }
}
