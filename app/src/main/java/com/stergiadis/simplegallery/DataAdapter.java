package com.stergiadis.simplegallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Steru on 2016-06-27.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Image> mImageList;
    private Context      mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView imageNameTextView;
        public ImageView thumbnail;

        public ViewHolder(View v) {
            super(v);
            imageNameTextView = (TextView) v.findViewById(R.id.grid_content_text_view);
            thumbnail         = (ImageView) v.findViewById(R.id.grid_content_image_view);
        }
    }

    public DataAdapter(List<Image> imageList, Context context) {
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

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.imageNameTextView.setText(mDataset[position]);

//        holder.imageNameTextView.setText(mImageList.get(position).getName());

        Glide.with(mContext)
                .load(mImageList.get(position).getPath())
                .override(150,150)
                .thumbnail(0.3f)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.thumbnail);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
//        return mDataset.length;
        return mImageList.size();

    }
}