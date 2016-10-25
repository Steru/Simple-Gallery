package com.stergiadis.simplegallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Created by Steru on 2016-06-28.
 */
public class FullscreenImageFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_IMG_NAME = "imageName";
    private static final String ARG_IMG_PATH = "imagePath";
    private int mSelectedPosition;
    private String mName, mPath;

    public FullscreenImageFragment(){
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.mSelectedPosition = args.getInt(ARG_POSITION);
        this.mName = args.getString(ARG_IMG_NAME);
        this.mPath = args.getString(ARG_IMG_PATH);
    }

    public static FullscreenImageFragment newInstance(int position, String name, String path) {
        FullscreenImageFragment fragment = new FullscreenImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_IMG_NAME, name);
        args.putString(ARG_IMG_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_fullscreen_image, container, false);
        ImageView iv = (ImageView) v.findViewById(R.id.fragment_fullscreen_imageview);
        if(savedInstanceState != null) {
            mPath = savedInstanceState.getString("imagePath");
        }
        Glide.with(getActivity())
                .load(mPath)
                .thumbnail(0.3f)
                .fitCenter()
                .into(iv);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("currentPosition", mSelectedPosition);
        savedInstanceState.putString("imagePath", mPath);
    }
}


