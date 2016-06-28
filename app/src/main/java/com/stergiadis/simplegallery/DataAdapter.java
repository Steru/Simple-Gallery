package com.stergiadis.simplegallery;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

/**
 * Created by Steru on 2016-06-27.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private String[] mDataset;



    private List<File> mFileList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.grid_content_text_view);
        }
    }

    public DataAdapter(List<File> fileList) { mFileList = fileList; }


    // Provide a suitable constructor (depends on the kind of dataset)
    public DataAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_content, parent, false);

        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);

        holder.mTextView.setText(mFileList.get(position).getName());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
//        return mDataset.length;
        return mFileList.size();

    }
}