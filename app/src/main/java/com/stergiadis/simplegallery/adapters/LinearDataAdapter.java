package com.stergiadis.simplegallery.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stergiadis.simplegallery.R;
import com.stergiadis.simplegallery.model.Image;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Steru on 2016-06-30.
 */
public class LinearDataAdapter extends RecyclerView.Adapter<LinearDataAdapter.ViewHolder> {

    private List<Image> mImageList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView  imageName;

        public ViewHolder(View v) {
            super(v);
            thumbnail = (ImageView) v.findViewById(R.id.list_content_image_view);
            imageName = (TextView)  v.findViewById(R.id.list_content_text_view);
        }
    }

    public LinearDataAdapter(List<Image> imageList, Context context) {
        mImageList = imageList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.imageName.setText(mImageList.get(position).getName());

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
