package com.stergiadis.simplegallery.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stergiadis.simplegallery.R;
import com.stergiadis.simplegallery.model.Image;

import java.util.List;

/**
 * Created by Steru on 2016-06-27.
 */
public class GridDataAdapter extends RecyclerView.Adapter<GridDataAdapter.ViewHolder> {

    private List<Image> mImageList;
    private Context      mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public ViewHolder(View v) {
            super(v);
            thumbnail         = (ImageView) v.findViewById(R.id.grid_content_image_view);
        }
    }

    public GridDataAdapter(List<Image> imageList, Context context) {
        mImageList = imageList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_content, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(mContext)
                .load(mImageList.get(position).getPath())
                .override(150,150)
                .thumbnail(0.3f)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return mImageList.size();

    }
}