package com.stergiadis.simplegallery;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.stergiadis.simplegallery.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridRecyclerView extends AppCompatActivity {

    private static final int MAX_NUMBER_OF_GRID_ELEMENTS = 750;
    private static final String[] EXTENSIONS_STRING_TAB = {"jpg", "bmp", "png", "jpeg"};
    private static final int GRID_SPAN_COUNT = 3;

    public static final String PARCELABLE_NAME_IMAGE_LIST = "ImageList";
    public static final String PARCELABLE_NAME_POSITION = "position";

    private int mMaxImageSubListItems;

    private RecyclerView                mRecyclerView;
    private RecyclerView.Adapter        mAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;

    private SwipeRefreshLayout          mSwipeRefreshLayout;

    private List<File>     mFileList;
    private ArrayList<Image>    mImageList;
    private ArrayList<Image>    mImageSubList; //MAX_NUMBER_OF_GRID_ELEMENTS images drawn from mImageList


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_recycler_view);


        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getApplicationContext(), GRID_SPAN_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mFileList = new ArrayList<>();

        boolean cardIsPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(cardIsPresent == false) {

            Toast.makeText(getApplicationContext(), R.string.error_msg_no_sdcard_found, Toast.LENGTH_LONG);

        } else {
            getFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(),
                    mFileList);

            if (mFileList.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.error_msg_no_files_found, Toast.LENGTH_LONG);
            } else {


                //write filenames and paths from List<File> to List<Image>
                mImageList = new ArrayList<>();
                for (File f : mFileList) {
                    Image img = new Image();
                    img.setName(f.getName());
                    img.setPath(f.getAbsolutePath());
                    mImageList.add(img);
                }

                if (mImageList.size() > MAX_NUMBER_OF_GRID_ELEMENTS) {
                    mMaxImageSubListItems = MAX_NUMBER_OF_GRID_ELEMENTS;
                } else{
                    mMaxImageSubListItems = mImageList.size();
                }

                mImageSubList = new ArrayList<>();
                if (savedInstanceState != null) {
                    mImageSubList = savedInstanceState.getParcelableArrayList(GridRecyclerView.PARCELABLE_NAME_IMAGE_LIST);
                } else {
                    //only n files showing, user can shuffle those with swipe down (swipeRefresh)
                    Collections.shuffle(mImageList);
                    mImageSubList = new ArrayList<>(mImageList.subList(0, mMaxImageSubListItems));
                }

                mAdapter = new DataAdapter(mImageSubList, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);

                setGridListeners();

                setSwipeRefresh();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList(GridRecyclerView.PARCELABLE_NAME_IMAGE_LIST, mImageSubList);

        super.onSaveInstanceState(savedInstanceState);
    }


    private void setGridListeners(){
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                    if (mImageList.isEmpty()){
                        Toast.makeText(getApplicationContext(), R.string.error_msg_no_files_found , Toast.LENGTH_LONG);
                    }else {
                        Intent intent = new Intent(getApplicationContext(), FullscreenImageActivity.class);
                        intent.putParcelableArrayListExtra(PARCELABLE_NAME_IMAGE_LIST, mImageSubList);
                        intent.putExtra(PARCELABLE_NAME_POSITION, position);
                        startActivity(intent);
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });
    }

    private void setSwipeRefresh(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Collections.shuffle(mImageList);

                mImageSubList = new ArrayList<>(mImageList.subList(0,mMaxImageSubListItems));
                mAdapter = new DataAdapter(mImageSubList, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    // Search the dirName directory and subdirectories for image files
    private void getFiles(String dirName, List<File> fileList){

        File dir = new File(dirName);

        File[] fileTab = dir.listFiles();

        for(File file : fileTab) {
            //check if it's a directory or a file, and check if the file has the right extension
            if(file.isFile() && fileIsAnImage(file.getName())) {
                fileList.add(file);
            } else if(file.isDirectory()){
                getFiles(file.getAbsolutePath(), fileList);
            }
        }

    }

    private boolean fileIsAnImage(String filename) {
        for (String extString : EXTENSIONS_STRING_TAB){
            if (filename.endsWith("." + extString)) {
                return true;
            }
        }
        return false;
    }
}

